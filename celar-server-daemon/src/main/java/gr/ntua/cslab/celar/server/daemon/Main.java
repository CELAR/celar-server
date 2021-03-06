package gr.ntua.cslab.celar.server.daemon;

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import static gr.ntua.cslab.database.DBTools.openConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;


/**
 * Executor class, used as an endpoint to the jar package from the outside
 * world.
 *
 * @author Giannis Giannakopoulos
 */
public class Main {

    private static void loadProperties() throws IOException {
        InputStream stream = Main.class.getClassLoader().getResourceAsStream("celar-server.properties");
        if (stream == null) {
            System.err.println("No celar-server.properties file was found! Exiting...");
            System.exit(1);
        }
        ServerStaticComponents.properties = new Properties();
        ServerStaticComponents.properties.load(stream);
    }

    private static void configureServer() throws Exception {
        ServerStaticComponents.server = new Server();
        int plainPort = -1, sslPort = -1;
        String keystorePath = null, keystorePassword = null;
        ServerConnector connector = null, sslConnector = null;

        if (ServerStaticComponents.properties.getProperty("server.plain.port") != null) {
            plainPort = new Integer(ServerStaticComponents.properties.getProperty("server.plain.port"));
        }
        if (ServerStaticComponents.properties.getProperty("server.ssl.port") != null) {
            sslPort = new Integer(ServerStaticComponents.properties.getProperty("server.ssl.port"));
            keystorePath = ServerStaticComponents.properties.getProperty("server.ssl.keystore.path");
            keystorePassword = ServerStaticComponents.properties.getProperty("server.ssl.keystore.password");
        }

        if (plainPort != -1) {
            connector = new ServerConnector(ServerStaticComponents.server);
            connector.setPort(plainPort);
        }

        if (sslPort != -1) {
            HttpConfiguration https = new HttpConfiguration();
            https.addCustomizer(new SecureRequestCustomizer());
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePath(keystorePath);
            sslContextFactory.setKeyStorePassword(keystorePassword);
            sslContextFactory.setKeyManagerPassword(keystorePassword);

            sslConnector = new ServerConnector(
                    ServerStaticComponents.server,
                    new SslConnectionFactory(sslContextFactory, "http/1.1"),
                    new HttpConnectionFactory(https));
            sslConnector.setPort(sslPort);

        }
        if (sslConnector != null && connector != null) {
            ServerStaticComponents.server.setConnectors(new Connector[]{connector, sslConnector});
        } else if (connector != null) {
            ServerStaticComponents.server.setConnectors(new Connector[]{connector});
        } else if (sslConnector != null) {
            ServerStaticComponents.server.setConnectors(new Connector[]{sslConnector});
        } else {
            System.err.println("Please choose one of the plain and SSL connections!");
            System.exit(1);
        }

        ServletHolder holder = new ServletHolder(ServletContainer.class);
        holder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        holder.setInitParameter("com.sun.jersey.config.property.packages",
                "gr.ntua.cslab.celar.server.daemon.rest;"
                + "org.codehaus.jackson.jaxrs");//Set the package where the services reside
        holder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        holder.setInitParameter("com.sun.jersey.config.feature.Formatted", "true");
//        
        holder.setInitOrder(1);
//
//        ServerStaticComponents.server = new Server();
        ServletContextHandler context = new ServletContextHandler(ServerStaticComponents.server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(holder, "/*");
        Logger.getLogger(Main.class.getName()).info("Server configured");

    }

    private static void configureLogger() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        InputStream logPropertiesStream = Main.class.getClassLoader().getResourceAsStream("log4j.properties");
        PropertyConfigurator.configure(logPropertiesStream);
        Logger.getLogger(Main.class.getName()).info("Logger configured");
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Logger.getLogger(Main.class.getName()).info("Server is shutting down");
                    ServerStaticComponents.server.stop();
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));

    }

    private static void creatDirs() {
        File csarDir = new File("/tmp/csar/");

        if (!csarDir.exists()) {
            csarDir.mkdir();
        }
    }

    public static void main(String[] args) throws Exception {
        configureLogger();
        loadProperties();
        creatDirs();
        addShutdownHook();
        configureServer();
        configureSlipstreamClient();
        openConnection(ServerStaticComponents.properties);

        ServerStaticComponents.server.start();
        Logger.getLogger(Main.class.getName()).info("Server is started");

    }

    private static void configureSlipstreamClient() throws IOException, ValidationException {

//        InputStream propsInput = Applications.class.getClassLoader().getResourceAsStream("slipstream.properties");
//        Properties properties = new Properties();
//        if (propsInput != null) {
//            properties.load(propsInput);
//        } else {
//            throw new WebServiceException("Wrong server configuration; SlipStream not accessible");
//        }
//        Logger.getLogger(Main.class.getName()).info(properties.toString());
        String username = ServerStaticComponents.properties.getProperty("slipstream.username"),
                password = ServerStaticComponents.properties.getProperty("slipstream.password"),
                slipstreamHost = ServerStaticComponents.properties.getProperty("slipstream.url"),
        		connectorName = ServerStaticComponents.properties.getProperty("slipstream.connector.name");
        Logger.getLogger(Main.class.getName()).info("Init ssService user: "+username+" password: "+password+" url: "+slipstreamHost+" connector: "+connectorName);

        ServerStaticComponents.ssService = new SlipStreamSSService(username, password, slipstreamHost, connectorName);
    }
}
