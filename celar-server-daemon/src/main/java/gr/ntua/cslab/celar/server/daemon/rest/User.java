package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.rest.beans.user.AuthenticationRequest;
import gr.ntua.cslab.celar.server.daemon.rest.beans.user.AuthenticationResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
//import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/user/")
public class User {
    
    @POST
//    @Consumes(MediaType.APPLICATION_XML)
//    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Path("authenticate/")
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        return new AuthenticationResponse("Logged in", "OK", true);
    }
    
}