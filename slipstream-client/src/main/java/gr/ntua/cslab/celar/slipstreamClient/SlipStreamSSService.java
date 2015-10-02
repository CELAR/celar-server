package gr.ntua.cslab.celar.slipstreamClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.net.ssl.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sixsq.slipstream.exceptions.ValidationException;
import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.CloudImageIdentifier;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.Module;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.ProjectModule;
import com.sixsq.slipstream.persistence.Target;
import com.sixsq.slipstream.persistence.User;
import com.sixsq.slipstream.statemachine.States;
import com.sixsq.slipstream.util.SerializationUtil;

import static gr.ntua.cslab.celar.slipstreamClient.SSXMLParser.parse;


public class SlipStreamSSService {
	private String user, password, url, cookie, cookieFile, connectorName;


	private boolean cookieAuth;
    public  Logger logger = Logger.getLogger(SlipStreamSSService.class);
    public HashMap<String,String> baseImageReferences; //imageName-reference
    public HashMap<String,HashMap<String,String>> baseImages; //imageName-cloud-flavorID
    public List<ModuleParameter> baseParameters;
    private static String preScaleScript=
    		"#!/bin/bash\n"
    		+ "set -e\n\n"
    		+ "# Pre-scale: intended to be ran before any vertical scaling and horizontal downscaling action. \n\n"
    		+ "function before_vm_remove() { echo \"Before VM remove\";\n $remove \n}\n"
    		+ "function before_vm_resize() { echo \"Before VM resize\";\n $resize \n}\n"
    		+ "function before_disk_attach() { echo \"Before disk attach\";\n $disk_attach \n}\n"
    		+ "function before_disk_detach() { echo \"Before disk detach\";\n $disk_detach \n}\n\n"
    		+ "case $SLIPSTREAM_SCALING_ACTION in\n"
    		+ "vm_remove)\n"
    		+ "before_vm_remove ;;\n"
    		+ "vm_resize)\n"
    		+ "before_vm_resize ;;\n"
    		+ "disk_attach)\n"
    		+ "before_disk_attach ;;\n"
    		+ "disk_detach)\n"
    		+ "before_disk_detach ;;\n"
    		+ "esac\n";
    
    private static String postScaleScript=
    		"#!/bin/bash\n"
    		+ "set -e\n\n"
    		+ "# Post-Scale: intended to be ran after vertical scaling action. \n\n"
    		+ "function after_vm_resize() { echo \"After VM resize\";\n $resize \n}\n"
    		+ "function after_disk_attach() { echo \"After disk attach\";\n $disk_attach \n}\n"
    		+ "function after_disk_detach() { echo \"After disk detach\";\n $disk_detach \n}\n\n"
    		+ "case $SLIPSTREAM_SCALING_ACTION in\n"
    		+ "vm_resize)\n"
    		+ "after_vm_resize ;;\n"
    		+ "disk_attach)\n"
    		+ "after_disk_attach ;;\n"
    		+ "disk_detach)\n"
    		+ "after_disk_detach ;;\n"
    		+ "esac\n";

	/*public Module getModule(String name) throws Exception{
		String[] command = new String[] {"ss-module-get", "-u", user, "-p", password, "--endpoint", url, name};
		String ret = executeCommand(command);
		if(ret.startsWith("<?xml")){
			System.out.println(ret);
			Module m = (Module)SerializationUtil.fromXml(ret, ImageModule.class);
			//String xml = SerializationUtil.toXmlString(m);
			System.out.println(m.getName());
			//m.
			return m;
		}
		return null;
	}*/
	public String writeToFile(String script) throws IOException{
		logger.info("writing file:" + script);
		BufferedWriter writer = null;
		String file = "/tmp/script.sh";
		
		try
		{
		    writer = new BufferedWriter( new FileWriter(file));
		    writer.write( script);

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
		return file;
	}
	
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
	
	private void writeCookie(String cookie) throws IOException{
		logger.debug("Writing cookie");
		BufferedWriter writer = null;
		this.cookieFile = "/tmp/slipstream-"+System.currentTimeMillis()+".cookie";
		
		try
		{
		    writer = new BufferedWriter( new FileWriter( this.cookieFile));
		    writer.write("cookie = ");
		    writer.write( cookie);

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
	}
	
	public boolean putUser(User user) throws Exception{
		logger.info("Putting user: "+ user.getName());
		String xml = SerializationUtil.toXmlString(user);
		String xmlfile = writeXML(xml);
		String[] command;
		if(cookieAuth){
			command = new String[] {"ss-user-put", "-u", this.user, "--cookie="+cookieFile, "--endpoint", url, xmlfile};
		}
		else{	
			command = new String[] {"ss-user-put", "-u", this.user, "-p", password, "--endpoint", url, xmlfile};
		}
		Map<String, String> ret = executeCommand(command);
		if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}
		return true;
	}
	
	public boolean putModule(Module module) throws Exception{
		logger.info("Putting "+module.getClass() +" module: "+ module.getName());
		String xml = SerializationUtil.toXmlString(module);
		logger.debug(xml);
		String xmlfile = writeXML(xml);
		String[] command;
		if(cookieAuth){
			command = new String[] {"ss-module-put", "-u", user, "--cookie="+cookieFile, "--endpoint", url, xmlfile};
		}
		else{	
			command = new String[] {"ss-module-put", "-u", user, "-p", password, "--endpoint", url, xmlfile};
		}
		Map<String, String>  ret = executeCommand(command);
		if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}
		return true;
	}
	
