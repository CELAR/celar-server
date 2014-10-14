package gr.ntua.cslab.database.parsers;

import fasolakia.Application;
import fasolakia.Component;
import fasolakia.ComponentDependency;
import fasolakia.Deployment;
import fasolakia.Module;
import fasolakia.ModuleDependency;
import fasolakia.ProvidedResource;
import fasolakia.Resource;
import fasolakia.ResourceType;
import fasolakia.User;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getProvidedResourceByFlavorInfo;
import static gr.ntua.cslab.database.EntityGetters.getResourceTypeByName;
import static gr.ntua.cslab.database.EntityGetters.getUserByName;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import tools.CSARParser;
import tools.Parser;
/**
 *
 * @author cmantas
 */
public class ToscaHandler {
    public  Logger logger = Logger.getLogger(CSARParser.class);
    private Parser parser;
    User user;
    Application app;
    List<Module> modules=new LinkedList();
    List<ModuleDependency> moduleDependencies = new LinkedList();
    List<ComponentDependency> componentDependencies = new LinkedList();
    Map<Module, List<Component>> moduleComponents= new java.util.HashMap<>(5);
    List<Resource> resources = new java.util.LinkedList<>();
    Deployment deployment;
    
    
    public ToscaHandler(String csarFilePath) throws Exception{
       //create the parser object
        parser = new CSARParser(csarFilePath);
    }
    
    public Module getModuleByName(String moduleName){
        for(Module m: modules) if (m.getName().equals(moduleName)) return m;
        return null;
    }
    
    public Component getComponentByName(String componentName, List<Component> l){
        for(Component m: l) if (m.getDescription().equals(componentName)) return m;
        return null;
    }
    
    public void removeDescription() throws DBException {
        //delete component dependencies
        for(ComponentDependency cd: componentDependencies){
            delete(cd);
        }
        //delete module dependencies
        for(ModuleDependency md: moduleDependencies){
            delete(md);
        }
        //delete components and modules
        for (Module m : modules) {
            for (Component c : moduleComponents.get(m)) {
                delete(c);
            }
           delete(m);
        }        
        delete(app);
        
        logger.info("Removed the Application Description for "+app.getDescription());
    }
    
    public void removeDeployment() throws DBException{
        for (Resource p: resources){
            delete(p);
        }
        delete(deployment);
        logger.info("Removed the Application Deployment for "+app.getDescription());
    }
    
    
    private void handleUser() throws DBException{
         user = getUserByName("celar-user");
        if (user==null){
            user = new User("celar-user");
            store(user);
        }
    }
    
    private void handleApplication()throws DBException{
        String name = parser.getAppName();
        String v = parser.getAppVersion();
        int i= v.indexOf(".");
        int mjv = Integer.parseInt(v.substring(0, i));
        int mnv = Integer.parseInt(v.substring(i+1, v.length()));
        app = new Application(mjv, mnv, name, user);
        store(app);
    }
    
    private Module handleModule(String name) throws DBException {
        //create module
        Module module = new Module(name, app);
        store(module);
        modules.add(module);
        moduleComponents.put(module, new java.util.LinkedList());
        return module;
    }
    
    private void handleModuleDependencies(Module m) throws DBException{
        for(String depName:parser.getModuleDependencies(m.getName())){
                Module dep = getModuleByName(depName);
                ModuleDependency md = new ModuleDependency(m, dep);
                moduleDependencies.add(md);
                store(md);
            }

    }

    private Component handleComponent(Module m, String componentName, String resourceTypeName) throws DBException {
        ResourceType rt = getResourceTypeByName(resourceTypeName);
        Component c = new Component(m, componentName, rt);
        moduleComponents.get(m).add(c);
        store(c);
        return c;
    }

    private void handleComponentDependencies(Module m, Component c) throws DBException {
        for(String depName:parser.getComponentDependencies(c.getDescription())){
                Component dep = getComponentByName(depName, moduleComponents.get(m));
                ComponentDependency cd = new ComponentDependency(c, dep);
                componentDependencies.add(cd);
                store(cd);
            }
    }

    
    public void storeDescription() throws Exception {
        
        //get the user or create him
         handleUser();

        //create the application
        handleApplication();
        
        //iterate through modules and create them and their components
        for (String moduleName:  parser.getModules()) {            
            //handle each module
            Module m = handleModule(moduleName);
            //iterate through components
            for (String componentName : parser.getModuleComponents(moduleName)) {              
                //handle each component
                Component c= handleComponent(m, componentName, "VM_IMAGE");
                
            }
        }
        
        //iterate through modules and create their dependencies and the dependencies of their components
        for (Module m : modules) {            
            //module dependecies
            handleModuleDependencies(m);
            //iterate through components
            for (Component c: moduleComponents.get(m)) {                
                //component dependencies
                handleComponentDependencies(m, c);
            }
        }
        
        logger.info("Stored the Application Description for "+app.getDescription());
    }

    public static void main(String args[]) throws Exception {
        ToscaHandler tp = new ToscaHandler("app_7.csar");
        
        tp.storeDescription();
        tp.removeDescription();
    }



    public void storeDeployment() throws Exception {
        
        deployment = new Deployment(app);
        store(deployment);
        
        for (Entry<Module, List<Component>> e : moduleComponents.entrySet()) {
            for (Component component : e.getValue()) {
                Map<String, String> componentProperties = parser.getComponentProperties(component.getDescription());
                //retreive the characteristics of the flavor
                int vcpus = 1, ram = 1024, disk = 20;
                String flavorString =  componentProperties.get("flavor");
                for (String s : flavorString.split("\\s+")) {
                    int val = Integer.parseInt(s.substring(s.indexOf(":") + 1));
                    if (s.startsWith("vcpus")) vcpus = val;
                    else if (s.startsWith("ram")) ram = val;
                    else if (s.startsWith("disk"))disk = val;
                }

                int count = 1;
                if (!componentProperties.containsKey("minInstances")) {
                    logger.debug("there is no 'minInstances' entry in the component properties, assuming 1");
                } else {
                    //retreive the flavor by its characteristics
                    count = Integer.parseInt(componentProperties.get("minInstances"));
                }

                //this is the provided resource of this flavor
                List<ProvidedResource> flavors = getProvidedResourceByFlavorInfo(vcpus, ram, disk);
                if(flavors.isEmpty()) throw new Exception("No flavors matching the requirements ("+flavorString+")");
                ProvidedResource provRes = flavors.get(0);
                //add the required resources
                for (int i = 0; i < count; i++) {
                    Resource res = new Resource(deployment, component, provRes);
                    resources.add(res);
                    store(res);
                }

            }
        }
    }

    public Application getApplication(){
        return this.app;
    }



}
