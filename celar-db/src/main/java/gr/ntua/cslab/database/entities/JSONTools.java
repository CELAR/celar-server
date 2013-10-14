/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.Tables;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Christos Mantas <cmantas@cslab.ece.ntua.gr>
 */
public class JSONTools {

    public static JSONArray exportComponentResources(Component comp, Timestamp ts) {
        List<Resource> resources = //list of all resources in this component
                Tables.resTable.getComponentResources(comp.getId(), ts);
        System.out.println("resources for componentId:" + comp.getId() + " " + resources);
        JSONArray resoursesJson = new JSONArray();//json array of resourses
        //iterate through resourses
        for (Resource r : resources) {
            resoursesJson.put(r.toJSONObject());
        }
        return resoursesJson;
    }

    public static JSONArray exportModuleComponents(Module m, Timestamp ts) {
        List<Component> components = //a list of all components in this module
                Tables.componentTable.getModuleComponents(m.getId());
        System.out.println("components for moduleid:" + m.getId() + " " + components);

        JSONArray componentsJson = new JSONArray();//json array of components
        //iterate components and add to json list
        for (Component c : components) {
            JSONObject componentJson = c.toJSONObject();
            componentJson.put("resources", exportComponentResources(c, ts));
            componentsJson.put(componentJson);
        }
        return componentsJson;
    }

    public static JSONArray exportApplicationModules(Application app, Timestamp ts) {
        List<Module> modules = //a lit of all the modules for this app
                Tables.moduleTable.getAppModules(app.getId());
        System.out.println("modules for facebook:" + modules);
        JSONArray modulesJson = new JSONArray(); //json array of modules
        //iterate modules and add to json array   
        for (Module m : modules) {

            JSONObject moduleJson = m.toJSONObject();
            moduleJson.put("components", exportModuleComponents(m, ts));
            modulesJson.put(moduleJson);
        }
        return modulesJson;
    }

    public static JSONObject exportApplicationConfiguration(Application app, Timestamp ts) {
        JSONObject result = new JSONObject();  //top level json object
        JSONObject applicationJson = app.toJSONObject();
        applicationJson.put("modules", exportApplicationModules(app, ts));
        result.put("application", applicationJson);
        return result;
    }
    
        public static JSONArray exportApplicationDeployments(Application app) {
        JSONArray deployments = new JSONArray();
        List<Deployment> deps = Tables.deplTable.getApplicationDeployments(app.getId());
        for (Deployment d : deps) {
            deployments.put(d.toJSONObject());
        }
        return deployments;
    }

    public static JSONArray exportUserApps(int userId) {
        JSONArray result = new JSONArray();
        List<Application> apps = Tables.appTable.getUserApplications(userId);
        for (Application a : apps) {
            JSONObject appJson = a.toJSONObject();
            appJson.put("deployments", exportApplicationDeployments(a));
            result.put(appJson);
        }
        return result;
    }

    public static JSONObject exportUser(User user) {
        JSONObject userJson = user.toJSONObject();
        userJson.put("applications", exportUserApps(user.getId()));
        return userJson;
    }
    
    public static JSONObject exportAllUsers(){
        JSONObject result=new JSONObject();
        List<User> users=Tables.usertable.getAllUsers();
        JSONArray usersJ=new JSONArray();
        for(User u:users){
            usersJ.put(exportUser(u));                    
        }
        result.put("users", usersJ);
        return result;
    }
    
    public static JSONArray exportProvidedResourceSpecs(ProvidedResource pr){
        List<Spec> specs=Tables.specsTable.getProvidedResourceSpecs(pr);
        JSONArray specsJson=new JSONArray();
        for(Spec s:specs){
            specsJson.put(s.toJSONObject());
        }
        return specsJson;
    }
    
    
    public static JSONObject exportProvidedResource(ProvidedResource pr){
        JSONObject prj=pr.toJSONObject();
        prj.put("specs", exportProvidedResourceSpecs(pr));
        return prj;
    }
    

    
    public static JSONObject exportProvidedResources(){
        JSONObject result=new JSONObject();
        JSONArray prs=new JSONArray();
        List<ProvidedResource> resources=Tables.provResTable.getAllProvidedResources();
        for(ProvidedResource pr:resources){
            prs.put(exportProvidedResource(pr));
        }
        result.put("provided_resources", prs);
        return result;
    }
    

    
    public static JSONObject exportMetric(Metric m){
        JSONObject result=m.toJSONObject();        
        JSONArray mvsJ=new JSONArray();
        List<MetricValue> mvs=Tables.mvTable.getMetricValues(m.getId());
        for(MetricValue mv:mvs){
            mvsJ.put(mv.toJSONObject());
        }
        result.put("metric_values", mvsJ);
        return result;
    }


    public static Application findDeploymentApp(int deplId) {
        Deployment depl = new Deployment(deplId);
        Application app = new Application(depl.getApplicationId());
        return app;
    }

