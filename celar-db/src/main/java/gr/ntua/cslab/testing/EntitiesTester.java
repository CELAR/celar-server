/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.testing;

import java.sql.Timestamp;
import gr.ntua.cslab.database.entities.JSONTools;
import gr.ntua.cslab.database.Tables;
import gr.ntua.cslab.database.entities.Application;
import gr.ntua.cslab.database.entities.Component;
import gr.ntua.cslab.database.entities.DBEntity;
import gr.ntua.cslab.database.entities.Dependency;
import gr.ntua.cslab.database.entities.Deployment;
import gr.ntua.cslab.database.entities.Metric;
import gr.ntua.cslab.database.entities.MetricValue;
import gr.ntua.cslab.database.entities.Module;
import gr.ntua.cslab.database.entities.NotInDBaseException;
import gr.ntua.cslab.database.entities.ProvidedResource;
import gr.ntua.cslab.database.entities.ResizingAction;
import gr.ntua.cslab.database.entities.Resource;
import gr.ntua.cslab.database.entities.Spec;
import gr.ntua.cslab.database.entities.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.json.JSONObject;


/**
 *
 * @author cmantas
 */
public class EntitiesTester {
    
    public static User chris;
    public static User john;
    public static Application test_app;
    public static Module cassandra;
    public static Module apache;
    public static Deployment dep;
    public static ProvidedResource tinyVM, bigVM;
    public static Spec spec;
    public static Component component;
    public static ResizingAction ra;
    public static Resource resource1, resource2;
    public static Metric cpuLoad;
    public static MetricValue metricValue;
    public static Dependency moduleDependency;
    
    static boolean DEBUG=false;
    
    public static void createStructure(){
        try {
        int userId = Tables.usertable.insertUser("christaras");
        if(DEBUG) System.out.print("Inserted chris. exists: ");
        if(DEBUG) System.out.println(Tables.usertable.exists(userId));
        //create and retrieve some users	
        chris = new User(userId);
        if(DEBUG) System.out.println("The of user you inserted=" + chris);
        john = new User("Cellar_User");
        john.store();
        if(DEBUG) System.out.println("John exists:" + Tables.usertable.exists(john.getId()));
        //create and retrieve an app
        test_app = new Application("test_application",
                new Timestamp(System.currentTimeMillis()), john);
        test_app.store();
        test_app = new Application(test_app.getId());
        if(DEBUG) System.out.println("Your app: " + test_app);
        //create and retrieve a module
        cassandra = new Module("Cassandra store", test_app);
        cassandra.store();
        cassandra = new Module(cassandra.getId());
        if(DEBUG) System.out.println("A module: " + cassandra);
        apache = new Module("Appache Webserver", test_app);
        apache.store();
        moduleDependency=new Dependency(apache,cassandra,"depends on storage",Dependency.MODULE);
        moduleDependency.store();
//create and retrieve a deployment (alternative retreive)
        dep = new Deployment(test_app,
                new Timestamp(System.currentTimeMillis()), null);
        dep.store();
            System.out.println("storing dep: "+dep);
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


  
        
        
        //Tables.clearDB();
        
       
 

    }//main

   
    


}
