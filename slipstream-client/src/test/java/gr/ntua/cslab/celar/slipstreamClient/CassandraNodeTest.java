package gr.ntua.cslab.celar.slipstreamClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.sixsq.slipstream.exceptions.ConfigurationException;
import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Target;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;

public class CassandraNodeTest {

	public static ImageModule putModule(SlipStreamSSService ssservise) throws Exception {

		String name = "examples/CELAR/Cassandra/cassandraNode";
		ImageModule module = new ImageModule(name);
		module.setModuleReference("module/examples/images/ubuntu-12.04");
		module.setLoginUser("ubuntu");
		module.setPlatform("ubuntu");
		module.setDescription("Cassandra node");
		Authz auth = new Authz(ssservise.getUser(), module);
		module.setAuthz(auth);
		

		Set<Target> targets = new HashSet<Target>();
	    BufferedReader br = new BufferedReader(new FileReader("src/main/resources/node.sh"));
	    String script = "";
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        script = sb.toString();
	    } finally {
	        br.close();
	    }
		Target t1 = new Target(Target.EXECUTE_TARGET, script);
		targets.add(t1);
		
		module.setTargets(targets);
	
		String parameterName = "ready";
		String description = "Server ready";
	
		ModuleParameter parameter = new ModuleParameter(parameterName, "", description);
		parameter.setCategory("Output");
		module.setParameter(parameter);
		
		
		parameterName = "Flexiant.ram";
		description = "ram";
		String value = "2048";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("2048");
		module.setParameter(parameter);
		
		parameterName = "Flexiant.cpu";
		description = "cpu";
		value = "2";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("1");
		module.setParameter(parameter);
		
		ssservise.putModule(module);
		return module;
	}

}
