/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entities;

import java.sql.Timestamp;
import java.util.List;
import database.Tables;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Tester {
    
    public static User chris;
    public static User john;
    public static Application facebook;
    public static Module cassandra;
    public static Module fapache;
    public static Deployment dep;
    public static ProvidedResource provRes;
    public static Spec spec;
    public static Component component;
    public static ResizingAction ra;
    public static Resource resource;
    public static Metric cpuLoad;
    public static MetricValue metricValue;
    
    
    public static void createStructure(){
        try {
        int userId = Tables.usertable.insertUser("christaras");
        System.out.print("Inserted chris. exists: ");
        System.out.println(Tables.usertable.exists(userId));
        //create and retrieve some users	
        chris = new User(userId);
        System.out.println("The of user you inserted=" + chris);
        john = new User("John");
        john.store();
        System.out.println("John exists:" + Tables.usertable.exists(john.id));
        //create and retrieve an app
        facebook = new Application("social net",
                new Timestamp(System.currentTimeMillis()), john);
        facebook.store();
        facebook = new Application(facebook.id);
        System.out.println("Your app: " + facebook);
        //create and retrieve a module
        cassandra = new Module("Cassandra store", facebook);
        cassandra.store();
        cassandra = new Module(cassandra.id);
        System.out.println("A module: " + cassandra);
        fapache = new Module("facebook's appache ", facebook);
        fapache.store();
//create and retrieve a deployment (alternative retreive)
        dep = new Deployment(facebook,
                new Timestamp(System.currentTimeMillis()), null);
        dep.store();
        dep = Tables.deplTable.getDeployment(dep.id);
        System.out.println("Deployment: " + dep);
        //create and retrieve a provided resource
        provRes = new ProvidedResource("VM", "tiny");
        provRes.store();
        provRes = Tables.provResTable.getProvidedResource(provRes.id);
        System.out.println("Prov. Resource: " + provRes);
        //create and retrieve a spec
        spec = new Spec(provRes, "dual core machine");
        spec.store();
        spec = Tables.specsTable.getSpec(spec.id);
        System.out.println("Spec: " + spec);
        //create and save a component
        component = new Component(cassandra, provRes);
        component.store();
        component = Tables.componentTable.getComponent(component.id);
        System.out.println("Component: " + component);
        //create, save and retreive a resizing action
        ra = new ResizingAction(cassandra, component, "add a vm");
        ra.store();
        ra = Tables.raTable.getResizingAction(ra.id);
        System.out.println("Resizing action: " + ra);
        //create, save and retreive a resource
        resource = new Resource(dep, component, provRes,
                new Timestamp(System.currentTimeMillis()), null);
        resource.store();
        resource = Tables.resTable.getResource(resource.id);
        System.out.println("Resource: " + resource);
        //create, save and retreive a metric
        cpuLoad = new Metric(component, new Timestamp(System.currentTimeMillis()));
        cpuLoad.store();
        cpuLoad = Tables.metricsTable.getMetric(cpuLoad.id);
        System.out.println("Metric cpu load: " + cpuLoad);
        //create, save and retreive a metric Value
        metricValue = new MetricValue(cpuLoad, resource,
                new Timestamp(System.currentTimeMillis()));
        metricValue.store();
        metricValue = Tables.mvTable.getMetricValue(metricValue.id);
        System.out.println("Metric value: " + metricValue);
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
    
}
    
    public static void destroyStructure(){
        //Delete the entries created
        metricValue.delete();
        cpuLoad.delete();
        resource.delete();
        ra.delete();
        component.delete();
        spec.delete();
        provRes.delete();
        dep.delete();
        cassandra.delete();
        fapache.delete();
        facebook.delete();
        Tables.usertable.delete(john.id);
        john.delete();
        chris.delete();
        System.out.println("Deleted john. Exists:" + Tables.usertable.exists(john.id));
        Tables.closeConnections();
    }
    
    

    public static void main(String args[]) throws NotInDBaseException {

        createStructure();


        //after having created all the structure crawl it
        crawlApplicationModules(facebook.id);
        
        destroyStructure();

 

    }//main

    static void crawlApplicationModules(int AppId) {
        JSONObject result=new JSONObject();
        
        Application app=new Application(AppId);
        
        
        List<Module> modules = Tables.moduleTable.getAppModules(AppId);
        System.out.println("modules for facebook:" + modules);
        
        JSONArray modulesListJson = new JSONArray();
        //iterate modules and add to json list
        
        for (Module m : modules) {
            List<Component> components = Tables.componentTable.getModuleComponents(m.id);
            System.out.println("components for moduleid:" + m.id + " " + components);
            
            JSONArray componentsListJson = new JSONArray();
            //iterate components and add to json list
            for (Component c : components) {
                System.out.println("resource for componentId:" + c.id + " " + new ProvidedResource(c.providedResourceId));
                componentsListJson.put(c.toJSONObject());
            }
            JSONObject moduleJson=m.toJSONObject();
            moduleJson.put("components", componentsListJson);
            modulesListJson.put(moduleJson);
        }
        
        JSONObject applicationJson=app.toJSONObject();
        applicationJson.put("modules", modulesListJson);
        
        result.put("application", applicationJson);
        System.out.println(result.toString(2));
    }

}
