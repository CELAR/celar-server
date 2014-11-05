package gr.ntua.cslab.celar.server.daemon.rest;

import com.sixsq.slipstream.statemachine.States;
import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;
import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import static gr.ntua.cslab.database.EntityGetters.getApplicationById;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentById;
import static gr.ntua.cslab.database.EntityTools.removeDeployment;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceException;
import org.apache.log4j.Logger;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/deployment/")
public class Deployments {
    public static Logger logger = Logger.getLogger(Applications.class);
    
    
    @GET
    @Path("{id}/")
    public static Deployment getDeployment(@PathParam("id") String deploymentID) throws Exception {
        logger.info("Get deployment: "+deploymentID);
        Deployment dep =  getDeploymentById(deploymentID);;
        States state = null;
        //TODO get state
//      state = ServerStaticComponents.ssService.getDeploymentState(deploymentID);
        dep.setState(state);
        //TODO what is this?
//      HashMap<String, String> ips = ServerStaticComponents.ssService.getDeploymentIPs(deploymentID);
//    	dep.setDescription(ips.toString());
        return dep;
    }
	

    
    @GET
    @Path("search/")
    public List<Deployment> searchDeployments(
            @DefaultValue("0") @QueryParam("start_time") long startTime,
            @DefaultValue("0") @QueryParam("end_time") long endTime,
            @DefaultValue("ERROR") @QueryParam("status") DeploymentStatus status,
            @DefaultValue("-1") @QueryParam("application_id") int applicationId) {
    	logger.info("Searchin deployment");
        throw new UnsupportedOperationException("not implemented");
    }

    @GET
    @Path("{id}/terminate/")
    public static String terminateDeployment(@PathParam("id") String deploymentID) throws Exception {
        logger.info("Terminating deployment: "+deploymentID);
        Deployment dep = getDeploymentById(deploymentID);
        //TODO terminate SlipStream
    	//ServerStaticComponents.ssService.terminateApplication(deploymentID);
        removeDeployment(dep);    	
        return "OK";
    }
    
    
    @GET
    @Path("{deployment_id}/tosca/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getApplicationDescription(@PathParam("deployment_id") String deploymentId) throws IOException, Exception {
        logger.info("Sencing csar file");
        Deployment depl = getDeploymentById(deploymentId);
        Application father=  getApplicationById(depl.getApplication_Id());
        return  Files.readAllBytes(Paths.get((father.description_file_location)));
    }
	
}
