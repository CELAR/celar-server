package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.SlipStreamCredentials;
import gr.ntua.cslab.celar.server.daemon.rest.beans.user.AuthenticationRequest;
import gr.ntua.cslab.celar.server.daemon.rest.beans.user.AuthenticationResponse;
import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import org.eclipse.jetty.server.Response;


/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/user/")
public class User {
    
    @POST
    @Path("authenticate/")
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        return new AuthenticationResponse("Logged in", "OK", true);
    }
    
    @GET
    @Path("credentials/")
    public SlipStreamCredentials getSlipStreamCredentials(@QueryParam("user") String username) {
        String  user =ServerStaticComponents.properties.getProperty("slipstream.username"),
                pass =ServerStaticComponents.properties.getProperty("slipstream.password");
        if(user!=null && username.equals(user))
            return new SlipStreamCredentials(user, pass);
        else
            throw new WebApplicationException(Response.SC_FORBIDDEN);
    }
    
}
