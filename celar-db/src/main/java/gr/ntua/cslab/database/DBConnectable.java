package gr.ntua.cslab.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author cmantas
 */
public abstract class DBConnectable {

    /**
     * The connection to the database
     */
    protected static Connection connection;

    /**
     * The logger for DB Operations
     */
    final static Logger LOG = Logger.getLogger(DBConnectable.class.getName());

    /**
     * the properties file location
     */
    final static String PROPERTIES_FILE = "/db_entities.properties";

    /**
     * the properties required for connection to a database
     */
    static String BACKEND, HOST, PORT, USER, PASSWORD, DB_NAME;

    /**
     * The static initializer loads the properties
     */
//    static {
//        //LOG.setLevel(DEBUG);
//        loadProperties();
//    }
    
    public static void loadProperties(Properties prop ){
        
            // get the property value 
            if(BACKEND==null) BACKEND = prop.getProperty("backend");
            if(HOST==null) HOST = prop.getProperty(BACKEND + ".host");
            if(PORT==null) PORT = prop.getProperty(BACKEND + ".port");
            if(USER==null) USER = prop.getProperty(BACKEND + ".username");
            if(PASSWORD==null) PASSWORD = prop.getProperty(BACKEND + ".password");
            if(DB_NAME==null) DB_NAME = prop.getProperty(BACKEND + ".db_name");
    }

   /**
    * Loads the properties from the properties file and uses default values if failed
    */
    public static void loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            InputStream in = DBConnectable.class.getResourceAsStream(PROPERTIES_FILE);
            // load a properties file
            prop.load(in);
            loadProperties(prop);

        } catch (IOException ex) {
            LOG.error("Could not load the properties for the DB Connection");
            BACKEND ="postgresql";
            HOST = "localhost";
            PORT = ""+5432;
            USER = "celaruser";
            PASSWORD = "celar-user";
            DB_NAME = "celardb";
        } catch (Exception e){
            e.printStackTrace();
        } 
        
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void openConnectionWithSetProps(){
        try {
            String DRIVER = "org." + BACKEND + ".Driver";
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String url = "jdbc:" + BACKEND + ":" + "//" + HOST + ":" + PORT + "/" + DB_NAME;
            connection = DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            LOG.fatal("Failed to create connection to DB server "+HOST+":"+PORT);
            LOG.fatal("REASON: " + e);
            e.printStackTrace();
        }
        LOG.debug("Connection created");
    }
    

    /**
     * Opens the connection to the database.
     *
     * <b>Note (by ggian):</b> SLOW procedure, use with care!
     */
    public static void openConnection() {
        loadProperties();
        openConnectionWithSetProps();
    }
    
    public static void openConnection(Properties props){
        loadProperties(props);
        openConnectionWithSetProps();
    }

    /**
     * Closes the connection with the database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            LOG.debug("Connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static String getHost(){
        return  HOST;
    }
    
    public static String getPort(){
        return PORT;
    }
    
}
