package gr.ntua.cslab.celar.server.daemon;

import java.io.InputStream;
import java.util.Properties;
import org.eclipse.jetty.server.Server;

/**
 * Executor class, used as an endpoint to the jar package from the outside world.
 * @author Giannis Giannakopoulos
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("celar-server.properties");
        if(stream == null) {
            System.err.println("Bad luck...");
            System.exit(1);
        }
        Properties props = new Properties();
        props.load(stream);
        
        System.out.println("Hello world");
        Server server  = new Server(new Integer(props.getProperty("server.port")));
        
        server.start();
    }
}
