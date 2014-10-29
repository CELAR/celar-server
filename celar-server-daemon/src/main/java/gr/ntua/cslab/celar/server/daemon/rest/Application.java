package gr.ntua.cslab.celar.server.daemon.rest;

import com.sixsq.slipstream.exceptions.ValidationException;
import gr.ntua.cslab.celar.server.daemon.rest.beans.ApplicationServerInfo;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Node;
import com.sixsq.slipstream.persistence.Target;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.Main;
import gr.ntua.cslab.celar.server.daemon.cache.ApplicationCache;
import gr.ntua.cslab.celar.server.daemon.cache.DeploymentCache;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfoList;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;
import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;

import static gr.ntua.cslab.database.EntityGetters.getApplicationById;
import static gr.ntua.cslab.database.parsers.ApplicationParser.exportApplication;
import gr.ntua.cslab.database.parsers.ToscaHandler;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;

import tools.CSARParser;
import tools.Parser;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/application/")
public class Application {

    public Logger logger = Logger.getLogger(Application.class);
    
    
//    @GET
//    public ApplicationInfoList getApplications() {
//        return ApplicationCache.getApplications();
//    }
    // IS calls

    @GET
    @Path("{id}/")
    public ApplicationInfo getApplicationInfo(@PathParam("id") String id) throws Exception {
        ApplicationInfo info = exportApplication(getApplicationById(id));
        return info;
    }

    @GET
    @Path("search/")
    public List<ApplicationInfo> searchApplicationsByProperty(
            @DefaultValue("0") @QueryParam("submitted_start") long submittedStart,
            @DefaultValue("0") @QueryParam("submitted_end") long submittedEnd,
            @DefaultValue("Null") @QueryParam("description") String description,
            @DefaultValue("0") @QueryParam("user_id") int userid,
            @DefaultValue("Null") @QueryParam("module_name") String moduleName,
            @DefaultValue("Null") @QueryParam("component_description") String componentDescription,
            @DefaultValue("Null") @QueryParam("provided_resource_id") String providedResourceId) {
        List<ApplicationInfo> list = new LinkedList<>();
        return list;
    }

    @GET
    @Path("{id}/description/")
    public ApplicationInfo getApplicationDescription(@PathParam("id") String id) {
        ApplicationInfo info = new ApplicationInfo();
        return info;
    }

