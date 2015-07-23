package gr.ntua.cslab.celar.server.daemon.rest;

import com.sixsq.slipstream.statemachine.States;
import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Decision;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.DeploymentState;
import gr.ntua.cslab.celar.server.beans.ResizingAction;
import gr.ntua.cslab.celar.server.beans.Resource;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import static gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents.ssService;
import static gr.ntua.cslab.database.EntityGetters.getApplicationById;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentById;
import static gr.ntua.cslab.database.EntityGetters.getDeployments;
import static gr.ntua.cslab.database.EntitySearchers.searchDecisions;
import static gr.ntua.cslab.database.EntityGetters.getResizingActions;
import static gr.ntua.cslab.database.EntityGetters.getResources;
import gr.ntua.cslab.database.EntitySearchers;
import static gr.ntua.cslab.database.EntityTools.removeDeployment;
import static gr.ntua.cslab.database.EntityTools.store;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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
    public static DeploymentState getDeployment(@PathParam("id") String deploymentID) throws Exception {
        logger.info("Get deployment: "+deploymentID);
        Deployment dep =  getDeploymentById(deploymentID);
        try {
            States state = null;
            Map<String,String> map =null;
            if (ssService != null) {
                map = ssService.getAllRuntimeParams(deploymentID);
                
//                state = ssService.getDeploymentState(deploymentID);
                //TODO manage Orchestrator and VMs IPs?
//                HashMap<String, String> ips = ServerStaticComponents.ssService.getDeploymentIPs(deploymentID);
                //dep.setDescription(ips.toString());
            }
            dep.setState(map.get("state"));
            DeploymentState depState = new DeploymentState(map, dep);
            return depState;
        }
        catch (Exception e){
            logger.error("Slipstream get state failed");
            e.printStackTrace();
        }

        return null;
    }
	

    
    @GET
    @Path("search/")
    public static REList<Deployment> searchDeployments(
            @DefaultValue("-1") @QueryParam("start_time") long startTime,
            @DefaultValue("-1") @QueryParam("end_time") long endTime,
            @DefaultValue("-1") @QueryParam("application_id") String applicationId,
            @DefaultValue("-1") @QueryParam("state") String state
            ) throws Exception {
    	logger.info("Searching deployment for start>="+startTime+" end<="+ endTime+" appId="+applicationId);
        Timestamp stt=null, ett=null;
        Application app=null;
        if(startTime!=-1) stt=new Timestamp(startTime);
        if(endTime!=-1) ett=new Timestamp(endTime);
        if(!(applicationId==null || applicationId.equals("-1"))) app = getApplicationById(applicationId);
        REList<Deployment> deployments= new REList(getDeployments(stt, ett, app));
        
        for (Object o : deployments) {
            Deployment dep = (Deployment) o;
            try {
                if (ssService != null) {
                    States s = ssService.getDeploymentState(dep.id);
                    logger.info("Deployment("+dep.id+") state: "+s);
                    HashMap<String, String> ips = ServerStaticComponents.ssService.getDeploymentIPs(dep.id);
                    //dep.setDescription(ips.toString());
                    for(String host: ips.keySet())
                        if(host.toLowerCase().contains("orchestrator")){
                            dep.setOrchestrator_IP(ips.get(host));
                            break;
                        }
                    dep.setState(s);
                }
                
            } catch (Exception e) {
                logger.error("Slipstream get state failed for Deployement("+dep.id+")");
                e.printStackTrace();
            }

        }
        
        if (state.equals("-1")) return deployments;
        
        logger.info("Filtering deployments for state: "+state);
        //filter states
        REList<Deployment> rv= new REList();
        for(Object d: deployments){
            String depState = ((Deployment)d).getState();
            if(depState!=null && depState.toLowerCase().equals(state.toLowerCase()))
                rv.add(d);
        }       
         return rv;
        
    }

    @GET
    @Path("{id}/terminate/")
    public static String terminateDeployment(@PathParam("id") String deploymentID) throws Exception {
        logger.info("Terminating deployment: "+deploymentID);
        Deployment dep = getDeploymentById(deploymentID);
    	if (ssService!=null){
            ssService.terminateApplication(deploymentID);
        }
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
    
        
    

    @GET
    @Path("{id}/decisions")
    public static REList<Decision> getDecisions(
            @PathParam("id")                                String deploymentID,            
            @DefaultValue("-1")  @QueryParam("start_time")   long startTime,
            @DefaultValue("-1")  @QueryParam("end_time")     long endTime,
            @DefaultValue("-1")  @QueryParam("module_id")    int moduleId,
            @QueryParam("component_id") int componentId,
            @DefaultValue("")    @QueryParam("action_name") String actionName
            ) throws Exception {
        
        logger.info("Search decisions for deployment: "+deploymentID);
        Deployment dep =  getDeploymentById(deploymentID);
        //check deployment exists
        if(dep.id.equals("")) throw new Exception("Deployment not found");
        
        REList<Decision> rv = new REList();
        List<Decision> decisions = EntitySearchers.searchDecisions(dep, startTime, endTime, 
                actionName, componentId, moduleId);
        rv.values.addAll(decisions);        
        return rv;
    }
    
    @GET
    @Path("{id}/component/{component_id}/decide/{type}")
    public static Decision addDecision(
            @PathParam("id") String deploymentID,
            @PathParam("component_id") int componentID,
            @PathParam("type") String decisionType,
            @DefaultValue("1")  @QueryParam("count") int count) throws Exception{
            Deployment dep =  getDeploymentById(deploymentID);
            Component comp = new Component(componentID);
            ResizingAction ra = getResizingActions(decisionType, comp).get(0);
            Decision des = new Decision(ra, dep, count);
           /* TODO: do implement the decision */
            store(des);
            return des;
    }
    
    @GET
    @Path("{id}/resources")
    public static REList<Resource> getDeploymentResources(
           @PathParam("id")  String deploymentID, 
           @DefaultValue("-1") @QueryParam("component_id") int componentId) throws Exception{
        Deployment dep = getDeploymentById(deploymentID);
        Component c = null;
        if(componentId!=-1) c = new Component(componentId);
        
        return new REList<>(getResources(c, dep));
        
    }
	
}
