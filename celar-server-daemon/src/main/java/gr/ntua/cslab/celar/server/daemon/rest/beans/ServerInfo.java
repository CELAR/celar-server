package gr.ntua.cslab.celar.server.daemon.rest.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Server info bean, holding information about the server. Not crucial, will be
 * implemented later on.
 * @author Giannis Giannakopoulos
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerInfo {
    
    protected long uptime;
    
    public ServerInfo() {
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }    
}
