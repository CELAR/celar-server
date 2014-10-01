package gr.ntua.cslab.db_entities.parsers;

import gr.ntua.cslab.db_entities.Application;
import gr.ntua.cslab.db_entities.Component;
import gr.ntua.cslab.db_entities.ComponentDependency;
import gr.ntua.cslab.db_entities.DBException;
import gr.ntua.cslab.db_entities.Module;
import gr.ntua.cslab.db_entities.ModuleDependency;
import gr.ntua.cslab.db_entities.ProvidedResource;
import gr.ntua.cslab.db_entities.User;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    List<ProvidedResource> povidedResources = new java.util.LinkedList<>();
    
    
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
            cd.delete();
        }
        //delete module dependencies
        for(ModuleDependency md: moduleDependencies){
            md.delete();
        }
        //delete components and modules
        for (Module m : modules) {
            for (Component c : moduleComponents.get(m)) {
                c.delete();
            }
            m.delete();
        }        
        app.delete();
        
        logger.info("Removed the Application Description for "+app.getDescription());
    }
    
    
    private void handleUser() throws DBException{
         user = User.getByName("celar-user");
        if (user==null){
            user = new User("celar-user");
            user.store();
        }
    }
    
    private void handleApplication()throws DBException{
        String name = parser.getAppName();
        String v = parser.getAppVersion();
        int i= v.indexOf(".");
        int mjv = Integer.parseInt(v.substring(0, i));
        int mnv = Integer.parseInt(v.substring(i+1, v.length()));
        app = new Application(mjv, mnv, name, user);
        app.store();
    }
    
    private Module handleModule(String name) throws DBException {
        //create module
        Module module = new Module(name, app);
        module.store();
        modules.add(module);
        moduleComponents.put(module, new java.util.LinkedList());
        return module;
    }
    
    private void handleModuleDependencies(Module m) throws DBException{
        for(String depName:parser.getModuleDependencies(m.getName())){
                Module dep = getModuleByName(depName);
                ModuleDependency md = new ModuleDependency(m, dep);
                moduleDependencies.add(md);
                md.store();
            }

    }

    private Component handleComponent(Module m, String componentName, String resourceTypeName) throws DBException {
        Component c = new Component(m, componentName, resourceTypeName);
        moduleComponents.get(m).add(c);
        c.store();
        return c;
    }

    private void handleComponentDependencies(Module m, Component c) throws DBException {
        for(String depName:parser.getComponentDependencies(c.getDescription())){
                Component dep = getComponentByName(depName, moduleComponents.get(m));
                ComponentDependency cd = new ComponentDependency(c, dep);
                componentDependencies.add(cd);
                cd.store();
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
                
                //handle each provided resource
                handleComponentResources(parser.getComponentProperties(componentName));
                
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
        
        //orphan the parser (free mem)
        parser = null;
        logger.info("Stored the Application Description for "+app.getDescription());
    }

    public static void main(String args[]) throws Exception {
        ToscaHandler tp = new ToscaHandler("app_7.csar");
        
        tp.storeDescription();
        tp.removeDescription();
    }

    void storeConfiguration() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleComponentResources(Map<String, String> componentProperties) throws Exception {
        
        //retreive the characteristics of the flavor
        int vcpus=1, ram=1024, disk=20;
        for(String s: componentProperties.get("flavor").split("\\s+")){
            int val = Integer.parseInt(s.substring(s.indexOf(":")+1));
            if (s.startsWith("vcpus")) vcpus=val;
            else if (s.startsWith("ram")) ram=val;
            else if (s.startsWith("disk")) vcpus=val;
        }
        
        //retreive the flavor by its characteristics
        logger.info(""+vcpus+" "+ram+" "+disk);
    }




}
