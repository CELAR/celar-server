package gr.ntua.cslab.celar.slipstreamClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//import org.junit.Test;


import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.CloudImageIdentifier;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Node;
import com.sixsq.slipstream.persistence.ServiceConfiguration;
import com.sixsq.slipstream.persistence.User;
import com.sixsq.slipstream.persistence.ProjectModule;
import com.sixsq.slipstream.persistence.Target;

import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;


public class SlipStreamSSServiceTest {

	public static void testPutApplication(SlipStreamSSService ssservise) throws Exception {
	
		User user = new User("dfs");
		ServiceConfiguration conf = new ServiceConfiguration();
		user.addSystemParametersIntoUser(conf);
		
		//create project directory
		String projectName = "examples1/CELAR/apacheExample3";
		ProjectModule project = new ProjectModule(projectName);
		Authz auth = new Authz(ssservise.getUser(), project);
		project.setAuthz(auth);
		ssservise.putModule(project);
		
		//add ImageModule apache
		String name = "examples1/CELAR/apacheExample3/apache";
		ImageModule module = new ImageModule(name);
		module.setModuleReference("module/examples1/images/ubuntu-12.04");
		module.setLoginUser("ubuntu");
		module.setPlatform("ubuntu");
		module.setDescription("Apache!! web server appliance with custom landing page.");
		auth = new Authz(ssservise.getUser(), module);
		module.setAuthz(auth);
		
		Set<Target> targets = new HashSet<Target>();
		Target t = new Target(Target.REPORT_TARGET, "#!/bin/sh -x \n"
				+ "cp /var/log/apache2/access.log $SLIPSTREAM_REPORT_DIR \n"
				+ "cp /var/log/apache2/error.log $SLIPSTREAM_REPORT_DIR");
		targets.add(t);
	
		Target t1 = new Target(Target.EXECUTE_TARGET, "#!/bin/sh -xe \n"
				+ "apt-get update -y \n"
				+ "apt-get install -y apache2 \n"
				+ "clientnum=$(ss-get client:multiplicity) \n"
				+ "echo \"Clients: $clientnum\" > /var/www/data.txt \n"
				+ "service apache2 stop \n"
				+ "port=$(ss-get port) \n"
				+ "sed -i -e 's/^Listen.*$/Listen '$port'/' /etc/apache2/ports.conf \n"
				+ "sed -i -e 's/^NameVirtualHost.*$/NameVirtualHost *:'$port'/' /etc/apache2/ports.conf \n"
				+ "sed -i -e 's/^<VirtualHost.*$/<VirtualHost *:'$port'>/' /etc/apache2/sites-available/default \n"
				+ "service apache2 start \n"
				+ "ss-set ready true \n");
		targets.add(t1);
		
		Target t2 = new Target(Target.ONVMADD_TARGET, "#!/bin/sh -xe \n"
				+ "clientnum=$(ss-get client:multiplicity) \n"
				+ "echo \"Clients: $clientnum\" > /var/www/data.txt \n");
		targets.add(t2);
		Target t3 = new Target(Target.ONVMREMOVE_TARGET, "#!/bin/sh -xe \n"
				+ "clientnum=$(ss-get client:multiplicity) \n"
				+ "echo \"Clients: $clientnum\" > /var/www/data.txt \n");
		targets.add(t3);
		
		module.setTargets(targets);
		String parameterName = "port";
		String description = "Port";
		String value = "8080";
	
		ModuleParameter parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Output");
		parameter.setDefaultValue("8080");
		module.setParameter(parameter);
	
		parameterName = "ready";
		description = "Server ready";
	
		parameter = new ModuleParameter(parameterName, "", description);
		parameter.setCategory("Output");
		module.setParameter(parameter);
		
		
		parameterName = "Flexiant.ram";
		description = "ram";
		value = "2048";
	
		parameter = new ModuleParameter(parameterName, value, description);
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
		
		
		//add ImageModule client
		String name1 = "examples1/CELAR/apacheExample3/client";
		ImageModule module1 = new ImageModule(name1);
		module1.setModuleReference("module/examples1/images/ubuntu-12.04");
		module1.setLoginUser("ubuntu");
		module1.setPlatform("ubuntu");
		module1.setDescription("Web client tests server connectivity and verifies content.");
		auth = new Authz(ssservise.getUser(), module1);
		module1.setAuthz(auth);
		
		targets = new HashSet<Target>();
		t = new Target(Target.REPORT_TARGET, "#!/bin/sh -x \n"
				+ "web_server_ip=$(ss-get --timeout 360 apache:hostname) \n"
				+ "web_server_port=$(ss-get --timeout 360 apache:port) \n"
				+ "ss-get --timeout 360 apache:ready \n"
				+ "ENDPOINT=http://${web_server_ip}:${web_server_port}/data.txt \n"
				+ "wget -t 2 -O /tmp/data.txt ${ENDPOINT} \n"
				+ "[ \"$?\" = \"0\" ] & ss-set statecustom \"$(cat /tmp/data.txt)\" || ss-abort \"Could not get the test file: ${ENDPOINT}\" \n"
				+ "cp /tmp/data.txt $SLIPSTREAM_REPORT_DIR \n");
		targets.add(t);
	
		t1 = new Target(Target.EXECUTE_TARGET, "#!/bin/sh -xe \n"
				+ "# Wait for the metadata to be resolved \n"
				+ "web_server_ip=$(ss-get --timeout 360 apache:hostname) \n"
				+ "web_server_port=$(ss-get --timeout 360 apache:port) \n"
				+ "ss-get --timeout 360 apache:ready \n"
				+ "# Execute the test \n"
				+ "ENDPOINT=http://${web_server_ip}:${web_server_port}/data.txt \n"
				+ "wget -t 2 -O /tmp/data.txt ${ENDPOINT} \n"
				+ "[ \"$?\" = \"0\" ] & ss-set statecustom \"$(cat /tmp/data.txt)\" || ss-abort \"Could not get the test file: ${ENDPOINT}\" \n");
		targets.add(t1);
		
		module1.setTargets(targets);
		
		parameterName = "Flexiant.ram";
		description = "ram";
		value = "2048";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("2048");
		module1.setParameter(parameter);
		
		parameterName = "Flexiant.cpu";
		description = "cpu";
		value = "1";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("1");
		module1.setParameter(parameter);
		
		ssservise.putModule(module1);
	
		//add DeploymentModule 
		String name2 = "examples1/CELAR/apacheExample3/apacheExample";
		DeploymentModule deployment = new DeploymentModule(name2);
		auth = new Authz(ssservise.getUser(), deployment);
		deployment.setAuthz(auth);
		
		
		
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		nodes.put("apache", new Node("apache", module));
		nodes.put("client", new Node("client", module1));
		deployment.setNodes(nodes );
	
		ssservise.putModule(deployment);
	}