    @POST
    @Path("describe/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public ApplicationInfo describe(@Context HttpServletRequest request, InputStream input) throws IOException, Exception {
// fetching csar file and store it to local fs
        String tempFileName = storeFile(input, "temp-csar-describe");
        //create a Parser instance
        Parser tc = new CSARParser(tempFileName);
        
        //application name and version
        //String ssApplicationName = System.currentTimeMillis() + "-" + tc.getAppName();
        String ssApplicationName = tc.getAppName();
        logger.info("Application: " + tc.getAppName() + " v" + tc.getAppVersion());
        String appName = ServerStaticComponents.ssService.createApplication(ssApplicationName, tc.getAppVersion());
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        //iterate through modules
        for (String module : tc.getModules()) {
            logger.info("\t" + module);

            //module dependecies
            logger.info("\t\tdepends on: " + tc.getModuleDependencies(module));

            //iterate through components
            for (String component : tc.getModuleComponents(module)) {
                logger.info("\t\t" + component);
                ImageModule imModule = new ImageModule(appName + "/" + component);
                Authz auth = new Authz(ServerStaticComponents.ssService.getUser(), imModule);
                imModule.setAuthz(auth);

                //component dependencies
                logger.info("\t\t\tdepends on: " + tc.getComponentDependencies(component));

                //component properties
                List<ModuleParameter> parameters = new ArrayList<ModuleParameter>();
                Set<Target> targets = new HashSet<Target>();
                for (Map.Entry prop : tc.getComponentProperties(component).entrySet()) {
                    if (prop.getKey().toString().equals("VMI")) {
                    	logger.info("\t\t\t"+prop.getKey()+" : "+prop.getValue());
                    	imModule.setModuleReference(ServerStaticComponents.ssService.getImageReference("ubuntu-12.04"));
                    } 
                    else if (prop.getKey().toString().equals("executeScript")) {
                    	logger.info("\t\t\t"+prop.getKey());
                        logger.debug("Execute script: " + prop.getValue().toString());
                    	parameters.addAll(ServerStaticComponents.ssService.getOutputParamsFromScript(prop.getValue().toString()));
                		Target t = new Target(Target.EXECUTE_TARGET, ServerStaticComponents.ssService.patchExecuteScript(prop.getValue().toString()));
                		targets.add(t);
                    }
                    else if (prop.getKey().toString().contains("Add")) {
                    	logger.info("\t\t\t"+prop.getKey());
                        logger.debug("Add script: " + prop.getValue().toString());
                    	parameters.addAll(ServerStaticComponents.ssService.getOutputParamsFromScript(prop.getValue().toString()));
                		Target t = new Target(Target.ONVMADD_TARGET, prop.getValue().toString());
                		targets.add(t);
                    }
                    else if (prop.getKey().toString().contains("Remove")) {
                    	logger.info("\t\t\t"+prop.getKey());
                        logger.debug("Remove script: " + prop.getValue().toString());
                    	parameters.addAll(ServerStaticComponents.ssService.getOutputParamsFromScript(prop.getValue().toString()));
                		Target t = new Target(Target.ONVMREMOVE_TARGET, prop.getValue().toString());
                		targets.add(t);
                    }
                    else if (prop.getKey().toString().equals("flavor")) {
                    	logger.info("\t\t\t"+prop.getKey()+" : "+prop.getValue());
                    	parameters.addAll(ServerStaticComponents.ssService.createFlavorParameters(prop.getValue().toString()));
                    }
                }
                imModule.setTargets(targets);

            	for(ModuleParameter p : parameters){
            		imModule.setParameter(p);
            	}
                for (ModuleParameter p : ServerStaticComponents.ssService.baseParameters) {
                    imModule.setParameter(p);
                }

                ServerStaticComponents.ssService.putModule(imModule);
                nodes.put(imModule.getShortName(), new Node(imModule.getShortName(), imModule));

            }
        }

        //add DeploymentModule 
        String name = appName + "/" + appName;
        DeploymentModule deployment = new DeploymentModule(name);
        Authz auth = new Authz(ServerStaticComponents.ssService.getUser(), deployment);
        deployment.setAuthz(auth);
        logger.info("App Modules: " + nodes);
        deployment.setNodes(nodes);

        ServerStaticComponents.ssService.putModule(deployment);
        //store the description in  the DB
        ToscaHandler th = new ToscaHandler(tc);
        th.storeDescription();
        
        ApplicationInfo info = exportApplication(th.getApplication());
        
        //extra information for SlipStream
        ApplicationServerInfo  serverInfo = new ApplicationServerInfo(info);
        serverInfo.setCsarFilePath(tempFileName);
        serverInfo.setSlipstreamName(name);
        
        ApplicationCache.insertApplication(serverInfo);

        return info;
    }

    @POST
    @Path("{id}/deploy/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public ApplicationInfo launchDeployment(@PathParam("id") String applicationId, InputStream input) throws Exception {
        String tempFileName = storeFile(input, "temp-csar-deploy");

        // parse TOSCA and give params to deployment
        
        ApplicationServerInfo app = ApplicationCache.getApplicationById(applicationId);

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
        
        
        
        String deploymentID = ServerStaticComponents.ssService.launchApplication(app.getSlipstreamName(), params);

        ApplicationInfo ai =  tp.storeDeployment(app, deploymentID);
        System.out.println(ai.getAllResources());
        return ai;
    }

    @GET
    @Path("{id}/deployments/")
    public DeploymentInfoList getDeploymentsByApplicationId(@PathParam("id") String applicationId) {
        List<DeploymentInfo> res = DeploymentCache.getDeploymentsByApplication(applicationId);
        return new DeploymentInfoList(res);
    }
    
    private String storeFile(InputStream input, String prefix) {
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
    
    @POST
    @Path("describe2/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public ApplicationInfo describe2(@Context HttpServletRequest request, InputStream input) throws Exception {
        // fetching csar file and store it to local fs
        String tempFileName = storeFile(input, "temp-csar-describe");
        //creates the tosca handler object from a scar file path
        ToscaHandler tp = new ToscaHandler(tempFileName);
        //stores the application Description in the database
        tp.storeDescription();
        //construct the application description from the database and return it
        return exportApplication(tp.getApplication());
    }
    
    @POST
    @Path("deploy2/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public ApplicationInfo deploy2(@Context HttpServletRequest request, String applicationId, InputStream input) throws Exception {
        // fetching csar file and store it to local fs
        String tempFileName = storeFile(input, "temp-csar-deploy");
        //creates the tosca handler object from a scar file path
        ToscaHandler tp = new ToscaHandler(tempFileName);
        
        //Dummy deploymentID
        String deploymentId = "" + (new java.util.Random()).nextInt();
        //stores the application Deployment in the database
        gr.ntua.cslab.celar.server.beans.Application app = getApplicationById(applicationId);
        ApplicationInfo ai =  tp.storeDeployment(app, deploymentId);
        System.out.println(ai.getAllResources());
        return ai;

    }
}
