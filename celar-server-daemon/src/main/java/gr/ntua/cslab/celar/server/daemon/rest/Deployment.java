package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Ginnis Giannakopoulos
 */
@Path("/deployment/")
public class Deployment {
 
    @GET
    @Path("{id}/")
    public DeploymentInfo getDeployment(@PathParam("id") int id) {
        return new DeploymentInfo(id, new ApplicationInfo(), new Date().getTime()-10000l, new Date().getTime()+10000l, DeploymentStatus.ERROR);
    }
    
    @GET
    @Path("search/")
    public List<DeploymentInfo> searchDeployments(
            @DefaultValue("0") @QueryParam("start_time") long startTime,
            @DefaultValue("0") @QueryParam("end_time") long endTime,
            @DefaultValue("ERROR") @QueryParam("status") DeploymentStatus status,
            @DefaultValue("-1") @QueryParam("application_id") int applicationId) {
        List<DeploymentInfo> list = new LinkedList<>();
        list.add(new DeploymentInfo(1, new ApplicationInfo(), startTime, endTime, status));
        list.add(new DeploymentInfo(2, new ApplicationInfo(), startTime, endTime, status));
        list.add(new DeploymentInfo(3, new ApplicationInfo(), startTime, endTime, status));
        return list;
    }
}
