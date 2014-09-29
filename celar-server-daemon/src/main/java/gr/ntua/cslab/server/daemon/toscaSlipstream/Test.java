package gr.ntua.cslab.server.daemon.toscaSlipstream;

import static org.junit.Assert.fail;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import tools.CSARParser;
import tools.Parser;

import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Node;
import com.sixsq.slipstream.persistence.Target;

public class Test {
	public static void main(String[] args) throws Exception {
		SlipStreamSSService ssservise = new SlipStreamSSService("celar", "a1s2d3f4", "https://83.212.122.157");
		
		
        //create a Parser instance
        Parser tc  = new CSARParser("src/main/resources/myApp_npapa.csar");
        
        //application name and version
        System.out.println("Application: "+tc.getAppName()+" v"+tc.getAppVersion());
        String appName = ssservise.createApplication(tc.getAppName(), tc.getAppVersion());
		HashMap<String, Node> nodes = new HashMap<String, Node>();
        
        //iterate through modules
        for(String module: tc.getModules()){
            System.out.println("\t"+module);
            
            //module dependecies
            System.out.println("\t\tdepends on: "+tc.getModuleDependencies(module));
            
            //iterate through components
            for(String component: tc.getModuleComponents(module)){
                System.out.println("\t\t"+component);
                ImageModule imModule = new ImageModule(appName+"/"+component);
                Authz auth = new Authz(ssservise.getUser(), imModule);
                imModule.setAuthz(auth);
        		
                
                //component dependencies
                System.out.println("\t\t\tdepends on: "+tc.getComponentDependencies(component));
                
                //component properties
            	Set<Target> targets = new HashSet<Target>();
                for(Entry prop: tc.getComponentProperties(component).entrySet()){
                    System.out.println("\t\t\t"+prop.getKey()+": "+prop.getValue());
                    if(prop.getKey().toString().equals("VMI")){
                        imModule.setModuleReference("images/ubuntu-12.04");
                    }
                    else if(prop.getKey().toString().equals("executeScript")){
                		Target t = new Target(Target.EXECUTE_TARGET, prop.getValue().toString());
                		targets.add(t);
                	
                    }
                }
                
        		String parameterName = "ready";
        		String description = "Server ready";
        	
        		ModuleParameter parameter = new ModuleParameter(parameterName, "", description);
        		parameter.setCategory("Output");
        		imModule.setParameter(parameter);
        		
        		
        		parameterName = "Flexiant.ram";
        		description = "ram";
        		String value = "2048";
        	
        		parameter = new ModuleParameter(parameterName, value, description);
        		parameter.setCategory("Flexiant");
        		parameter.setDefaultValue("2048");
        		imModule.setParameter(parameter);
        		
        		parameterName = "Flexiant.cpu";
        		description = "cpu";
        		value = "1";
        	
        		parameter = new ModuleParameter(parameterName, value, description);
        		parameter.setCategory("Flexiant");
        		parameter.setDefaultValue("1");
        		imModule.setParameter(parameter);
        		
        		parameterName = "okeanos.instance.type";
        		description = "Flavor";
        		value = "C2R2048D10ext_vlmc";
        	
        		parameter = new ModuleParameter(parameterName, value, description);
        		parameter.setCategory("okeanos");
        		parameter.setDefaultValue("C2R2048D10ext_vlmc");
        		imModule.setParameter(parameter);
        		
        		parameterName = "okeanos.security.groups";
        		description = "Security Groups (comma separated list)";
        		value = "default";
        	
        		parameter = new ModuleParameter(parameterName, value, description);
        		parameter.setCategory("okeanos");
        		parameter.setDefaultValue("default");
        		imModule.setParameter(parameter);
        		
        		ssservise.putModule(imModule);
        		nodes.put(imModule.getShortName(), new Node(imModule.getShortName(), imModule));
        		
            }
        }

		//add DeploymentModule 
		String name = appName+"/"+appName;
		DeploymentModule deployment = new DeploymentModule(name);
		Authz auth = new Authz(ssservise.getUser(), deployment);
		deployment.setAuthz(auth);
		System.out.println(nodes);
		deployment.setNodes(nodes );
	
		ssservise.putModule(deployment);
            
		System.exit(0);
	}
}
