package gr.ntua.cslab.celar.server.daemon.rest;

import com.sixsq.slipstream.statemachine.States;
import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Decision;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.ResizingAction;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import static gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents.ssService;
import static gr.ntua.cslab.database.EntityGetters.getApplicationById;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentById;
import static gr.ntua.cslab.database.EntityGetters.searchDecisions2;
import static gr.ntua.cslab.database.EntityGetters.getResizingActions;
import static gr.ntua.cslab.database.EntityTools.removeDeployment;
import static gr.ntua.cslab.database.EntityTools.store;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        Deployment dep =  getDeploymentById(deploymentID);
        States state = null;
        if(ssService!=null){
            state = ssService.getDeploymentState(deploymentID);
            //TODO manage Orchestrator and VMs IPs?
            HashMap<String, String> ips = ServerStaticComponents.ssService.getDeploymentIPs(deploymentID);
            //dep.setDescription(ips.toString());
        }
        dep.setState(state);

        return dep;
    }
	

    
    @GET
    @Path("search/")
    public List<Deployment> searchDeployments(
            @DefaultValue("0") @QueryParam("start_time") long startTime,
            @DefaultValue("0") @QueryParam("end_time") long endTime,
            @DefaultValue("-1") @QueryParam("application_id") int applicationId) {
    	logger.info("Searchin deployment");
        throw new UnsupportedOperationException("not implemented");
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
            @DefaultValue("-1")  @QueryParam("component_id") int componentId,
            @DefaultValue("")    @QueryParam("action_name") String actionName
            ) throws Exception {
        
        logger.info("Search decisions for deployment: "+deploymentID);
        Deployment dep =  getDeploymentById(deploymentID);
        //check deployment exists
        if(dep.id.equals("")) throw new Exception("Deployment not found");
        
        REList<Decision> rv = new REList();
        List<Decision> decisions = searchDecisions2(dep, startTime, endTime, 
                actionName, componentId, moduleId);
        rv.values.addAll(decisions);        
        return rv;
    }
    
    @GET
    @Path("{id}/component/{component_id}/{type}")
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
    
	
}
