package gr.ntua.cslab.celar.server.daemon.rest;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/server/")
public class Server {
    
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public class ServerInfo {
        protected long uptime;
        public ServerInfo() {}
        public long getUptime() {return uptime;}
        public void setUptime(long uptime) {this.uptime = uptime;}
    }
    
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