	public boolean terminateApplication(String deploymentID) throws Exception{
		logger.info("Terminating deployment: "+deploymentID);
		String[] command;
		if(cookieAuth){
			command = new String[] {"curl", url+"/run/"+deploymentID, "--cookie", cookie, "-X", "DELETE", "-k"};
		}
		else{	
			command = new String[] {"curl", url+"/run/"+deploymentID, "--user", user+":"+password, "-X", "DELETE", "-k"};
		}
		Map<String, String>  ret = executeCommand(command);

		/*if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}*/
		return true;
	}
	
	public String launchApplication(String name, Map<String,String> deploymentParameters) throws Exception{
		String[] command;
		if(deploymentParameters.size()==0){
			logger.info("Launching application: "+name+" without parameters");
			if(cookieAuth){
				command = new String[] {"ss-execute", "-u", user, "--cookie="+cookieFile, "--endpoint", url, "--mutable-run", name};
			}
			else{
				command = new String[] {"ss-execute", "-u", user, "-p", password, "--endpoint", url, "--mutable-run", name};
			}
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
			if(cookieAuth){
				command = new String[] {"ss-execute", "-u", user, "--cookie="+cookieFile, "--endpoint", url, "--mutable-run", "--parameters", params,  name};
			}
			else{
				command = new String[] {"ss-execute", "-u", user, "-p", password, "--endpoint", url, "--mutable-run", "--parameters", params,  name};
			}
		}
		Map<String, String> ret = executeCommand(command);
		if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}
		String r = ret.get("output");
		String deploymentId = r.substring(r.lastIndexOf("/")+1,r.length());
		deploymentId = deploymentId.replaceAll("(\\r|\\n|\\t)", "");
		logger.info("deploymentId: "+deploymentId);
		return deploymentId;
	}

	public States getDeploymentState(String deploymentID) throws Exception{
		logger.info("Getting deployment state for deploymentID: "+deploymentID);
		//String[] command = new String[] {"ss-run-get", "--endpoint", url, "-u", user, "-p", password, deploymentID};
		//String[] command = new String[] {"curl", url+"/run/"+deploymentID, "--user", user+":"+password, "-k"};
		//String ret = executeCommand(command);
		String ret = httpsGet(url+"/run/"+deploymentID+"?media=xml");
		if(ret.startsWith("<!DOCTYPE html>")){
			return States.Unknown;
		}
		else{
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			SAXStateHandler handler = new SAXStateHandler();
			parser.parse(new ByteArrayInputStream(ret.getBytes(StandardCharsets.UTF_8)),handler);
			return handler.state;
		}
	}	

	public HashMap<String,String> getDeploymentIPs(String deploymentID) throws Exception{
		logger.info("Getting deployment ips for deploymentID: "+deploymentID);
		logger.info("URL: "+url+"/run/"+deploymentID+"?media=xml");
		String ret = httpsGet(url+"/run/"+deploymentID+"?media=xml");
		if(ret.startsWith("<!DOCTYPE html>")){
			return new HashMap<>();
		}
		else{
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			SAXStateHandler handler = new SAXStateHandler();
			parser.parse(new ByteArrayInputStream(ret.getBytes(StandardCharsets.UTF_8)),handler);
			logger.info(handler.getIps());
			return handler.getIps();
		}
	}	
	
	private String httpsGet(String urlLink) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { 
	      new X509TrustManager() {
	        public X509Certificate[] getAcceptedIssuers() { 
	          return new X509Certificate[0]; 
	        }
	        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	    }};

	    // Ignore differences between given hostname and certificate hostname
	    HostnameVerifier hv = new HostnameVerifier() {
	      public boolean verify(String hostname, SSLSession session) { return true; }
	    };
	    // Install the all-trusting trust manager
	    SSLContext sc = SSLContext.getInstance("SSL");
	    sc.init(null, trustAllCerts, new SecureRandom());

	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    HttpsURLConnection.setDefaultHostnameVerifier(hv);
		SSLSocketFactory sslsocketfactory = sc.getSocketFactory();//(SSLSocketFactory) SSLSocketFactory.getDefault();
		URL url1 = new URL(urlLink);
		HttpsURLConnection conn = (HttpsURLConnection)url1.openConnection();
		conn.setSSLSocketFactory(sslsocketfactory);

		if(cookieAuth){
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		else{
			String userpass = user + ":" + password;
			String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
			conn.setRequestProperty ("Authorization", basicAuth);
		}
		InputStream inputStream = conn.getInputStream();
		return IOUtils.toString(inputStream, "UTF-8");
	}
	
	public void attachDisk(String deploymnetId, String type, String id, Integer gb) throws Exception {
		logger.info("Attaching disk vm: "+type+"."+id+" from deployment: "+deploymnetId+" disk: "+ gb+"GB");
		String[] command;
		if(cookieAuth){
			command = new String[] {};
		}
		else{	
			//command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+ids, "--user", user+":"+password,"-X", "DELETE", "-k", "-D", "-"};
			command = new String[] {"ss-scale-disk", "--endpoint", url, "-u", user, "-p", password, "--attach", gb+"", deploymnetId, type, id};
		}
		Map<String, String> ret = executeCommand(command);
		/*if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}*/
	}
	
	public void detachDisk(String deploymnetId, String type, String id, String diskId) throws Exception {
		logger.info("Detaching disk vm: "+type+"."+id+" from deployment: "+deploymnetId+" disk id: "+ diskId);
		String[] command;
		if(cookieAuth){
			command = new String[] {};
		}
		else{	
			//command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+ids, "--user", user+":"+password,"-X", "DELETE", "-k", "-D", "-"};
			command = new String[] {"ss-scale-disk", "--endpoint", url, "-u", user, "-p", password, "--detach", diskId+"", deploymnetId, type, id};
		}
		Map<String, String> ret = executeCommand(command);
		/*if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}*/
	}
	
	public void scaleVM(String deploymnetId, String type, String id, Integer cpu, Integer ram) throws Exception {
		logger.info("Scaling vm: "+type+"."+id+" from deployment: "+deploymnetId+" new cpu: "+ cpu+" ram: "+ram);
		String[] command;
		if(cookieAuth){
			command = new String[] {};
		}
		else{	
			//command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+ids, "--user", user+":"+password,"-X", "DELETE", "-k", "-D", "-"};
			command = new String[] {"ss-scale-resize", "--endpoint", url, "-u", user, "-p", password, "--cpu", cpu+"","--ram",ram+"", deploymnetId, type, id};
		}
		Map<String, String> ret = executeCommand(command);
		/*if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}*/
	}

	public void scaleVM(String deploymnetId, String type, String id, String flavor) throws Exception {
		logger.info("Scaling vm: "+type+"."+id+" from deployment: "+deploymnetId+" new flavor: "+flavor);
		String[] command;
		if(cookieAuth){
			command = new String[] {};
		}
		else{	
			//command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+ids, "--user", user+":"+password,"-X", "DELETE", "-k", "-D", "-"};
			command = new String[] {"ss-scale-resize", "--endpoint", url, "-u", user, "-p", password, "--instance-type", flavor, deploymnetId, type, id};
		}
		Map<String, String> ret = executeCommand(command);
		/*if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}*/
	}
	
	public void addVM(String deploymentId, String type, Integer number, Integer cores, Integer ram, Integer disk) throws Exception {
		logger.info(String.format("Adding %d VMs of type %s to deployment %s of flavor (%d cores, %d RAM, %d disk)", 
				number,
				type,
				deploymentId,
				cores,
				ram,
				disk));
		String command;
		if(cookieAuth) {
			command=String.format("ss-node-add --cookie %s --endpoint %s %s %s %d --runtime-parameter %s", 
					cookieFile,
					url,
					deploymentId,
					type,
					number,
					calculateFlavorParameter(cores, ram, disk));
		} else {
			command=String.format("ss-node-add -u %s -p %s --endpoint %s %s %s %d --runtime-parameter %s", 
					user,
					password,
					url,
					deploymentId,
					type,
					number,
					calculateFlavorParameter(cores, ram, disk));
		}
		Map<String, String> ret = executeCommand(command.split(" "));
		logger.info("Returned: "+ret.toString());
	}
	private String calculateFlavorParameter(Integer cores, Integer ram, Integer disk) {
		if(getConnectorName().equals("okeanos")) {
			return "okeanos.instance.type:C"+cores+"R"+ram+"D"+disk+"drbd";
		} else {		// TODO: implement Flexiant flavor creation
			
		}
		return null;
	}

	// ss-node-add -u celar -p celar2015 --endpoint https://83.212.102.166 0e54569b-6b66-420d-b7c8-bcf247837445 Worker 1 --runtime-parameter okeanos.instance.type:C2R4096D20drbd
	public String addVM(String deploymnetId, String type, Integer number) throws Exception {
		logger.info("Adding "+number+" vms: "+type+" to deployment: "+deploymnetId);
		String[] command;
		if(cookieAuth){
			command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "n="+number, "--cookie", cookie,"-X", "POST", "-H", "Content-Type: text/plain", "-k", "-D", "-"};
		}
		else{	
			command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "n="+number, "--user", user+":"+password,"-X", "POST", "-H", "Content-Type: text/plain", "-k", "-D", "-"};
		}
		Map<String, String> ret = executeCommand(command);
		/*if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}*/
		return ret.get("output");
	}

	public void removeVM(String deploymnetId, String type, String ids) throws Exception {
		logger.info("Removing vm: "+type+"."+ids+" from deployment: "+deploymnetId);
		String[] command;
		if(cookieAuth){
			command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+ids, "--cookie", cookie,"-X", "DELETE", "-k", "-D", "-"};
		}
		else{	
			//command = new String[] {"curl", url+"/run/"+deploymnetId+"/"+type, "-d", "ids="+ids, "--user", user+":"+password,"-X", "DELETE", "-k", "-D", "-"};
			command = new String[] {"ss-node-remove", "--endpoint", url, "-u", user, "-p", password, deploymnetId, type, ids};
		}
		Map<String, String> ret = executeCommand(command);
		/*if(!ret.get("error").equals("")){
			throw new Exception(ret.get("error"));
		}*/
	}
        
        /**
         * Remove a number of VMs from a specific node type
         * @param deploymnetId the unique Id of the deployment
         * @param type the node type
         * @param number the number of the VMs to be removed
         * @throws Exception 
         */
        public void removeVM(String deploymnetId, String type, int number) throws Exception {
//		logger.info("Removing vm: "+type+"."+ids+" from deployment: "+deploymnetId);
            HashMap<String,String> ips = this.getDeploymentIPs(deploymnetId);
            List<String> vmsToBeDeleted = new ArrayList<>(number);
            for(String vm : ips.keySet()) {
                if(vm.startsWith(type) && vmsToBeDeleted.size()<number) {
                    vmsToBeDeleted.add(vm.split(":")[0]); // keeping only the identifier, no the "hostname" keyword
                }
            }
            String ids = "";
            for(int i=0;i<number;i++) {
            	String id =vmsToBeDeleted.get(i);
            	String s = id.substring(id.indexOf('.')+1, id.length());
            	System.out.println(s);
                ids+=s;
                if(i!=number-1) {
                    ids+=" ";
                }
            }
            this.removeVM(deploymnetId, type, ids);
        }
        
        public List<String> removeVMIDs(String deploymnetId, String type, int number) throws Exception {
//    		logger.info("Removing vm: "+type+"."+ids+" from deployment: "+deploymnetId);
                HashMap<String,String> ips = this.getDeploymentIPs(deploymnetId);
                List<String> vmsToBeDeleted = new ArrayList<>(number);
                for(Entry<String,String> vm : ips.entrySet()) {
                    if(vm.getKey().startsWith(type) && vmsToBeDeleted.size()<number) {
                        vmsToBeDeleted.add(vm.getKey().split(":")[0]); // keeping only the identifier, no the "hostname" keyword
                    }
                }
                return vmsToBeDeleted;
    	}
        
        public void removeVMswithIDs(String deploymnetId, List<String> vmsToBeDeleted, String type) throws Exception {
            String ids = "";
            int number = vmsToBeDeleted.size();
            for(int i=0;i<number;i++) {
            	String id =vmsToBeDeleted.get(i);
            	String s = id.substring(id.indexOf('.')+1, id.length());
            	logger.info("ID: "+s);
                ids+=s;
                if(i!=number-1) {
                    ids+=" ";
                }
            }
            this.removeVM(deploymnetId, type, ids);
    	}
        
        public List<String> translateIPs(String deploymnetId, List<String> ids) throws Exception {
                HashMap<String,String> ips = this.getDeploymentIPs(deploymnetId);
                List<String> ret = new ArrayList<String>();
                for(String key : ids) {
                	ret.add(ips.get(key+":hostname"));
                }
                return ret;
    	}
        
        
	public void waitForReadyState(String deploymnetId) throws Exception {
		logger.info("Waiting for ready state deploymentID: "+deploymnetId);
		while(true){
			States state = getDeploymentState(deploymnetId);
			logger.info("Current State: "+state);
			if(state.equals(States.Ready))
				break;
			Thread.sleep(10000);
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
		logger.info("Getting image reference: "+imageName);
		String reference = baseImageReferences.get(imageName);
		if(reference!=null)
			return reference;
		
		String projectName = user+"_images";
		ProjectModule project = new ProjectModule(projectName);
		Authz auth = new Authz(getUser(), project);
		project.setAuthz(auth);
		putModule(project);
		
		reference = projectName+"/"+imageName;
		ImageModule module = new ImageModule(reference);
		module.setIsBase(true);
        if(getConnectorName().equals("okeanos")){
        	module.setLoginUser("root");
        }
        else{
    		module.setLoginUser("ubuntu");
        }
		module.setPlatform("ubuntu");
		module.setDescription("Baseline Image "+imageName);
		auth = new Authz(getUser(), module);
		module.setAuthz(auth);
		HashMap<String, String> imageIds = baseImages.get(imageName);
		if(imageIds==null){
			imageIds = new HashMap<String, String>();
			imageIds.put(connectorName, imageName);
			imageIds.put("okeanos", imageName);
			//logger.error("No imageIDs for image with name: "+imageName);
			//throw new Exception("No imageIDs for image with name: "+imageName);
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
	
	public Map<String,String> executeCommand(String[] command) throws IOException, InterruptedException {
		String c="Executing command: ";
		for (int i = 0; i < command.length; i++) {
			c+=command[i]+" ";
		}
		logger.info(c);
		
		StringBuffer output = new StringBuffer();
		ProcessBuilder p = new ProcessBuilder(command);
		Process p1 = p.start();
		//Process p = Runtime.getRuntime().exec(command);
		p1.waitFor();
		Map<String,String> ret = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
		String line = "";			
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");
		}
        logger.info("Command Output: "+output.toString());
        ret.put("output", output.toString());
		reader = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
		line = "";	
		output = new StringBuffer();		
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");
		}
        logger.info("Command Error: "+output.toString());
        ret.put("error", output.toString());
		return ret;
 
	}

	public SlipStreamSSService(String user, String password, String url) throws ValidationException {
		super();
		logger.info("Init ssService user: "+user+" password: "+password+" url: "+url);
		this.user = user;
		this.password = password;
		this.url = url;
		this.connectorName="Flexiant";
		init();
	}
	
	public SlipStreamSSService(String user, String password, String url, String connectorName) throws ValidationException {
		super();
		logger.info("Init ssService user: "+user+" password: "+password+" url: "+url);
		this.user = user;
		this.password = password;
		this.url = url;
		this.connectorName=connectorName;
		init();
	}
	
	
	public SlipStreamSSService(String user, String cookie, String url, Boolean cookieAuth) throws Exception {
		super();
		this.user = user;
		this.cookie = cookie;
		writeCookie(cookie);
		this.cookieAuth = true;
		this.url = url;
		init();
	}
	
	private void init() throws ValidationException{

		baseImageReferences = new HashMap<String,String>();
		baseImages = new HashMap<>();
		HashMap<String,String> temp = new HashMap<String, String>();
		temp.put(connectorName, "81aef2d3-0291-38ef-b53a-22fcd5418e60");
		temp.put("okeanos", "fe31fced-a3cf-49c6-b43b-f58f5235ba45");
		temp.put("stratuslab", "HZTKYZgX7XzSokCHMB60lS0wsiv");
		baseImages.put("ubuntu-12.04", temp);
		baseImages.put("Ubuntu 12.04.1 LTS", temp);
		baseImages.put("Ubuntu12.04.1LTS", temp);
		
		
		
		baseParameters = new ArrayList<ModuleParameter>();
		/*String parameterName = "Flexiant.ram";
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
        baseParameters.add(parameter);*/
	}
	
	public String patchExecuteScript(String script){
		String jcatascopiaInit = "#!/bin/bash \n"
				+ "ip=$(ss-get hostname) \n"
				+ "hostname=$(hostname) \n"
				+ "echo $ip $hostname >> /etc/hosts \n"
				+ "SERVER_IP=$(ss-get orchestrator-"+connectorName+":hostname) \n"
				+ "CELAR_REPO=http://snf-175960.vm.okeanos.grnet.gr \n"
				+ "JC_VERSION=LATEST \n"
				+ "JC_ARTIFACT=JCatascopia-Agent \n"
				+ "JC_GROUP=eu.celarcloud.cloud-ms \n"
				+ "JC_TYPE=tar.gz \n"
				+ "DISTRO=$(eval cat /etc/*release) \n"
				+ "if [[ \"$DISTRO\" == *Ubuntu* ]]; then \n"
				+ "        apt-get update -y \n"
				+ "        #download and install java \n"
				+ "        apt-get install -y openjdk-7-jre-headless \n"
				+ "fi \n"
				+ "if [[ \"$DISTRO\" == *CentOS* ]]; then \n"
				+ "        yum -y update \n"
				+ "        yum install -y wget \n"
				+ "        #download and install java \n"
				+ "        yum -y install java-1.7.0-openjdk \n"
				+ "fi \n"
				+ "#download,install and start jcatascopia agent... \n"
				+ "URL=\"$CELAR_REPO/nexus/service/local/artifact/maven/redirect?r=snapshots&g=$JC_GROUP&a=$JC_ARTIFACT&v=$JC_VERSION&p=$JC_TYPE\" \n"
				+ "wget -O JCatascopia-Agent.tar.gz $URL \n"
				+ "tar xvfz JCatascopia-Agent.tar.gz \n"
				+ "eval \"sed -i 's/server_ip=.*/server_ip=$SERVER_IP/g' JCatascopia-Agent-*/JCatascopiaAgentDir/resources/agent.properties\" \n"
				+ "cd JCatascopia-Agent-* \n"
				+ "./installer.sh \n"
				+ "cd .. \n"
				+ "/etc/init.d/JCatascopia-Agent restart \n";
		
		return jcatascopiaInit+script;
	}
	
	public List<ModuleParameter> getOutputParamsFromScript(String script) throws ValidationException{
		List<ModuleParameter> ret = new ArrayList<ModuleParameter>();
		String[] s = script.split("ss-set ");
		for (int i = 1; i < s.length; i++) {
			String param = s[i].substring(0,s[i].indexOf(" "));
			String parameterName = param;
	        String description = param;

	        ModuleParameter parameter = new ModuleParameter(parameterName, "", description);
	        parameter.setCategory("Output");
			ret.add(parameter);
		}
		return ret;
	}
	
	public List<ModuleParameter> createFlavorParameters(String flavor)throws ValidationException{
		List<ModuleParameter> ret = new ArrayList<ModuleParameter>();
    	String cpu="", ram="", disk="";
    	String[] fl = flavor.split(" ");
    	for (int i = 0; i < fl.length; i++) {
			String[] f = fl[i].split(":");
			switch (f[0]) {
			case "vcpus":
				cpu=f[1];
				break;
			case "ram":
				ram=f[1];
				break;
			case "disk":
				disk=f[1];
				break;

			default:
				break;
			}
		}
    	String okeanosFlavor = "C"+cpu+"R"+ram+"D"+disk+"drbd";
    	
    	logger.info("Okeanos flavor: "+okeanosFlavor);
    	
    	String parameterName = "okeanos.instance.type";
    	String description = "Flavor";
    	String value = okeanosFlavor;
	
    	ModuleParameter parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("okeanos");
		parameter.setDefaultValue(okeanosFlavor);
		ret.add(parameter);
		
		parameterName = "okeanos.security.groups";
		description = "Security Groups (comma separated list)";
		value = "default";
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory("okeanos");
		parameter.setDefaultValue("default");
		ret.add(parameter);
		
		parameterName = connectorName+".ram";
		description = "ram";
		value = ram;
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory(connectorName);
		parameter.setDefaultValue(ram);
		ret.add(parameter);
		
		parameterName = connectorName+".cpu";
		description = "cpu";
		value = cpu;
	
		parameter = new ModuleParameter(parameterName, value, description);
		parameter.setCategory(connectorName);
		parameter.setDefaultValue(cpu);
		ret.add(parameter);
		
		return ret;
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

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}
        
    public Map<String,String> getAllRuntimeParams(String deploymentId) throws Exception{
        String ret= httpsGet(getUrl()+"/run/"+deploymentId+"?media=xml");
        return parse(ret);
    }

	public void generateTargetScripts(HashMap<String, String> scripts,
			Set<Target> targets) {
		String preScale = new String(preScaleScript);
		String postScale = new String(postScaleScript);
		String postOnVmAdd = "";
		String postOnVmRemove = "";
		for(Entry<String, String> script: scripts.entrySet()){
			 if(script.getKey().contains("scaleOut") && script.getKey().contains("Pre")){
				 //preScale = preScale.replace("$remove", script.getValue());
			 }
			 else if(script.getKey().contains("scaleOut") && (script.getKey().contains("Post") || script.getKey().contains("Lifecycle"))){
				 postOnVmAdd = script.getValue();
			 }
			 else if(script.getKey().contains("scaleIn") && script.getKey().contains("Pre")){
				 preScale = preScale.replace("$remove", script.getValue());
			 }
			 else if(script.getKey().contains("scaleIn") && (script.getKey().contains("Post") || script.getKey().contains("Lifecycle"))){
				 postOnVmRemove = script.getValue();
			 }
			 else if(script.getKey().contains("vmResize") && script.getKey().contains("Pre")){
				 preScale = preScale.replace("$resize", script.getValue());
			 }
			 else if(script.getKey().contains("vmResize") && (script.getKey().contains("Post") || script.getKey().contains("Lifecycle"))){
				 postScale = postScale.replace("$resize", script.getValue());
			 }
			 else if(script.getKey().contains("attachDisk") && script.getKey().contains("Pre")){
				 preScale = preScale.replace("$disk_attach", script.getValue());
			 }
			 else if(script.getKey().contains("attachDisk") && (script.getKey().contains("Post") || script.getKey().contains("Lifecycle"))){
				 postScale = postScale.replace("$disk_attach", script.getValue());
			 }
			 else if(script.getKey().contains("detachDisk") && script.getKey().contains("Pre")){
				 preScale = preScale.replace("$disk_detach", script.getValue());
			 }
			 else if(script.getKey().contains("detachDisk") && (script.getKey().contains("Post") || script.getKey().contains("Lifecycle"))){
				 postScale = postScale.replace("$disk_detach", script.getValue());
			 }
		}
		postScale = postScale.replace("$resize", "");
		postScale = postScale.replace("$disk_attach", "");
		postScale = postScale.replace("$disk_detach", "");
		preScale = preScale.replace("$remove", "");
		preScale = preScale.replace("$resize", "");
		preScale = preScale.replace("$disk_attach", "");
		preScale = preScale.replace("$disk_detach", "");
		logger.debug("---------------------------------------------preScale---------------------------------------------");
		logger.debug(preScale);
		logger.debug("---------------------------------------------postScale---------------------------------------------");
		logger.debug(postScale);
		logger.debug("---------------------------------------------postOnVmAdd---------------------------------------------");
		logger.debug(postOnVmAdd);
		logger.debug("---------------------------------------------postOnVmRemove---------------------------------------------");
		logger.debug(postOnVmRemove);

        Target postOnVmAddt = new Target(Target.ONVMADD_TARGET, postOnVmAdd);
        targets.add(postOnVmAddt);

        Target postOnVmRemovet = new Target(Target.ONVMREMOVE_TARGET, postOnVmRemove);
        targets.add(postOnVmRemovet);

        Target postScalet = new Target(Target.POSTSCALE_TARGET, postScale);
        targets.add(postScalet);
        
        Target preScalet = new Target(Target.PRESCALE_TARGET, preScale);
        targets.add(preScalet);
        
	}
}
