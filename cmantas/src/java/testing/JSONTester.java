/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import database.entities.JSONTools;
import database.Tables;
import database.entities.Application;
import java.sql.Timestamp;
import org.json.JSONObject;
import static testing.EntitiesTester.createStructure;


/**
 *
 * @author Christos Mantas <cmantas@cslab.ece.ntua.gr>
 */
public class JSONTester {
    
    static boolean DEBUG;
    static int deploymentID;
    
    static String appDeploymentConfigFile="data_files/application_deployment_configuration.json";
    static String userConfigFile="data_files/user_configuration.json";
    static String providedResourcesFile = "data_files/provided_resources.json";
    
    
    
    public static void exportToFiles(){
         //find the application that this deploymentID belongs to 
        Application a=JSONTools.findDeploymentApp(deploymentID);
        
        //export the application configuration to JSON (application->modules->components->resources)
        JSONObject jsonResources= JSONTools.exportApplicationConfiguration(a, new Timestamp(System.currentTimeMillis())); 
        if(DEBUG) System.out.println(jsonResources);
        JSONTools.exportToFile(jsonResources,appDeploymentConfigFile);
        
        
        //export the user configuration (user->applications->deployment)
        JSONObject jsonUsers=JSONTools.exportAllUsers();
        JSONTools.exportToFile(jsonUsers, userConfigFile);
        String usersJsonString=jsonUsers.toString(2);
        System.out.println(usersJsonString);
        
        //export the provided resources
        JSONObject resoursesJ=JSONTools.exportProvidedResources();
        System.out.println("provided resources: \n"+resoursesJ.toString(2));
        JSONTools.exportToFile(resoursesJ,providedResourcesFile );
    }
    
    
    public static void loadFromFiles(boolean store){      
        //load from JSON files
        JSONObject jsonUsers=JSONTools.loadJSONObjectFromFile(userConfigFile);
        JSONObject resoursesJ=JSONTools.loadJSONObjectFromFile(providedResourcesFile);
        JSONObject appConfig=JSONTools.loadJSONObjectFromFile(appDeploymentConfigFile);
        
        //parse the loaded JSON and store it
        JSONTools.parseUsers(jsonUsers, store);
        JSONTools.parseProvidedResources(resoursesJ, store);
        JSONTools.parseApplicationConfiguration(appConfig, store);
    }
    
    
    
    
    public static void main(String args[]){
        
        //create a dummy structure of entities in the database
       createStructure();
       
       //cheat and get the deployment id from the dummy deployment
       deploymentID=testing.EntitiesTester.dep.getId();

       //export the dummy structure to data files
       exportToFiles();
       
       //clear the db  from the dummy structure
       Tables.clearDB();

       //load the stored configuration from json files and store it
       loadFromFiles(true);
        
        //clear the db : comment if you need the configuration to be persisted after running this file
        //Tables.clearDB();
    }
}
