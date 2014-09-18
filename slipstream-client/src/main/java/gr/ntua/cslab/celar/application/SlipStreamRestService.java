package gr.ntua.cslab.celar.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.ParameterCategory;
import com.sixsq.slipstream.persistence.Target;
import com.sixsq.slipstream.util.SerializationUtil;

//import com.sixsq.slipstream.persistence.ImageModule;
//import com.sixsq.slipstream.persistence.Module;
//import com.sixsq.slipstream.util.SerializationUtil;

public class SlipStreamRestService {
	private static String user="ioannis", password="a1s2d3f4", url="https://109.231.121.23/";

	public static Module getModule(String name) throws Exception{

		String[] command = new String[] {"curl", url+"module/"+name, "--user", user+":"+password, 
				"-H", "Content-Type: application/xml", "-k"};
		String ret = executeCommand(command);
		if(ret.startsWith("<?xml")){
			System.out.println(ret);
			Module m = (Module)SerializationUtil.fromXml(ret, ImageModule.class);
			//String xml = SerializationUtil.toXmlString(m);
			System.out.println("asdfasd!!!!!!!!!!!!!!");
			System.out.println(m.getName());
			//m.
			return m;
		}
		return null;
	}
	
	public static boolean putModule(Module module) throws IOException, InterruptedException{

		String xml = SerializationUtil.toXmlString(module);
		BufferedWriter writer = null;
		String xmlfile = "/Users/nikospapailiou/test.xml";
		
		try
		{
		    writer = new BufferedWriter( new FileWriter( xmlfile));
		    writer.write( xml);

		}
		catch ( IOException e)
		{
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		    }
		}
		String[] command = new String[] {"curl", url+"module/"+module.getName(), "--user", user+":"+password, 
				"--data", "@"+xmlfile, "-X", "PUT","-H", "Content-Type: application/xml", "-k"};
				
		
		String ret = executeCommand(command);
		if(ret.equals(""))
			return true;
		else
			return false;
	}
	
	
	
	public static String executeCommand(String command) throws IOException, InterruptedException {
		 
		StringBuffer output = new StringBuffer();
		Process p = Runtime.getRuntime().exec(command);
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";			
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");
		}
 
		return output.toString();
 
	}
	
	public static String executeCommand(String[] command) throws IOException, InterruptedException {
		System.out.print("Executing command: ");
		for (int i = 0; i < command.length; i++) {
			System.out.print(command[i]+" ");
		}
		System.out.println();
		StringBuffer output = new StringBuffer();
		Process p = Runtime.getRuntime().exec(command);
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";			
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");
		}
 
		return output.toString();
 
	}
	
	public static boolean launchApplication(String name) throws IOException, InterruptedException{
		String[] command = new String[] {"curl", url+"run", "-d", "refqname="+name, 
					"--user", user+":"+password, "-X", "POST","-H", "Content-Type: text/plain", "-k", "-D", "-"};
		System.out.println(executeCommand(command));
		return true;
	}
	
	public static String getDeploymentState(String deploymentID) throws Exception{
		String[] command = new String[] {"curl", url+"run/"+deploymentID, "--user", user+":"+password, "-k"};
		String ret = executeCommand(command);
		if(ret.startsWith("<!DOCTYPE html>")){
			return "Not Found";
		}
		else{
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			SAXStateHandler handler = new SAXStateHandler();
			parser.parse(new ByteArrayInputStream(ret.getBytes(StandardCharsets.UTF_8)),handler);
			return handler.state;
		}
	}
	
	public static void main(String[] args) {
		try {
			//System.out.println(getDeploymentState("20c30f04-b9fc-4db9-93ca-52d8297599ea"));
			//getModule("module/examples/tutorials/service-testing/apache");
			//launchApplication("module/examples/CELAR/apacheExample/apacheExample");
			
			
			
			String name = "examples/CELAR/apacheExample/apache1";
			ImageModule module = new ImageModule(name);
			module.setModuleReference("module/examples/images/ubuntu-12.04");
			module.setLoginUser("ubuntu");
			module.setPlatform("ubuntu");
			module.setDescription("Apache!! web server appliance with custom landing page.");
			Authz auth = new Authz("ioannis", module);
			module.setAuthz(auth);
			
			Set<Target> targets = new HashSet<Target>();
			Target t = new Target(Target.REPORT_TARGET, "#!/bin/sh -x cp /var/log/apache2/access.log $SLIPSTREAM_REPORT_DIR cp /var/log/apache2/error.log $SLIPSTREAM_REPORT_DIR");
			targets.add(t);

			Target t1 = new Target(Target.EXECUTE_TARGET, "#!/bin/sh -xe apt-get update -y apt-get install -y apache2 echo 'Hello from Apache deployed by SlipStream!' > /var/www/data.txt service apache2 stop port=$(ss-get port) sed -i -e 's/^Listen.*$/Listen '$port'/' /etc/apache2/ports.conf sed -i -e 's/^NameVirtualHost.*$/NameVirtualHost *:'$port'/' /etc/apache2/ports.conf sed -i -e 's/^<VirtualHost.*$/<VirtualHost *:'$port'>/' /etc/apache2/sites-available/default service apache2 start ss-set ready true");
			targets.add(t1);
			
			module.setTargets(targets);
			String parameterName = "port";
			String description = "Port";
			String value = "8080";

			ModuleParameter parameter = new ModuleParameter(parameterName, value, description, ParameterCategory.Output);
			parameter.setDefaultValue("8080");
			module.setParameter(parameter);
			

			parameterName = "Flexiant.ram";
			description = "ram";
			value = "2048";

			parameter = new ModuleParameter(parameterName, value, description, ParameterCategory.Cloud);
			parameter.setDefaultValue("2048");
			module.setParameter(parameter);
			
			
			parameterName = "Flexiant.cpu";
			description = "cpu";
			value = "1";
			//ParameterCategory category = new 

			parameter = new ModuleParameter(parameterName, value, description, ParameterCategory.Cloud);
			parameter.setDefaultValue("1");
			module.setParameter(parameter);
			
			
			System.out.println(putModule(module));
			Module m = (Module)SerializationUtil.fromXml(SerializationUtil.toXmlString(module), ImageModule.class);
			//String xml = SerializationUtil.toXmlString(m);
			System.out.println(m.getName()+" !!!!!!!!!!!!!bjkliuuil");
			
			Module m1 = getModule(name);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
