package gr.ntua.cslab.celar.slipstreamClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Target;

public class YCSBClientTest {

	public static ImageModule putModule(SlipStreamSSService ssservise) throws Exception {

		String name = "examples/CELAR/Cassandra/ycsbClient";
		ImageModule module = new ImageModule(name);
		module.setModuleReference("module/examples/images/ubuntu-12.04");
		module.setLoginUser("ubuntu");
		module.setPlatform("ubuntu");
		module.setDescription("YCSB client");
		Authz auth = new Authz(ssservise.getUser(), module);
		module.setAuthz(auth);
		

		Set<Target> targets = new HashSet<Target>();
	    BufferedReader br = new BufferedReader(new FileReader("src/main/resources/ycsbClient.sh"));
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
		
		String parameterName = "Flexiant.ram";
		String description = "ram";
		String value = "2048";
	
		ModuleParameter parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("2048");
		module.setParameter(parameter);
		
		parameterName = "Flexiant.cpu";
		description = "cpu";
		value = "1";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("1");
		module.setParameter(parameter);
		
		ssservise.putModule(module);
		return module;
	}
}