	private static void testPutImage(String image, SlipStreamSSService ssservise) throws Exception {

		String projectName = "images1";
		ProjectModule project = new ProjectModule(projectName);
		Authz auth = new Authz(ssservise.getUser(), project);
		project.setAuthz(auth);
		ssservise.putModule(project);
		
		ImageModule module = new ImageModule(image);
		module.setIsBase(true);
		module.setLoginUser("ubuntu");
		module.setPlatform("ubuntu");
		module.setDescription("Baseline Image");
		auth = new Authz(ssservise.getUser(), module);
		module.setAuthz(auth);
		
		Set<CloudImageIdentifier> cloudImageIdentifiers = new HashSet<CloudImageIdentifier>();
		CloudImageIdentifier ident = new CloudImageIdentifier(module, "Flexiant", "81aef2d3-0291-38ef-b53a-22fcd5418e60");
		cloudImageIdentifiers.add(ident);
		
		ident = new CloudImageIdentifier(module, "stratuslab", "HZTKYZgX7XzSokCHMB60lS0wsiv");
		cloudImageIdentifiers.add(ident);
		
		ident = new CloudImageIdentifier(module, "okeanos", "fe31fced-a3cf-49c6-b43b-f58f5235ba45");
		cloudImageIdentifiers.add(ident);

		module.setCloudImageIdentifiers(cloudImageIdentifiers );
		
		String parameterName = "Flexiant.ram";
		String description = "ram";
		String value = "2048";
	
		ModuleParameter parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("2048");
		module.setParameter(parameter);
		
		parameterName = "Flexiant.cpu";
		description = "cpu";
		value = "2";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("2");
		module.setParameter(parameter);

        parameterName = "okeanos.instance.type";
        description = "Flavor";
        value = "C2R2048D10ext_vlmc";

        parameter = new ModuleParameter(parameterName, value, description);
        parameter.setCategory("okeanos");
        parameter.setDefaultValue("C2R2048D10ext_vlmc");
        module.setParameter(parameter);

        parameterName = "okeanos.security.groups";
        description = "Security Groups (comma separated list)";
        value = "default";

        parameter = new ModuleParameter(parameterName, value, description);
        parameter.setCategory("okeanos");
        parameter.setDefaultValue("default");
        module.setParameter(parameter);
        
		ssservise.putModule(module);
	}
	
