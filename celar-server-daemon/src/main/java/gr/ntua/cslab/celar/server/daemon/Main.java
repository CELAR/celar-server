package gr.ntua.cslab.celar.server.daemon;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Executor class, used as an endpoint to the jar package from the outside world.
 * @author Giannis Giannakopoulos
 */
public class Main {
    
    private static void loadProperties() throws IOException {
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("celar-server.properties");
        if(stream == null) {
            System.err.println("No celar-server.properties file was found! Exiting...");
            System.exit(1);
        }
        ServerStaticComponents.properties = new Properties();
        ServerStaticComponents.properties.load(stream);
    }
    
    private static void configureServer() throws Exception {
        ServletHolder holder = new ServletHolder(ServletContainer.class);
        holder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        holder.setInitParameter("com.sun.jersey.config.property.packages", 
                "gr.ntua.cslab.celar.server.daemon.rest;" +
                "org.codehaus.jackson.jaxrs");//Set the package where the services reside
        holder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        holder.setInitParameter("com.sun.jersey.config.feature.Formatted", "true");
        
        holder.setInitOrder(1);

        ServerStaticComponents.server = new Server(new Integer(ServerStaticComponents.properties.getProperty("server.port")));
        ServletContextHandler context = new ServletContextHandler(ServerStaticComponents.server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(holder, "/*");
    }
    
    private static void configureLogger() {
        
    }
    
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerStaticComponents.server.stop();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));

    }
    
    public static void main(String[] args) throws Exception {
        loadProperties();
        configureLogger();
        addShutdownHook();
        configureServer();
        
        ServerStaticComponents.server.start();

    }
}
