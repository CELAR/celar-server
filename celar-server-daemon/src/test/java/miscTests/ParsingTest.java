package miscTests;
import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Node;
import com.sixsq.slipstream.persistence.Target;

import static gr.ntua.cslab.celar.server.daemon.shared.ServerStaticComponents.ssService;
import static gr.ntua.cslab.celar.slipstreamClient.SSXMLParser.parse;
import gr.ntua.cslab.celar.server.beans.DeploymentState;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import gr.ntua.cslab.database.parsers.ToscaHandler;
import static gr.ntua.cslab.database.EntityTools.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.CSARParser;
import tools.Parser;


/**
 *
 * @author cmantas
 */
public class ParsingTest {
    
    static SlipStreamSSService ssService;
    
    public static void initClient() throws ValidationException{
            String 
                    username  = "celar",
                    password  = "celar2015",
                    slipstreamHost  = "https://83.212.102.166",
                    connectorName  = "okeanos";
            
             ssService  = new SlipStreamSSService(username, password, slipstreamHost, connectorName);
    }
    
    
    public static void main(String args[]) throws Exception{
     
    	Parser tc = new CSARParser("/Users/npapa/Desktop/CELAR/MySubmittedApp.csar");
        //store the description in  the DB
        ToscaHandler th = new ToscaHandler(tc);
        
        HashMap<String, Node> nodes = new HashMap();
        //iterate through modules
        for (String module : tc.getModules()) {
            System.out.println("\t" + module);

            //module dependecies
            System.out.println("\t\tdepends on: " + tc.getModuleDependencies(module));

            //iterate through components
            for (String component : tc.getModuleComponents(module)) {
                System.out.println("\t\t" + component);
                System.out.println("\t\t\tdepends on: " + tc.getComponentDependencies(component));
                
                String preAttachScript="";
                String postAttachScript="";
                String preDetachScript="";
                String postDetachScript="";
                String preScaleScript="";
                String postScaleScript="";
                //component properties
                List<ModuleParameter> parameters = new ArrayList<ModuleParameter>();
                Set<Target> targets = new HashSet<Target>();
                for (Map.Entry prop : tc.getComponentProperties(component).entrySet()) {
                    System.out.println("\t\t\t" + prop.getKey() );
                    if (prop.getKey().toString().contains("ImageArtifactPropertiesType")) {
                        System.out.println("\t\t\t" + prop.getKey() + " : " + prop.getValue());
                    } else if (prop.getKey().toString().equals("executeScript")) {
                        System.out.println("\t\t\t" + prop.getKey());
                        System.out.println("Execute script: " + prop.getValue().toString());
                    } else if (prop.getKey().toString().contains("scaleOut")) {
                        System.out.println("\t\t\t" + prop.getKey());
                        System.out.println("Add script: " + prop.getValue().toString());
                    } else if (prop.getKey().toString().contains("scaleIn")) {
                        System.out.println("\t\t\t" + prop.getKey());
                        System.out.println("Remove script: " + prop.getValue().toString());
                    } else if (prop.getKey().toString().contains("vmResize")) {
                        System.out.println("\t\t\t" + prop.getKey());
                        System.out.println("Script: " + prop.getValue().toString());
                    } else if (prop.getKey().toString().contains("attachDisk")) {
                        //System.out.println("\t\t\t" + prop.getKey());
                        //System.out.println("Script: " + prop.getValue().toString());
                    } else if (prop.getKey().toString().contains("detachDisk")) {
                        System.out.println("\t\t\t" + prop.getKey());
                        System.out.println("Script: " + prop.getValue().toString());
                    } else if (prop.getKey().toString().equals("flavor")) {
                        System.out.println("\t\t\t" + prop.getKey() + " : " + prop.getValue());
                    }
                }
            }
        }
        
        
    	
        /*initClient();
        String deploymentId="e7054d0e-cfc0-4372-b9d6-076f56f62501";
        Map<String,String> test  = ssService.getAllRuntimeParams(deploymentId);
        System.out.println(test);

        DeploymentState depState = new DeploymentState(test, deploymentId);*/
        //store(depState); //this will fail with 
                            //Key (deployment_id)=(e7054d0e-cfc0-4372-b9d6-076f56f62501) is not present in table "deployment
    }
}
