package gr.ntua.cslab.celar.slipstreamClient.test.gr.ntua.cslab.celar.slipstreamClient;

import java.util.HashMap;

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.Node;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;

public class CassandraApp {

	public static void putModule(SlipStreamSSService ssservise, ImageModule seedNode, ImageModule node, ImageModule ycsb) throws Exception {
		//add DeploymentModule 
		String name2 = "examples/CELAR/Cassandra/cassandra";
		DeploymentModule deployment = new DeploymentModule(name2);
		Authz auth = new Authz(ssservise.getUser(), deployment);
		deployment.setAuthz(auth);
		
		
		
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		nodes.put("cassandraSeedNode", new Node("cassandraSeedNode", seedNode));
		nodes.put("cassandraNode", new Node("cassandraNode", node));
		nodes.put("ycsbClient", new Node("ycsbClient", ycsb));
		deployment.setNodes(nodes );
	
		ssservise.putModule(deployment);
	}

}
