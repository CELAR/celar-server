/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.testing;

import gr.ntua.cslab.database.entities.JSONTools;
import gr.ntua.cslab.database.Tables;
import gr.ntua.cslab.database.entities.Application;
import java.sql.Timestamp;
import org.json.JSONObject;
import static gr.ntua.cslab.testing.EntitiesTester.createStructure;
import static gr.ntua.cslab.database.entities.JSONTools.loadJSONObjectFromFile;
import java.util.List;


/**
 *
 * @author Christos Mantas <cmantas@cslab.ece.ntua.gr>
 */
public class JSONTester {
    
    static boolean DEBUG;
    static int deploymentID;
    
    static String appDeploymentConfigFile="src/main/resources/data_files/application_deployment_configuration.json";
    static String userConfigFile="src/main/resources/data_files/user_configuration.json";
    static String providedResourcesFile = "src/main/resources/data_files/provided_resources.json";
    
    
    
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
        JSONObject jsonUsers=loadJSONObjectFromFile(userConfigFile);
        JSONObject resoursesJ=loadJSONObjectFromFile(providedResourcesFile);
        JSONObject appConfig=loadJSONObjectFromFile(appDeploymentConfigFile);
        
        //parse the loaded JSON and store it
        JSONTools.parseUsers(jsonUsers, store);
        JSONTools.parseProvidedResources(resoursesJ, store);
        JSONTools.parseApplicationConfiguration(appConfig, store);
    }
    
    
    
    
    public static void main(String args[]){
        
        //create a dummy structure of entities in the database
       createStructure();
       
       //cheat and get the deployment id from the dummy deployment
        System.out.println("here");
       deploymentID=EntitiesTester.dep.getId();

//       //export the dummy structure to data files
//       exportToFiles();
//       
//       //clear the db  from the dummy structure
//       Tables.clearDB();
//
//       //load the stored configuration from json files and store it
//       loadFromFiles(true);
        
        
     
      System.out.println("metric:\n"+JSONTools.exportMetric(EntitiesTester.cpuLoad).toString(3));
       
       //clear the db : comment if you need the configuration to be persisted after running this file
       Tables.clearDB();
       
    }
}
