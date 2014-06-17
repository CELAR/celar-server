package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.rest.beans.ServerInfo;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/server/")
public class Server {
    
    
    @GET
    @Path("info/")
//    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public ServerInfo getServerInfo() throws IOException {
        ServerInfo serverInfo;
        serverInfo = new ServerInfo();
        serverInfo.setUptime(System.currentTimeMillis());
        return serverInfo;
    }    
}
