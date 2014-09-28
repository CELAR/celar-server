package gr.ntua.cslab.celar.slipstreamClient;

import java.util.HashMap;
import java.util.Map;

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.ProjectModule;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;

public class CassandraTest {
	public static void main(String[] args) throws Exception {
		SlipStreamSSService ssservise = new SlipStreamSSService("ioannis", "*", "https://109.231.121.23");

		/*String projectName = "examples/CELAR/Cassandra";
		ProjectModule project = new ProjectModule(projectName);
		Authz auth = new Authz(ssservise.getUser(), project);
		project.setAuthz(auth);
		ssservise.putModule(project);*/
		
		/*ImageModule seedNode = CassandraSeedNodeTest.putModule(ssservise);
		ImageModule node = CassandraNodeTest.putModule(ssservise);
		ImageModule ycsb = YCSBClientTest.putModule(ssservise);
		CassandraApp.putModule(ssservise, seedNode, node, ycsb);*/
		/*Map<String, String> deploymentParameters = new HashMap<String, String>();
		deploymentParameters.put("cassandraSeedNode:multiplicity", "1");
		deploymentParameters.put("cassandraNode:multiplicity", "1");
		deploymentParameters.put("ycsbClient:multiplicity", "1");
		ssservise.launchApplication("examples/CELAR/Cassandra/cassandra", deploymentParameters);
*/
		String type = "cassandraNode";
		String deploymnetId = "899d18f3-c375-4bca-969f-7c429f5e596c";
		ssservise.addVM(deploymnetId, type, 1);
		//System.exit(0);
	}
}
