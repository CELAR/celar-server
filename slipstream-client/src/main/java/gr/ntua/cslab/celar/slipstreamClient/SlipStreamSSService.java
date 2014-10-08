package gr.ntua.cslab.celar.slipstreamClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.CloudImageIdentifier;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.ProjectModule;
import com.sixsq.slipstream.persistence.User;
import com.sixsq.slipstream.util.SerializationUtil;

//import com.sixsq.slipstream.persistence.ImageModule;
//import com.sixsq.slipstream.persistence.Module;
//import com.sixsq.slipstream.util.SerializationUtil;

public class SlipStreamSSService {
	private String user, password, url;
    public  Logger logger = Logger.getLogger(SlipStreamSSService.class);
    public HashMap<String,String> baseImageReferences; //imageName-reference
    public HashMap<String,HashMap<String,String>> baseImages; //imageName-cloud-flavorID
    public List<ModuleParameter> baseParameters;


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
    
	private String writeXML(String xml) throws IOException{
		logger.debug(xml);
		BufferedWriter writer = null;
		String xmlfile = "/tmp/test.xml";
		
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
		    	throw e;
		    }
		}
		return xmlfile;
	}
	
	public boolean putUser(User user) throws Exception{
		logger.info("Putting user: "+ user.getName());
		String xml = SerializationUtil.toXmlString(user);
		String xmlfile = writeXML(xml);
		String[] command = new String[] {"ss-user-put", "-u", this.user, "-p", password, "--endpoint", url, xmlfile};
		String ret = executeCommand(command);
		if(ret.equals(""))
			return true;
		else
			return false;
	}
	
	public boolean putModule(Module module) throws IOException, InterruptedException{
		logger.info("Putting "+module.getClass() +" module: "+ module.getName());
		String xml = SerializationUtil.toXmlString(module);
		String xmlfile = writeXML(xml);
		String[] command = new String[] {"ss-module-put", "-u", user, "-p", password, "--endpoint", url, xmlfile};
		String ret = executeCommand(command);
		if(ret.equals(""))
			return true;
		else
			return false;
	}
	
	public boolean terminateApplication(String deploymentID) throws IOException, InterruptedException{
		logger.info("Terminating deployment: "+deploymentID);
		String[] command = new String[] {"curl", url+"/run/"+deploymentID, "--user", user+":"+password, "-X", "DELETE", "-k"};
		String ret = executeCommand(command);
		return true;
	}
	
	public String launchApplication(String name, Map<String,String> deploymentParameters) throws IOException, InterruptedException{
		if(deploymentParameters.size()==0){
			logger.info("Launching application: "+name+" without parameters");
			String[] command = new String[] {"ss-execute", "-u", user, "-p", password, "--endpoint", url, "--mutable-run", name};
			String ret = executeCommand(command);
			if(ret.equals("")){
				return null;
			}
			String deploymentId = ret.substring(ret.lastIndexOf("/")+1,ret.length());
			return deploymentId;
		}
		else{
			String params = "";
			int i =0;
			for(Entry<String, String> e : deploymentParameters.entrySet()){
				if(i>0){
					params+=",";
				}
				params+=e.getKey()+"="+e.getValue();
				i++;
			}
			logger.info("Launching application: "+name+" with parameters: "+params);
			String[] command = new String[] {"ss-execute", "-u", user, "-p", password, "--endpoint", url, "--mutable-run", "--parameters", params,  name};
			String ret = executeCommand(command);
			if(ret.equals("")){
				return null;
			}
			String deploymentId = ret.substring(ret.lastIndexOf("/")+1,ret.length());
			deploymentId = deploymentId.replaceAll("(\\r|\\n|\\t)", "");
			return deploymentId;
		}
	}
	
	public String getDeploymentState(String deploymentID) throws Exception{
		logger.info("Getting deployment state for deploymentID: "+deploymentID);
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
	

	public String addVM(String deploymnetId, String type, Integer number) throws Exception {
		logger.info("Adding "+number+" vms: "+type+" to deployment: "+deploymnetId);
		String[] command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "n="+number, "--user", user+":"+password,"-X", "POST", "-H", "Content-Type: text/plain", "-k", "-D", "-"};
		return executeCommand(command);
	}

	public void removeVM(String deploymnetId, String type, String ids) throws Exception {
		logger.info("Removing vm: "+type+"."+ids+" from deployment: "+deploymnetId);
		String[] command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+ids, "--user", user+":"+password,"-X", "DELETE", "-k", "-D", "-"};
		executeCommand(command);
	}

	public void waitForReadyState(String deploymnetId) throws Exception {
		logger.info("Waiting for ready state deploymentID: "+deploymnetId);
		while(true){
			String state = getDeploymentState(deploymnetId);
			logger.info("Current State: "+state);
			if(state.equals("Ready"))
				break;
			Thread.sleep(1000);
		}
	}
	

	public String createApplication(String appName, String appVersion) throws Exception {
		ProjectModule project = new ProjectModule(appName);
		Authz auth = new Authz(getUser(), project);
		project.setAuthz(auth);
		putModule(project);
		return appName;
	}
	
	public String getImageReference(String imageName) throws Exception {
		String reference = baseImageReferences.get(imageName);
		if(reference!=null)
			return reference;
		
		String projectName = "images";
		ProjectModule project = new ProjectModule(projectName);
		Authz auth = new Authz(getUser(), project);
		project.setAuthz(auth);
		putModule(project);
		
		reference ="images/"+imageName;
		ImageModule module = new ImageModule(reference);
		module.setIsBase(true);
		module.setLoginUser("ubuntu");
		module.setPlatform("ubuntu");
		module.setDescription("Baseline Image "+imageName);
		auth = new Authz(getUser(), module);
		module.setAuthz(auth);
		HashMap<String, String> imageIds = baseImages.get(imageName);
		if(imageIds==null){
			logger.error("No imageIDs for image with name: "+imageName);
			throw new Exception("No imageIDs for image with name: "+imageName);
		}
		
		Set<CloudImageIdentifier> cloudImageIdentifiers = new HashSet<CloudImageIdentifier>();
		for(Entry<String, String> e : imageIds.entrySet()){
			CloudImageIdentifier ident = new CloudImageIdentifier(module, e.getKey(), e.getValue());
			cloudImageIdentifiers.add(ident);
		}
		module.setCloudImageIdentifiers(cloudImageIdentifiers );
		
		for(ModuleParameter p : baseParameters){
			module.setParameter(p);
		}
		putModule(module);
		String ref = "module/"+reference;
		baseImageReferences.put(imageName, ref);
		return ref;
	}
	
	public String executeCommand(String[] command) throws IOException, InterruptedException {
		String c="Executing command: ";
		for (int i = 0; i < command.length; i++) {
			c+=command[i]+" ";
		}
		logger.info(c);
		StringBuffer output = new StringBuffer();
		Process p = Runtime.getRuntime().exec(command);
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";			
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");
		}
        logger.info("Command Output: "+output.toString());
		return output.toString();
 
	}

	public SlipStreamSSService(String user, String password, String url) throws ValidationException {
		super();
		this.user = user;
		this.password = password;
		this.url = url;
		baseImageReferences = new HashMap<String,String>();
		baseImages = new HashMap<>();
		HashMap<String,String> temp = new HashMap<String, String>();
		temp.put("Flexiant", "81aef2d3-0291-38ef-b53a-22fcd5418e60");
		temp.put("okeanos", "fe31fced-a3cf-49c6-b43b-f58f5235ba45");
		temp.put("stratuslab", "HZTKYZgX7XzSokCHMB60lS0wsiv");
		baseImages.put("ubuntu-12.04", temp);
		
		baseParameters = new ArrayList<ModuleParameter>();
		String parameterName = "Flexiant.ram";
		String description = "ram";
		String value = "2048";
	
		ModuleParameter parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("2048");
		baseParameters.add(parameter);
		
		parameterName = "Flexiant.cpu";
		description = "cpu";
		value = "2";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("Flexiant");
		parameter.setDefaultValue("2");
		baseParameters.add(parameter);

        parameterName = "okeanos.instance.type";
        description = "Flavor";
        value = "C2R2048D10ext_vlmc";

        parameter = new ModuleParameter(parameterName, value, description);
        parameter.setCategory("okeanos");
        parameter.setDefaultValue("C2R2048D10ext_vlmc");
		baseParameters.add(parameter);

        parameterName = "okeanos.security.groups";
        description = "Security Groups (comma separated list)";
        value = "default";

        parameter = new ModuleParameter(parameterName, value, description);
        parameter.setCategory("okeanos");
        parameter.setDefaultValue("default");
		baseParameters.add(parameter);
		
		parameterName = "ready";
        description = "Server ready";

        parameter = new ModuleParameter(parameterName, "", description);
        parameter.setCategory("Output");
        baseParameters.add(parameter);

        parameterName = "loaded";
        description = "Data loaded";

        parameter = new ModuleParameter(parameterName, "", description);
        parameter.setCategory("Output");
        baseParameters.add(parameter);
		
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
