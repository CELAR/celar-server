package gr.ntua.cslab.celar.slipstreamClient;

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.ProjectModule;

public class CassandraTest {
	public static void main(String[] args) throws Exception {
		SlipStreamSSService ssservise = new SlipStreamSSService("ioannis", "a1s2d3f4", "https://109.231.121.23");

		/*String projectName = "examples/CELAR/Cassandra";
		ProjectModule project = new ProjectModule(projectName);
		Authz auth = new Authz(ssservise.getUser(), project);
		project.setAuthz(auth);
		ssservise.putModule(project);*/
		
		ImageModule seedNode = CassandraSeedNodeTest.putModule(ssservise);
		ImageModule node = CassandraNodeTest.putModule(ssservise);
		ImageModule ycsb = YCSBClientTest.putModule(ssservise);
		CassandraApp.putModule(ssservise, seedNode, node, ycsb);
		System.exit(0);
	}
}
