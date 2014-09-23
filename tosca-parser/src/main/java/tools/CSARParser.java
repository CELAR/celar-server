/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;

/**
 *
 * @author cmantas
 */
public class CSARParser {
    
    public  Logger logger = Logger.getLogger(CSARParser.class);
    
     List<String> modules = new java.util.LinkedList();
     Map<String, List<String> > moduleComponents = new java.util.TreeMap();
     Map<String, Map<String, String>> componentsProperties = new java.util.TreeMap();
     private List<Array> ModuleDependencies;
     private List<Array> ComponentDependencies;
     private Path tempDir=null;
    
    
    public CSARParser(){}
    
    public CSARParser(String csarFileName) throws Exception{
        tempDir = Tools.extractCsar(csarFileName);
        String toscaPath = Tools.findToscaPath(tempDir);
        logger.debug("path of tosca file is: "+toscaPath);
        //load the tosca from file
        DocumentRoot tosca = Tools.loadFromFile(tempDir+File.separator+toscaPath);
        //parse the tosca xml
        handleRoot(tosca);

        //delete the temp dir
        Tools.recursiveDelete(tempDir.toFile());
    }
    

    
    
    
    private  void handleRoot(DocumentRoot toscaRoot) throws Exception {
        //get root services -- should be one
        EList<TServiceTemplate> rootServiceTemplates = toscaRoot.getDefinitions().getServiceTemplate();
        //root application
        TServiceTemplate application = rootServiceTemplates.remove(0);
        
        //module relationships
        for(TRelationshipTemplate rel: application.getTopologyTemplate().getRelationshipTemplate()){
            handleModuleDependency(rel);
        }
        
        //modules 
        for (TServiceTemplate m : rootServiceTemplates) {
            //System.out.println(m.getBoundaryDefinitions());
            handleModule(m);
        }
        
       //scrpits step 1
       for(TNodeTypeImplementation ni: toscaRoot.getDefinitions().getNodeTypeImplementation()){
           handleNodeImplementation(ni);
       }
       
       //scripts step 2
       for(TArtifactTemplate a: toscaRoot.getDefinitions().getArtifactTemplate()){
           handleArtifact(a);
       }
       
        System.out.println(componentsProperties);         
       
    }
    
    
    private  void handleModuleDependency(TRelationshipTemplate rel) {
          logger.debug("Handling dependency: "+rel.getName());
//          logger.debug("from"+rel.getSourceElement().getRef());
//          logger.debug("to "+rel.getTargetElement().getRef());
          
    } 
     

    private  void handleModule(TServiceTemplate module) {
        String moduleName = module.getName();
        logger.debug("Handling module: " + moduleName);
        modules.add(module.getName());
       // module.getTopologyTemplate()
      EList<TNodeTemplate> components = module.getTopologyTemplate().getNodeTemplate();
      moduleComponents.put(moduleName,new java.util.LinkedList());
      for(TNodeTemplate component: components){          
          handleComponent(moduleName, component);
      }
      
    }

    private  void handleComponent(String moduleName, TNodeTemplate component) {
        String componentName = component.getName();
        //add component to father module
        moduleComponents.get(moduleName).add(componentName);
        
        Map<String, String> componentProperties= new java.util.TreeMap();
        logger.debug("Handling component:" + componentName);
        
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
        componentProperties.put("Flavor", ""+flavorString);  
        logger.debug("flavor:"+flavorString);
        
        //get artifacts
        for (TDeploymentArtifact da : component.getDeploymentArtifacts().getDeploymentArtifact()){
            switch (da.getArtifactType().toString()){
                    case "VMI":
                        componentProperties.put("Image", da.getName());
                        break;
                    default:
                        break;
            }
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
            logger.debug("arrtifact:" + ia);
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
                if (propertiesEntry.getValue().equals(artifactId)) {
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
        //
        componentProps.put(scriptName, scriptContents);
        
    }

    
    
    
    
        public static void main(String[] args) throws Exception{
      
        // extract the csar archive
//        String filepath = "app.csar";
//        String extractPath = "playground";
//        Tools.extractCsar(new File(filepath), extractPath);
 //      File toscaFile = handler.getDefinitionFiles()[0];
        
        DocumentRoot tosca = Tools.loadFromFile( "src/main/resources/myApp_v0.0.5.tosca" );
        //System.out.println(tosca.getDefinitions().getNodeTypeImplementation());
            
        
        CSARParser tc = new CSARParser();
        tc.handleRoot(tosca);
        //System.out.println(modules);
          
  
    }
    
    
    
}
