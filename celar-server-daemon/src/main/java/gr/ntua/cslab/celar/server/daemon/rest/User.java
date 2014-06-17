package gr.ntua.cslab.celar.server.daemon.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/user/")
public class User {
    
    @POST
//    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Path("authenticate/")
    public void authenticateUser() {
        throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).build());
    }
    
}