package gr.ntua.cslab.celar.server.daemon.rest;

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Node;
import com.sixsq.slipstream.persistence.Target;

import gr.ntua.cslab.celar.server.daemon.Main;
import gr.ntua.cslab.celar.server.daemon.cache.ApplicationCache;
import gr.ntua.cslab.celar.server.daemon.cache.DeploymentCache;
import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfoList;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfoList;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;
import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    
    
    @GET
    public ApplicationInfoList getApplications() {
        return ApplicationCache.getApplications();
    }
    // IS calls

    @GET
    @Path("{id}/")
    public ApplicationInfo getApplicationInfo(@PathParam("id") String id) {
        ApplicationInfo info = new ApplicationInfo();
        info.setId(id);
        info.setDescription("Hello world :)");
        info.setSubmitted(new Date().getTime());
        info.setVersion("1.1.0");
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
        list.add(new ApplicationInfo("ID1", submittedStart + 100, description, "1.0.0"));
        list.add(new ApplicationInfo("ID2", submittedStart + 200, description, "1.2.0"));
        list.add(new ApplicationInfo("ID3", submittedStart + 1000, description, "0.2.0"));
        return list;
    }

    @GET
    @Path("{id}/description/")
    public ApplicationInfo getApplicationDescription(@PathParam("id") String id) {
        ApplicationInfo info = new ApplicationInfo();
        info.setId(id);
        info.setDescription("This xml will be replaced by a TOSCA (or CSAR??) file...");
        info.setSubmitted(new Date().getTime());
        info.setVersion("1.1.0");
        return info;
    }

    @POST
    @Path("describe/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public ApplicationInfo describe(@Context HttpServletRequest request, InputStream input) throws IOException, Exception {
        // fetching csar file and store it to local fs
        String filename = "/tmp/csar/" + System.currentTimeMillis() + ".csar";
        byte[] buffer = new byte[1024];
        OutputStream file = new FileOutputStream(filename);
        
        int count, sum = 0;

        while ((count = input.read(buffer)) != -1) {
            sum+=count;
            file.write(buffer, 0, count);
        }

        file.flush();
        file.close();
        Logger.getLogger(Application.class).info("Read CSAR file (" + sum + " bytes)");
        input.close();

        //create a Parser instance
        Parser tc = new CSARParser(filename);

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
                Set<Target> targets = new HashSet<Target>();
                for (Map.Entry prop : tc.getComponentProperties(component).entrySet()) {
                    //System.out.println("\t\t\t"+prop.getKey()+": "+prop.getValue());
                    if (prop.getKey().toString().equals("VMI")) {
                        imModule.setModuleReference(ServerStaticComponents.ssService.getImageReference("ubuntu-12.04"));
                    } else if (prop.getKey().toString().equals("executeScript")) {
                        logger.info("script: " + prop.getValue().toString());
                        Target t = new Target(Target.EXECUTE_TARGET, prop.getValue().toString());
                        targets.add(t);
                    }
                }
                imModule.setTargets(targets);

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

        ApplicationInfo info = new ApplicationInfo();
        info.setCsarFilePath(filename);
        info.setId(UUID.randomUUID().toString());
        info.setSubmitted(System.currentTimeMillis());
        info.setVersion("1.0");
        info.setDescription("No description for now dude!");
        info.setSlipstreamName(name);

        ApplicationCache.insertApplication(info);

        return info;
    }

    @POST
    @Path("{id}/deploy/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public DeploymentInfo launchDeployment(@PathParam("id") String applicationId, InputStream input) throws Exception {

        // fetching csar file and store it to local fs
        String filename = "/tmp/csar/" + System.currentTimeMillis() + ".csar";
        byte[] buffer = new byte[1024];
        OutputStream file = new FileOutputStream(filename);
        
        int count, sum = 0;

        while ((count = input.read(buffer)) != -1) {
            sum+=count;
            file.write(buffer, 0, count);
        }

        file.flush();
        file.close();
        Logger.getLogger(Application.class).info("Read CSAR file (" + sum + " bytes)");
        input.close();

        // parse TOSCA and give params to deployment
        
        ApplicationInfo app = ApplicationCache.getApplicationById(applicationId);

        Map<String, String> params = new HashMap<>();
        //create a Parser instance
        Parser tc = new CSARParser(filename);
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

        DeploymentInfo deployment = new DeploymentInfo();
        deployment.setDeploymentID(deploymentID);
        deployment.setApplication(app);
        deployment.setStartTime(System.currentTimeMillis());
        deployment.setEndTime(-1);
        deployment.setState(ServerStaticComponents.ssService.getDeploymentState(deploymentID));
        deployment.setDescription(params.toString());
        DeploymentCache.addDeployment(deployment);
        return deployment;
    }

    @GET
    @Path("{id}/deployments/")
    public DeploymentInfoList getDeploymentsByApplicationId(@PathParam("id") String applicationId) {
        List<DeploymentInfo> res = DeploymentCache.getDeploymentsByApplication(applicationId);
        return new DeploymentInfoList(res);
    }
}
