package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.rest.beans.balancer.BalancingStatus;
import gr.ntua.cslab.celar.server.daemon.rest.beans.balancer.BalancerInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.balancer.StartBalancer;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/balancer/")
public class DBalancer {
    
    @PUT
    @Path("start/")
    public void startBalancing(StartBalancer startBalancerObject) {
        // dummy call :)
    }
    
    @GET
    @Path("info/")
    public BalancerInfo getInfo() {
        return new BalancerInfo(0.8, 10);
    }
    
    @GET
    @Path("{id}/status/")
    public BalancingStatus getStatus(@PathParam("id") Integer id) {
        return new BalancingStatus(true, 1000l);
    }
}
