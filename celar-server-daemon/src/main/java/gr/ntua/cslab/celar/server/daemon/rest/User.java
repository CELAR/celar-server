package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.rest.beans.user.AuthenticationRequest;
import gr.ntua.cslab.celar.server.daemon.rest.beans.user.AuthenticationResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


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