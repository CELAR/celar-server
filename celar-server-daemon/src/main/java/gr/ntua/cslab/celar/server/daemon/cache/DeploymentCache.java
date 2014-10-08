package gr.ntua.cslab.celar.server.daemon.cache;

import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public class DeploymentCache {

	private static HashMap<String,DeploymentInfo> deployments = new HashMap<String,DeploymentInfo>();
    
    
    public static void allocateCache() {
    	DeploymentCache.deployments = new HashMap<>();
    }
    
    /**
     * Search a deployment based on its id.
     * @param deploymentID
     * @return 
     */
    public static DeploymentInfo getDeployment(String deploymentID) {
    	DeploymentInfo deploymentInfo = deployments.get(deploymentID);
    	return deploymentInfo;
    }
    
    public static List<DeploymentInfo> searchDeployments(
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
	
	public static DeploymentInfo removeDeployment(String deploymentID) {
		return deployments.remove(deploymentID);
	}
}
