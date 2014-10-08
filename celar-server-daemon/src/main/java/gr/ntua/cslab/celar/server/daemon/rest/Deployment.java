package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.Main;
import gr.ntua.cslab.celar.server.daemon.cache.DeploymentCache;
import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/deployment/")
public class Deployment {
	
    @GET
    @Path("{deploymentID}/")
    public DeploymentInfo getDeployment(@PathParam("deploymentID") String deploymentID) throws Exception {
    	DeploymentInfo retInfo = DeploymentCache.removeDeployment(deploymentID);
    	if(retInfo==null){
    		return new DeploymentInfo(deploymentID, new ApplicationInfo(), new Date().getTime()-10000l, new Date().getTime()+10000l, "NOT FOUND");
    	}
    	else{
    		String status = Main.ssService.getDeploymentState(deploymentID);
    		retInfo.setStatus(status);
    		DeploymentCache.addDeployment(retInfo);
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

	
}
