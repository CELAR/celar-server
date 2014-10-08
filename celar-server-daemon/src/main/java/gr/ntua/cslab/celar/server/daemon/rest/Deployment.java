package gr.ntua.cslab.celar.server.daemon.rest;

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
	private static HashMap<String,DeploymentInfo> deployments = new HashMap<String,DeploymentInfo>();
	
    @GET
    @Path("{deploymentID}/")
    public static DeploymentInfo getDeployment(@PathParam("deploymentID") String deploymentID) {
    	DeploymentInfo deploymentInfo = deployments.get(deploymentID);
    	if(deploymentInfo==null){
            return new DeploymentInfo(deploymentID, new ApplicationInfo(), new Date().getTime()-10000l, new Date().getTime()+10000l, DeploymentStatus.ERROR);
    	}
    	else{
    		return deploymentInfo;
    	}
    }
    
    @GET
    @Path("search/")
    public List<DeploymentInfo> searchDeployments(
            @DefaultValue("0") @QueryParam("start_time") long startTime,
            @DefaultValue("0") @QueryParam("end_time") long endTime,
            @DefaultValue("ERROR") @QueryParam("status") DeploymentStatus status,
            @DefaultValue("-1") @QueryParam("application_id") int applicationId) {
        List<DeploymentInfo> list = new ArrayList<DeploymentInfo>();
        list.addAll(deployments.values());
        return list;
    }

	public static void addDeployment(DeploymentInfo deployment) {
		deployments.put(deployment.getDeploymentID(), deployment);
	}
	
	public static void removeDeployment(DeploymentInfo deployment) {
		deployments.remove(deployment).getDeploymentID();
	}
	
}
