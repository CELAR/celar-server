package gr.ntua.cslab.celar.server.daemon.shared;

import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import java.util.Properties;
import org.eclipse.jetty.server.Server;

/**
 * This class hold static references to components that are initialized once and
 * they can be accessible from any point of the Server (calls, other modules, etc.).
 * @author Giannis Giannakopoulos
 */
public class ServerStaticComponents {
    /**
     * Properties loaded by the celar-server.properties file. Can be used
     * by any component.
     */
    public static Properties properties;
    
    
    /**
     * Server object, accessible by anyone. Used for debugging purposes 
     * (could be a security leak).
     */
    public static Server server;
    
    /**
     * SSService component 
     */
    public static SlipStreamSSService ssService;

}
