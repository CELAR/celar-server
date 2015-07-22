package gr.ntua.cslab.celar.server.daemon.rest;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
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
        if(request.getUuid().equals("b7dab7d1-24d6-42f1-8bb0-b365e8abaf9e")) {
            return new AuthenticationResponse("4c021c79-6862-4337-bc24-923320e41354", 
                    "OK", 
                    true);
        } else  {
            ResponseBuilderImpl responseBuilder =  new ResponseBuilderImpl();
            responseBuilder.status(javax.ws.rs.core.Response.Status.FORBIDDEN);
            responseBuilder.entity("The received UUID is not allowed!");
            throw new WebApplicationException(responseBuilder.build());
        }
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