	public static void main(String[] args) throws Exception {
		String cookie = "com.sixsq.slipstream.cookie=com.sixsq.idtype=local&com.sixsq.identifier=ioannis&com.sixsq.expirydate=1413407587469&com.sixsq.signature=e9wlflxyxi139ge3anrf68d27j1o9uvoz0sb4oh003xpdbfrx0mi7jflj3g12or64v7anqpjr9mepa778vjy6qgqqfqbnbgxwu8; Path=/";
		
		SlipStreamSSService ssservise = new SlipStreamSSService("celar", "celar2015", "https://83.212.102.166");
		//ssservise.addVM("200e1a1e-0a7d-4ea2-9a97-52206566c821", "ubuntu_vm", 1);
		ssservise.launchApplication("test/ubuntu_deployment", new HashMap<String,String>());
		ssservise.terminateApplication("200e1a1e-0a7d-4ea2-9a97-52206566c821");
		//ssservise.scaleVM("200e1a1e-0a7d-4ea2-9a97-52206566c821", "ubuntu_vm", "1",2,2048);
		//System.out.println(ssservise.getDeploymentIPs("aa418e9a-ddfd-4273-8019-cd06d7bb708a"));
		
		//System.out.println(ssservise.getImageReference("ubuntu-12.04"));
		//System.out.println(ssservise.getImageReference("ubuntu-12.04"));
		//testPutImage("images1/ubuntu-12.04", ssservise);
		
		//testPutApplication(ssservise);
		
//		HashMap<String,String> parameters = new HashMap<String, String>();
//		parameters.put("apache:multiplicity", "1");
//		parameters.put("apache:Flexiant.cpu", "2");
//		parameters.put("client:multiplicity", "1");
//		String deploymnetId = ssservise.launchApplication("examples/CELAR/apacheExample3/apacheExample", parameters);
		
//		if(deploymnetId==null)
//			System.exit(0);
//		System.out.println(deploymnetId);
//		ssservise.waitForReadyState(deploymnetId);
//		Thread.sleep(30000);
//		
//		String type = "client";
//		//String deploymnetId = "b1803152-c3fb-44c7-aea0-f8cff38248d5";
//		ssservise.addVM(deploymnetId, type, 2);
//		Thread.sleep(30000);
//		ssservise.waitForReadyState(deploymnetId);
//		ssservise.removeVM(deploymnetId, type, "2,3");
//		Thread.sleep(30000);
//		ssservise.waitForReadyState(deploymnetId);
//		
//		ssservise.terminateApplication(deploymnetId);
//		System.out.println(ssservise.getDeploymentState(deploymnetId));
		
		System.exit(0);
		
	}




}
