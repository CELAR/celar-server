package gr.ntua.cslab.celar.server.daemon.rest;

import antlr.ByteBuffer;
import gr.ntua.cslab.celar.server.daemon.Main;
import gr.ntua.cslab.celar.server.daemon.cache.DeploymentCache;
import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.sixsq.slipstream.exceptions.ValidationException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/deployment/")
public class Deployment {
	
    @GET
    @Path("{deploymentID}/")
    public DeploymentInfo getDeployment(@PathParam("deploymentID") String deploymentID) throws Exception {
    	DeploymentInfo retInfo = DeploymentCache.getDeployment(deploymentID);
    	if(retInfo==null){
    		return new DeploymentInfo(deploymentID, new ApplicationInfo(), new Date().getTime()-10000l, new Date().getTime()+10000l, "NOT FOUND","No Description");
    	}
    	else{
    		String status = Main.ssService.getDeploymentState(deploymentID);
    		HashMap<String, String> ips = Main.ssService.getDeploymentIPs(deploymentID);
    		retInfo.setStatus(status);
    		retInfo.setDescription(ips.toString());
        	return retInfo;
    	}
    }
    
    @GET
    @Path("search/")
    public List<DeploymentInfo> searchDeployments(
            @DefaultValue("0") @QueryParam("start_time") long startTime,
            @DefaultValue("0") @QueryParam("end_time") long endTime,
            @DefaultValue("ERROR") @QueryParam("status") DeploymentStatus status,
            @DefaultValue("-1") @QueryParam("application_id") int applicationId) {
    	
        return DeploymentCache.searchDeployments(startTime, endTime, status, applicationId);
    }

    @POST
    @Path("{deploymentID}/terminate/")
    public DeploymentInfo terminateDeployment(@PathParam("deploymentID") String deploymentID) throws IOException, InterruptedException, ValidationException {
    	Main.ssService.terminateApplication(deploymentID);

    	DeploymentInfo deploymentInfo = DeploymentCache.removeDeployment(deploymentID);
    	deploymentInfo.setStatus("FINISHED");
    	
        return deploymentInfo;
    }
    
    
    @GET
    @Path("{deployment_id}/tosca/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] getApplicationDescription(@PathParam("deployment_id") String deploymentId) throws IOException {
        DeploymentInfo depl = DeploymentCache.getDeployment(deploymentId);
        byte[] array = Files.readAllBytes(Paths.get(depl.getApplication().getCsarFilePath()));
        return array;
    }
	
}
