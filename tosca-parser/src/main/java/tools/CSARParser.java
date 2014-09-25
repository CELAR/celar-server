package tools;

import eu.celar.tosca.DocumentRoot;
import eu.celar.tosca.ImplementationArtifactType;
import eu.celar.tosca.TArtifactReference;
import eu.celar.tosca.TArtifactTemplate;
import eu.celar.tosca.TDeploymentArtifact;
import eu.celar.tosca.TNodeTemplate;
import eu.celar.tosca.TNodeTypeImplementation;
import eu.celar.tosca.TRelationshipTemplate;
import eu.celar.tosca.TServiceTemplate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;

/**
 *
 * @author cmantas
 */
public class CSARParser implements Parser{
    
    public  Logger logger = Logger.getLogger(CSARParser.class);
    
     List<String> modules = new java.util.LinkedList();
     Map<String, List<String> > moduleComponents = new java.util.TreeMap();
     Map<String, Map<String, String>> componentsProperties = new java.util.TreeMap();
     private List<String[]> moduleDependencies = new java.util.LinkedList();
     private List<String[]> componentDependencies = new java.util.LinkedList();
     private Path tempDir=null;
     String name, version; 
    
    
    /***
     * Default constructor
     */
    public CSARParser(){}
    
    
    /***
     * Creates a CSARParser instance from a given .csar (zip) file
     * @param csarFileName
     * @throws Exception 
     */
    public CSARParser(String csarFileName) throws Exception{
        tempDir = Tools.extractCsar(csarFileName);
        Map<String, String> toscaProps = Tools.getToscaMeta(tempDir);
        String toscaPath = toscaProps.get("Name");
        logger.debug("path of tosca file is: "+toscaPath);
        version = toscaProps.get("Version");
        logger.debug("version is:"+version);
        //load the tosca from file
        DocumentRoot tosca = Tools.loadFromFile(tempDir+File.separator+toscaPath);
        //find the version
        //parse the tosca xml
        handleRoot(tosca);

        //delete the temp dir
        Tools.recursiveDelete(tempDir.toFile());
    }
    

    /***
     * Handles the root of the document (recursive)
     * @param toscaRoot
     * @throws Exception 
     */   
    private  void handleRoot(DocumentRoot toscaRoot) throws Exception {
        //get root services -- should be one
        EList<TServiceTemplate> rootServiceTemplates = toscaRoot.getDefinitions().getServiceTemplate();
        //root application
        TServiceTemplate application = rootServiceTemplates.remove(0);
        name = application.getName();
        
        //module relationships
        for(TRelationshipTemplate rel: application.getTopologyTemplate().getRelationshipTemplate()){
            handleDependency(moduleDependencies, rel);
        }
        
        //modules (recursive for components)
        for (TServiceTemplate m : rootServiceTemplates) {
            handleModule(m);
        }
        
       //scrits step 1
       for(TNodeTypeImplementation ni: toscaRoot.getDefinitions().getNodeTypeImplementation()){
           handleNodeImplementation(ni);
       }
       //scripts step 2
       for(TArtifactTemplate a: toscaRoot.getDefinitions().getArtifactTemplate()){
           handleArtifact(a);
       }       
       
    }

    /***
     * Handles a module (recursive)
     * @param module 
     */
    private void handleModule(TServiceTemplate module) {
        String moduleName = module.getName();
        logger.debug("Handling module: " + moduleName);
        modules.add(module.getName());
        String moduleId = module.getId();
        replaceReffs(moduleDependencies, moduleId, moduleName);

        //handle the component dependencies
        for (TRelationshipTemplate cd : module.getTopologyTemplate().getRelationshipTemplate()) {
            handleDependency(componentDependencies, cd);
        }

        //handle the component nodes
        EList<TNodeTemplate> components = module.getTopologyTemplate().getNodeTemplate();
        moduleComponents.put(moduleName, new java.util.LinkedList());
        for (TNodeTemplate component : components) {
            String componentName = component.getType().toString();
            moduleComponents.get(moduleName).add(componentName);
            handleComponent(component);
        }

    }

    /***
     * Handles a component
     * @param moduleName
     * @param component 
     */
    private  void handleComponent(TNodeTemplate component) {
        String componentName = component.getType().toString();        
        logger.debug("Handling component:" + componentName);
        String componentId = component.getId();
        replaceReffs(componentDependencies, componentId, componentName);
        
        //the properties of the component
        Map<String, String> componentProperties= new java.util.TreeMap();
        
        
        //get min instances
        int minInstances = component.isSetMinInstances()?component.getMinInstances():0;
        logger.debug("min inst:"+minInstances);
        componentProperties.put("minInstances", ""+minInstances);
        
        //get max instances
        int maxInstances = component.isSetMaxInstances()?((Number) component.getMaxInstances()).intValue(): -1;
        componentProperties.put("maxInstances", ""+maxInstances);        
        logger.debug("max inst:"+maxInstances);

        
        //get flavor
        AnyTypeImpl o = (AnyTypeImpl) component.getProperties().eContents().get(0);
        AnyTypeImpl vo = (AnyTypeImpl) o.getMixed().get(1).getValue();
        String flavorString = vo.getMixed().get(0).getValue().toString();
        componentProperties.put("flavor", ""+flavorString);  
        logger.debug("flavor:"+flavorString);
        
        //get artifacts
        for (TDeploymentArtifact da : component.getDeploymentArtifacts().getDeploymentArtifact()){
            componentProperties.put(da.getArtifactType().toString(), da.getName());
        }
        
        logger.debug("componennt properties: " + componentProperties);
        componentsProperties.put(componentName, componentProperties);
    }
    

