package gr.ntua.cslab.celar.slipstreamClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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

public class SlipStreamSSService {
	private String user, password, url;


	/*public Module getModule(String name) throws Exception{
		String[] command = new String[] {"ss-module-get", "-u", user, "-p", password, "--endpoint", url, name};
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
	}*/
	
	
	public boolean putModule(Module module) throws IOException, InterruptedException{

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
		String[] command = new String[] {"ss-module-put", "-u", user, "-p", password, "--endpoint", url, xmlfile};
				
		
		String ret = executeCommand(command);
		System.out.println(ret);
		if(ret.equals(""))
			return true;
		else
			return false;
	}
	
	public boolean terminateApplication(String deploymentID) throws IOException, InterruptedException{
		String[] command = new String[] {"curl", url+"/run/"+deploymentID, "--user", user+":"+password, "-X", "DELETE", "-k"};
		executeCommand(command);
		return true;
	}
	
	public String launchApplication(String name, Map<String,String> deploymentParameters) throws IOException, InterruptedException{
		String params = "";
		int i =0;
		for(Entry<String, String> e : deploymentParameters.entrySet()){
			if(i>0){
				params+=",";
			}
			params+=e.getKey()+"="+e.getValue();
			i++;
		}
		String[] command = new String[] {"ss-execute", "-u", user, "-p", password, "--endpoint", url, "--mutable-run", "--parameters", params,  name};
		String ret = executeCommand(command);
		if(ret.equals("")){
			return null;
		}
		String deploymentId = ret.substring(ret.lastIndexOf("/")+1,ret.length());
		return deploymentId;
	}
	
	public String getDeploymentState(String deploymentID) throws Exception{
		String[] command = new String[] {"curl", url+"/run/"+deploymentID, "--user", user+":"+password, "-k"};
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
	

	public void addVM(String deploymnetId, String type) throws Exception {
		String[] command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "n=1", "--user", user+":"+password,"-X", "POST", "-H", "Content-Type: text/plain", "-k", "-D", "-"};
		executeCommand(command);
	}

	public void removeVM(String deploymnetId, String type, String id) throws Exception {
		String[] command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+id, "--user", user+":"+password,"-X", "DELETE", "-k", "-D", "-"};
		executeCommand(command);
	}

	public void waitForReadyState(String deploymnetId) throws Exception {
		while(true){
			String state = getDeploymentState(deploymnetId);
			System.out.println(state);
			if(state.equals("Ready"))
				break;
			Thread.sleep(1000);
		}
	}
	
	public String executeCommand(String command) throws IOException, InterruptedException {
		 
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
	
	public String executeCommand(String[] command) throws IOException, InterruptedException {
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

	public SlipStreamSSService(String user, String password, String url) {
		super();
		this.user = user;
		this.password = password;
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}




}
