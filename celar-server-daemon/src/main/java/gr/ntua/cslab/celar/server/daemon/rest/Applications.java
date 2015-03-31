package gr.ntua.cslab.celar.server.daemon.rest;

import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Node;
import com.sixsq.slipstream.persistence.Target;
import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.ComponentDependency;
import gr.ntua.cslab.celar.server.beans.Module;
import gr.ntua.cslab.celar.server.beans.ModuleDependency;
import gr.ntua.cslab.celar.server.beans.ResizingAction;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.beans.structured.DeployedApplication;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import static gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents.ssService;
import static gr.ntua.cslab.database.EntityGetters.getApplicationById;
import static gr.ntua.cslab.database.EntityGetters.getDependencies;
import static gr.ntua.cslab.database.EntityGetters.getResizingActions;
import gr.ntua.cslab.database.EntitySearchers;
import static gr.ntua.cslab.database.parsers.ApplicationParser.exportApplication;
import gr.ntua.cslab.database.parsers.ToscaHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import tools.CSARParser;
import tools.Parser;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/application/")
public class Applications {

    public static Logger logger = Logger.getLogger(Applications.class);
    
    private static String storeFile(InputStream input, String prefix) {
        try {
            java.nio.file.Path tempFile = Files.createTempFile(prefix, null);
            long bytes = Files.copy(input, tempFile, REPLACE_EXISTING);
            input.close();
            logger.info("Copied " + bytes + " bytes to " + tempFile);
            return tempFile.toString();
        } catch (IOException ioe) {
            logger.error("Failed to store the temp file");
            ioe.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("{id}/")
    public ApplicationInfo getApplicationInfo(@PathParam("id") String id) throws Exception {
        ApplicationInfo info = exportApplication(getApplicationById(id));
        return info;
    }

    @GET
    @Path("search/")
    public static REList<Application> searchApplicationsByProperty(
            @DefaultValue("0") @QueryParam("submitted_start") long submittedStart,
            @DefaultValue("0") @QueryParam("submitted_end") long submittedEnd,
            @QueryParam("description") String description,
            @QueryParam("user_id") int userid,
            @QueryParam("module_name") String moduleName,
            @QueryParam("component_description") String componentDescription,
            @QueryParam("provided_resource_id") String providedResourceId) throws Exception {
       
        List<Application> apps= EntitySearchers.searchApplication(submittedStart, submittedEnd,
                description ,userid, moduleName, componentDescription, providedResourceId);
       
        return new REList(apps);
    }


    @POST
    @Path("describe/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public static ApplicationInfo describe(@Context HttpServletRequest request, InputStream input) throws IOException, Exception {
// fetching csar file and store it to local fs
        String tempFileName = storeFile(input, "temp-csar-describe");
        //create a Parser instance
        Parser tc = new CSARParser(tempFileName);
        //store the description in  the DB
        ToscaHandler th = new ToscaHandler(tc);
        th.storeDescription();
        
        if (ssService != null) {
        //application name and version
            //String ssApplicationName = System.currentTimeMillis() + "-" + tc.getAppName();
            String ssApplicationName = th.getApplication().getSlipstreamName();
            logger.info("Application: " + tc.getAppName() + "_v" + tc.getAppVersion());
            String appName = ssApplicationName;
            appName = ssService.createApplication(ssApplicationName, tc.getAppVersion());

            HashMap<String, Node> nodes = new HashMap();
            //iterate through modules
            for (String module : tc.getModules()) {
                logger.info("\t" + module);

                //module dependecies
                logger.info("\t\tdepends on: " + tc.getModuleDependencies(module));

                //iterate through components
                for (String component : tc.getModuleComponents(module)) {
                    logger.info("\t\t" + component);
                    ImageModule imModule = new ImageModule(appName + "/" + component);
                    Authz auth = new Authz(ssService.getUser(), imModule);
                    imModule.setAuthz(auth);

                    //component dependencies
                    logger.info("\t\t\tdepends on: " + tc.getComponentDependencies(component));

                    //component properties
                    List<ModuleParameter> parameters = new ArrayList<ModuleParameter>();
                    Set<Target> targets = new HashSet<Target>();
                    for (Map.Entry prop : tc.getComponentProperties(component).entrySet()) {
                        if (prop.getKey().toString().contains("ImageArtifactPropertiesType")) {
                            logger.info("\t\t\t" + prop.getKey() + " : " + prop.getValue());
                            imModule.setModuleReference(ssService.getImageReference(prop.getValue().toString()));
                        } else if (prop.getKey().toString().equals("executeScript")) {
                            logger.info("\t\t\t" + prop.getKey());
                            logger.debug("Execute script: " + prop.getValue().toString());
                            parameters.addAll(ssService.getOutputParamsFromScript(prop.getValue().toString()));
                            Target t = new Target(Target.EXECUTE_TARGET, ssService.patchExecuteScript(prop.getValue().toString()));
                            targets.add(t);
                        } else if (prop.getKey().toString().contains("Add")) {
                            logger.info("\t\t\t" + prop.getKey());
                            logger.debug("Add script: " + prop.getValue().toString());
                            parameters.addAll(ssService.getOutputParamsFromScript(prop.getValue().toString()));
                            Target t = new Target(Target.ONVMADD_TARGET, prop.getValue().toString());
                            targets.add(t);
                        } else if (prop.getKey().toString().contains("Remove")) {
                            logger.info("\t\t\t" + prop.getKey());
                            logger.debug("Remove script: " + prop.getValue().toString());
                            parameters.addAll(ssService.getOutputParamsFromScript(prop.getValue().toString()));
                            Target t = new Target(Target.ONVMREMOVE_TARGET, prop.getValue().toString());
                            targets.add(t);
                        } else if (prop.getKey().toString().equals("flavor")) {
                            logger.info("\t\t\t" + prop.getKey() + " : " + prop.getValue());
                            parameters.addAll(ssService.createFlavorParameters(prop.getValue().toString()));
                        }
                    }
                    imModule.setTargets(targets);
                    
                    for (ModuleParameter p : parameters) {
                        imModule.setParameter(p);
                    }
                    for (ModuleParameter p : ssService.baseParameters) {
                        imModule.setParameter(p);
                    }
                    
                    ssService.putModule(imModule);
                    nodes.put(imModule.getShortName(), new Node(imModule.getShortName(), imModule));
                    
                }
            }
            //add DeploymentModule 
            String name = appName + "/" + appName;
            logger.info("Adding deployment module" + name);
            DeploymentModule deployment = new DeploymentModule(name);
            Authz auth = new Authz(ssService.getUser(), deployment);
            deployment.setAuthz(auth);
            logger.info("App Modules: " + nodes);
            deployment.setNodes(nodes);
            ssService.putModule(deployment);
        }


        ApplicationInfo info = exportApplication(th.getApplication());
        return info;
    }

    @POST
    @Path("{id}/deploy/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public static DeployedApplication launchDeployment(@Context HttpServletRequest request, @PathParam("id") String applicationId, InputStream input) throws Exception {
        logger.info("Deploying application: "+applicationId);
        String tempFileName = storeFile(input, "temp-csar-deploy");
        // parse TOSCA and give params to deployment
        
        ApplicationInfo app = exportApplication(getApplicationById(applicationId));

        Map<String, String> params = new HashMap<>();
        //create a Parser instance
        Parser tc = new CSARParser(tempFileName);
        ToscaHandler tp = new ToscaHandler(tc);
        //iterate through modules
        for (String module : tc.getModules()) {
            logger.info("\t" + module);
            //iterate through components
            for (String component : tc.getModuleComponents(module)) {
                logger.info("\t\t" + component);
                for (Map.Entry prop : tc.getComponentProperties(component).entrySet()) {
                    if (prop.getKey().toString().equals("minInstances")) {
                        logger.info("minInstances: " + prop.getValue().toString());
                    	params.put(component+":multiplicity", prop.getValue().toString());
                    } 
                }
            }
        }
        
        
        //TODO deployment ID
        String deploymentId;
        if(ssService==null) deploymentId= "" + (new java.util.Random()).nextInt();
        else deploymentId = ssService.launchApplication(app.getSlipstreamName(), params);
        
        //TODO orchestrator IP 
        DeployedApplication ai =  tp.storeDeployment(app, deploymentId,"??IP??");
        return ai;
    }


    
    @GET
    @Path("component/{component_id}/resizing_actions")
    public static REList<ResizingAction> getComponentResizingActions(@PathParam("component_id") int componentId) throws Exception {
        Component c = new Component(componentId);
        return new REList<>(getResizingActions(null, c));
    }
    

    @GET
    @Path("component/{component_id}/dependencies")
    public static REList<ComponentDependency> getComponentDependencies(@PathParam("component_id") int componentId) throws Exception {
        Component c = new Component(componentId);
        return new REList<>(getDependencies(c));
    }
    
    @GET
    @Path("module/{module_id}/dependencies")
    public static REList<ModuleDependency> getModuleDependencies(@PathParam("module_id") int moduleId) throws Exception {
        Module c = new Module(moduleId);
        return new REList<>(getDependencies(c));
    }
    
}
