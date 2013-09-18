/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.sql.Timestamp;
import database.entities.JSONTools;
import database.Tables;
import database.entities.Application;
import database.entities.Component;
import database.entities.Deployment;
import database.entities.Metric;
import database.entities.MetricValue;
import database.entities.Module;
import database.entities.NotInDBaseException;
import database.entities.ProvidedResource;
import database.entities.ResizingAction;
import database.entities.Resource;
import database.entities.Spec;
import database.entities.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONObject;


/**
 *
 * @author cmantas
 */
public class EntitiesTester {
    
    public static User chris;
    public static User john;
    public static Application facebook;
    public static Module cassandra;
    public static Module fapache;
    public static Deployment dep;
    public static ProvidedResource tinyVM, bigVM;
    public static Spec spec;
    public static Component component;
    public static ResizingAction ra;
    public static Resource resource1, resource2;
    public static Metric cpuLoad;
    public static MetricValue metricValue;
    
    static boolean DEBUG=false;
    
    public static void createStructure(){
        try {
        int userId = Tables.usertable.insertUser("christaras");
        if(DEBUG) System.out.print("Inserted chris. exists: ");
        if(DEBUG) System.out.println(Tables.usertable.exists(userId));
        //create and retrieve some users	
        chris = new User(userId);
        if(DEBUG) System.out.println("The of user you inserted=" + chris);
        john = new User("John");
        john.store();
        if(DEBUG) System.out.println("John exists:" + Tables.usertable.exists(john.getId()));
        //create and retrieve an app
        facebook = new Application("social net",
                new Timestamp(System.currentTimeMillis()), john);
        facebook.store();
        facebook = new Application(facebook.getId());
        if(DEBUG) System.out.println("Your app: " + facebook);
        //create and retrieve a module
        cassandra = new Module("Cassandra store", facebook);
        cassandra.store();
        cassandra = new Module(cassandra.getId());
        if(DEBUG) System.out.println("A module: " + cassandra);
        fapache = new Module("facebook's appache ", facebook);
        fapache.store();
//create and retrieve a deployment (alternative retreive)
        dep = new Deployment(facebook,
                new Timestamp(System.currentTimeMillis()), null);
        dep.store();
        dep = Tables.deplTable.getDeployment(dep.getId());
        if(DEBUG) System.out.println("Deployment: " + dep);
        //create and retrieve a provided resource
        tinyVM = new ProvidedResource("VM", "tiny");
        tinyVM.store();
        bigVM = new ProvidedResource("VM", "big");
        bigVM.store();
        tinyVM = Tables.provResTable.getProvidedResource(tinyVM.getId());
        if(DEBUG) System.out.println("Prov. Resource: " + tinyVM);
        //create and retrieve a spec
        spec = new Spec(tinyVM, "dual core machine");
        spec.store();
        spec = Tables.specsTable.getSpec(spec.getId());
        if(DEBUG) System.out.println("Spec: " + spec);
        //create and save a component
        component = new Component(cassandra, tinyVM);
        component.store();
        component = Tables.componentTable.getComponent(component.getId());
        if(DEBUG) System.out.println("Component: " + component);
        //create, save and retreive a resizing action
        ra = new ResizingAction(cassandra, component, "add a vm");
        ra.store();
        ra = Tables.raTable.getResizingAction(ra.getId());
        if(DEBUG) System.out.println("Resizing action: " + ra);
        //create, save and retreive a resource
        resource1 = new Resource(dep, component, tinyVM,
                new Timestamp(System.currentTimeMillis()), null);
        
        resource1.store();
        resource2 = new Resource(dep, component, bigVM,
                new Timestamp(System.currentTimeMillis()), null);
        
        resource2.store();
        resource1 = Tables.resTable.getResource(resource1.getId());
        //create, save and retreive a metric
        cpuLoad = new Metric(component, new Timestamp(System.currentTimeMillis()));
        cpuLoad.store();
        cpuLoad = Tables.metricsTable.getMetric(cpuLoad.getId());
        if(DEBUG) System.out.println("Metric cpu load: " + cpuLoad);
        //create, save and retreive a metric Value
        metricValue = new MetricValue(cpuLoad, resource1,
                new Timestamp(System.currentTimeMillis()));
        metricValue.store();
        metricValue = Tables.mvTable.getMetricValue(metricValue.getId());
        if(DEBUG) System.out.println("Metric value: " + metricValue);
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
}
    

    


    public static void main(String args[]) throws NotInDBaseException, FileNotFoundException, IOException {

        //create a dummy structure of entities in the database
        createStructure();


        
        Application a=JSONTools.findDeploymentApp(dep.getId());
        
        //export the application configuration (application->modules->components->resources)
        JSONObject jsonResources= JSONTools.exportApplicationConfiguration(a, new Timestamp(System.currentTimeMillis()));
        
        if(DEBUG) System.out.println(jsonResources);
        //export the configuration to file
        JSONTools.exportToFile(jsonResources,"data_files/application_deployment_configuration");
        
        
        //export the user configuration (user->applications->deployment)
        JSONObject jsonUsers=JSONTools.exportAllUsers();
        JSONTools.exportToFile(jsonUsers, "data_files/user_configuration");
        String usersJsonString=jsonUsers.toString(2);
        System.out.println(usersJsonString);
        
        //export the provided resources
        JSONObject resoursesJ=JSONTools.exportProvidedResources();
        System.out.println("provided resources: \n"+resoursesJ.toString(2));
        JSONTools.exportToFile(resoursesJ, "data_files/provided_resources");
        

        jsonUsers=new JSONObject(usersJsonString);
        

       
        JSONTools.parseUsers(jsonUsers, true);
        JSONTools.parseProvidedResources(resoursesJ, true);
        JSONTools.parseApplicationConfiguration(jsonResources, true);
        
        
        
        Tables.clearDB();
        
        JSONObject users=JSONTools.loadJSONObjectFromFile("data_files/user_configuration");
        JSONObject providedResources=JSONTools.loadJSONObjectFromFile("data_files/provided_resources");
        JSONObject configuration=JSONTools.loadJSONObjectFromFile("data_files/application_deployment_configuration");
        
        
        JSONTools.parseUsers(users, true);
        JSONTools.parseProvidedResources(providedResources, true);
        JSONTools.parseApplicationConfiguration(configuration, true);
 

    }//main

   
    


}