    private  void handleNodeImplementation(TNodeTypeImplementation ni) throws Exception {
        String componentName = ni.getNodeType().toString();
        logger.debug("implementation for: " + componentName);
        Map props = componentsProperties.get(componentName);
        if (props==null){ 
            logger.error("Component: "+ componentName+" was not previously defined. Ignoring node implementation");
            return;
        }
        for (ImplementationArtifactType ia : ni.getImplementationArtifacts().getImplementationArtifact()) {
            String opName = ia.getOperationName();
            String artifactRef = ia.getArtifactRef().toString();
            props.put(opName+"Script", artifactRef);
        }
    }

    private  void handleArtifact(TArtifactTemplate a) {
        String artifactId = a.getId();
        Map<String, String> componentProps = null;
        String scriptName = null;
        boolean found = false;
        for (Map.Entry<String, Map<String, String>> componentPropsEntry : componentsProperties.entrySet()) {
            for (Map.Entry<String, String> propertiesEntry : componentPropsEntry.getValue().entrySet()) {
                if (propertiesEntry.getValue()!=null && propertiesEntry.getValue().equals(artifactId)) {
                    scriptName=propertiesEntry.getKey();
                    componentProps = componentPropsEntry.getValue();
                    found = true;
                    break;
                }
                if (found) {
                    break;
                }
            }
        }
        //check if you have found script that this artifact concerns
        if (!found) {
            logger.error("Orphan artifact: " + artifactId);
            return;
        }

        //assuming one artifact refference per artifact template
        TArtifactReference ar = a.getArtifactReferences().getArtifactReference().get(0);
        String scriptPath = ar.getReference();
        logger.debug("ScriptPath: "+scriptPath);
        String scriptContents = null;
        try {
            scriptContents = Tools.readFromFile(tempDir+File.separator+scriptPath);
        } catch (IOException ex) {
            logger.error("Could not find script file: "+scriptPath);
        }
        
        componentProps.put(scriptName, scriptContents);
        
    }
    
    private void handleDependency(List<String[]> store, TRelationshipTemplate rel) {
        String source = rel.getSourceElement().getRef();
        String target = rel.getTargetElement().getRef();
        logger.debug("Handling dependency: " + source + "-->" + target);
        String dep[] = {source, target};
        store.add(dep);
    }

    private static void replaceReffs(List<String[]> store, String reff, String name) {
        for (String[] d : store) {
            if (d[0].equals(reff)) {
                d[0] = name;
            }
            if (d[1].equals(reff)) {
                d[1] = name;
            }
        }
    }
    
    private List<String> getDependencies(List<String[]> store, String name){
        List<String> rv = new java.util.LinkedList<>();
        for(String[] d: store)
            if (d[1].equals(name)) rv.add(d[0]);
        return rv;
    }
    
    /* ============================  Getters =======================================*/
    
    @Override
    public List<String> getModules() {
        return modules;
    }

    @Override
    public List<String> getModuleComponents(String module) {
        return moduleComponents.get(module);
    }

    @Override
    public Map<String, String> getComponentProperties(String component) {
    
        return componentsProperties.get(component);
    }

    
    @Override
    public List<String> getModuleDependencies(String module) {
        return getDependencies(moduleDependencies, module);
    }

    @Override
    public List<String> getComponentDependencies(String component) {
        return getDependencies(componentDependencies, component);
    }

    @Override
    public String getAppName() {
       return name;
    }

    @Override
    public String getAppVersion() {
        return version;
    }
    
    
    public static void main(String[] args) throws Exception {

        // extract the csar archive
//        String filepath = "app.csar";
//        String extractPath = "playground";
//        Tools.extractCsar(new File(filepath), extractPath);
        //      File toscaFile = handler.getDefinitionFiles()[0];
        DocumentRoot tosca = Tools.loadFromFile("src/main/resources/extracted_csar/Definitions/myApp_v0.0.6_npapa.tosca");
//            
//        
        CSARParser tc = new CSARParser();
        tc.handleRoot(tosca);
        System.out.println(tc.componentsProperties);
        
        

    }



    
}
