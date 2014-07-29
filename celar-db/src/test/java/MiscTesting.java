import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.Ignore;
 
@Ignore 
public class MiscTesting {
    
    //the prop file location
    final static String PROPERTIES_FILE="src/main/resources/db_credentials.properties";
    
    //the properties required for the DB connection
    static String BACKEND, HOST, PORT, USER, PASSWORD, DB_NAME;

    /**
     * Loads the properties about the database connection from the properties file
     */
    public static void loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(PROPERTIES_FILE);
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            BACKEND = prop.getProperty("backend");
            HOST = prop.getProperty(BACKEND+".host");
            PORT = prop.getProperty(BACKEND+".port");
            USER = prop.getProperty(BACKEND+".username");
            PASSWORD = prop.getProperty(BACKEND+".password");
            DB_NAME = prop.getProperty(BACKEND+".db_name");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        loadProperties();
        System.out.println(BACKEND);
        System.out.println(HOST);
        System.out.println(PORT);
        System.out.println(USER);
        System.out.println(PASSWORD);
        System.out.println(DB_NAME);
    }
}
