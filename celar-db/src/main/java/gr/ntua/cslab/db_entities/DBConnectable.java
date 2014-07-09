/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
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
    static {
        //LOG.setLevel(DEBUG);
        loadProperties();
    }

   /**
    * Loads the properties from the properties file and uses default values if failed
    * <b>Note:</b> DEBUG: use with maven
    */
    public static void loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            InputStream in = DBConnectable.class.getResourceAsStream(PROPERTIES_FILE);
            // load a properties file
            prop.load(in);

            // get the property value 
            BACKEND = prop.getProperty("backend");
            HOST = prop.getProperty(BACKEND + ".host");
            PORT = prop.getProperty(BACKEND + ".port");
            USER = prop.getProperty(BACKEND + ".username");
            PASSWORD = prop.getProperty(BACKEND + ".password");
            DB_NAME = prop.getProperty(BACKEND + ".db_name");

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

    /**
     * Opens the connection to the database.
     *
     * <b>Note (by ggian):</b> SLOW procedure, use with care!
     */
    public static void openConnection() {
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
            LOG.fatal("Failed to create connection\n" + e);
            e.printStackTrace();
        }
        LOG.debug("Connection created");
    }

    /**
     * Closes the connection with the database.
     */
    public void closeConnection() {
        try {
            connection.close();
            LOG.debug("Connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
