package gr.ntua.cslab.database.parsers;


import fasolakia.Application;
import fasolakia.Component;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityTools.toJSONObject;
import fasolakia.Module;
import fasolakia.Resource;
import fasolakia.ResourceType;
import java.sql.Timestamp;
import java.util.List;
import static org.apache.log4j.Level.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import static gr.ntua.cslab.database.EntityGetters.*;

/**
 *
 * @author cmantas
 */
public class ApplicationParser {
    
    static final Logger LOG = Logger.getLogger(ApplicationParser.class) ;
    
    static{
        LOG.setLevel(DEBUG);
    }
    
    
//    public static Application parseApplicationDescription(JSONObject topJson, boolean store) throws DBException {
//        try {
//            JSONObject  appJson;
//            appJson = topJson.getJSONObject("application");
//            //check if the json specifies the user by name instead of USER_id
//            if (!appJson.has("USER_id")) {
//                appJson.put("USER_id", (User.getResourceTypeByName(appJson.getString("USER_name".toLowerCase()))).getId());
//            }
//            Application app = new Application(appJson);
//            if(store) app.store();
//            LOG.debug("loading description for app:" + app.getDescription());
//            JSONArray modules = appJson.getJSONArray("modules");
//            for (int i = 0; i < modules.length(); i++) {
//                JSONObject m = modules.getJSONObject(i);
//                m.put("APPLICATION_id", app.getId());
//                Module module = new Module(m);
//                if(store) module.store();
//                LOG.debug("parsed Module:" + module);
//                JSONArray components = m.getJSONArray("components");
//                for (int j = 0; j < components.length(); j++) {
//                    JSONObject c = components.getJSONObject(j);
//                    c.put("MODULE_id", ""+module.getId());
//                    
//                    //check if the json specifies the resource type name instead of the id
//                    if (!c.has("RESOURCE_TYPE_id")){ 
//                        ResourceType rt= ResourceType.getResourceTypeByName(c.getString("resource_type"));
//                        c.put("RESOURCE_TYPE_id", rt.getId());
//                    }
//                    Component component = new Component(c);
//                    if(store) component.store();
//                    LOG.debug("parsed component: " + component);
//                }//components
//            }//modules
//            return app;
//        } catch (JSONException ex) {
//            System.err.println("parsing not successfull");
//            ex.printStackTrace();
//        }
//        return null;
//    }
    
    public static JSONObject exportApplicationDescription(Application app) throws Exception{
        return exportApplication(app, new Timestamp(System.currentTimeMillis()), false);
    }
    
    public static JSONObject exportApplication(Application app) throws Exception{
        return exportApplication(app, new Timestamp(System.currentTimeMillis()), true);
    }

    public static JSONObject exportApplication(Application app, Timestamp ts, boolean includeResources) throws Exception {
        JSONObject result = new JSONObject();  //top level json object
        JSONObject applicationJson = toJSONObject(app);
        applicationJson.put("modules", exportApplicationModules(app, ts, includeResources));
        result.put("application", applicationJson);
        return result;
    }

    public static JSONArray exportApplicationModules(Application app, Timestamp ts, boolean includeResources) throws Exception {
        List<Module> modules =   getModulesByApplication(app);
        JSONArray modulesJson = new JSONArray(); //json array of modules
        LOG.debug("Modules for "+app.getDescription()+" :"+modules);
        //iterate modules and add to json array 
        for (Module m : modules) {
            JSONObject moduleJson = toJSONObject(m);
            moduleJson.put("components", exportModuleComponents(m, ts, includeResources));
            modulesJson.put(moduleJson);
        }
        return modulesJson;
    }
    
    
        public static JSONArray exportModuleComponents(Module m, Timestamp ts, boolean includeResources) throws Exception {
        List<Component> components = getComponentsByModule(m);
        LOG.debug("components for moduleid:" + m.getId() + " " + components);

        JSONArray componentsJson = new JSONArray();//json array of components
        //iterate components and add to json list
        for (Component c : components) {
            JSONObject componentJson = toJSONObject(c);
            //inject the resource type name as "resource type"
            componentJson.put("resource_type", (new ResourceType(c.getResourceTypeId())).getTypeName());
            if (includeResources) componentJson.put("resources", exportComponentResources(c, ts));
            componentsJson.put(componentJson);
        }
        return componentsJson;
    }
    
        public static JSONArray exportComponentResources(Component c, Timestamp ts) throws DBException{
           List<Resource> lr= getResourcesByComponent(c);
            JSONArray resourcesJson = new JSONArray();
            for(Resource r: lr) resourcesJson.put(toJSONObject(r));
//            resourcesJson.put(lr);
            return resourcesJson;
        }

}