    public static boolean parseApplicationConfiguration(String jsonString, boolean store){
        try{
            return parseApplicationConfiguration(new JSONObject(jsonString),store);
        }catch(Exception e){
            System.err.println("received an unparsable JSON string");
            return false;
        }
    }
    public static boolean parseApplicationConfiguration(JSONObject topJson, boolean store) {
        try {
            JSONObject  appJson;
            appJson = topJson.getJSONObject("application");
            Application app = new Application(appJson);
            if(store)app.store();
            System.out.println("Constructed app:" + app);
            JSONArray modules = appJson.getJSONArray("modules");
            for (int i = 0; i < modules.length(); i++) {
                JSONObject m = modules.getJSONObject(i);
                Module module = new Module(m);
                if(store) module.store();
                System.out.println("parsed Module:" + module);
                JSONArray components = m.getJSONArray("components");
                for (int j = 0; j < components.length(); j++) {
                    JSONObject c = components.getJSONObject(j);
                    Component component = new Component(c);
                    if(store) component.store();
                    System.out.println("parsed component: " + component);
                    JSONArray resources = c.getJSONArray("resources");
                    for (int k = 0; k < resources.length(); k++) {
                        JSONObject r = resources.getJSONObject(k);
                        Resource resource = new Resource(r);
                        if(store) resource.store();
                        System.out.println("parsed resource: " + resource);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            System.err.println("parsing not successfull");
            ex.printStackTrace();
        }
        return false;
    }
    
    
    
    public static void parseAppDeployments(JSONObject appJson, boolean store) {
        JSONArray deps = appJson.getJSONArray("deployments");
        for (int i = 0; i < deps.length(); i++) {
            JSONObject d = deps.getJSONObject(i);
            Deployment dep=new Deployment(d);
            if(store) dep.store();
            System.out.println("parsed deployment:"+dep);
        }
    }
    
    public static void parseUserApps(JSONObject userJson, boolean store){
        if(!userJson.has("applications")) return;
            JSONArray applications=userJson.getJSONArray("applications");
            for (int i = 0; i < applications.length(); i++) {
                JSONObject appJson = applications.getJSONObject(i);
                Application app=new Application(appJson);
                if(store)app.store();
                System.out.println("parsed application:"+app);
                parseAppDeployments(appJson, store);
            }

    }

    public static void parseUsers(JSONObject jsonUsers, boolean store) {
        JSONArray usersJ=jsonUsers.getJSONArray("users");
        for(int i=0; i<usersJ.length(); i++){
            JSONObject userJ=usersJ.getJSONObject(i);
            User user=new User(userJ);
            if(store) user.store();
            System.out.println("Parsed user:"+ user);
            parseUserApps(userJ, store);
        }
    }

    public static void parseProvidedResources(JSONObject prj, boolean store) {
        JSONArray resourcesJ=prj.getJSONArray("provided_resources");
        for (int i = 0; i < resourcesJ.length(); i++) {
            JSONObject rj = resourcesJ.getJSONObject(i);
            ProvidedResource pr=new ProvidedResource(rj);
            if(store) pr.store();
            System.out.println("Parsed prov resource:"+pr);
            parseResourceSpecs(rj,store);
            
        }
    }

    private static void parseResourceSpecs(JSONObject rj, boolean store) {
        JSONArray specsJ=rj.getJSONArray("specs");
        for(int i=0; i<specsJ.length();i++){
            JSONObject sj=specsJ.getJSONObject(i);
            Spec s=new Spec(sj);
            if(store) s.store();
            System.out.println("parsed spec:"+s);
        }
    }
   
  
   
   public static Metric parseMetric(JSONObject mj, boolean store, PrintWriter out){
       try{
       Metric m=new Metric(mj);
       boolean success=false;
       if(store)success=m.store();
       if(!success)return m;
       System.out.println("parsed metric : "+m);
       JSONArray mvsJ= mj.getJSONArray("metric_values");
       for (int i = 0; i < mvsJ.length(); i++) {
           JSONObject mvj = mvsJ.getJSONObject(i);
           mvj.put("METRICS_id", m.getId());
           MetricValue mv= new MetricValue(mvj);
           if(store)success=mv.store();
           if(!success)return m;
           System.out.println("parsed metric value: "+mv);
       }
       return m;
       }catch(Exception e){
           out.println("ERROR parsing metric:"+e.getMessage());
           return null;
       }
   }

    public static void exportToFile(JSONObject jo, String filename){
        try {
            FileWriter fw=new FileWriter(filename);
            fw.write(jo.toString(4));
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static JSONObject loadJSONObjectFromFile(String filename){
        try {
            byte[] contents = Files.readAllBytes(Paths.get(filename));
            String jsonString= new String(contents);
            return new JSONObject(jsonString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static JSONObject parseJson(String jsonStr, PrintWriter output) {
        if(jsonStr==null){
            output.println("ERROR: no input for requred parameter");
            return null;
        }
        try {
            JSONObject jo = new JSONObject(jsonStr);
            return jo;
        } catch (JSONException malformed) {
            output.println("ERROR: Malformed JSON input: " + malformed.getMessage());
        }
        return null;
    }


}