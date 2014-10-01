package gr.ntua.cslab.celar.server.daemon.rest;

import com.sixsq.slipstream.persistence.Authz;
import com.sixsq.slipstream.persistence.DeploymentModule;
import com.sixsq.slipstream.persistence.ImageModule;
import com.sixsq.slipstream.persistence.ModuleParameter;
import com.sixsq.slipstream.persistence.Node;
import com.sixsq.slipstream.persistence.Target;
import com.sun.jersey.multipart.FormDataParam;
import gr.ntua.cslab.celar.server.daemon.cache.ApplicationCache;
import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.deployment.DeploymentStatus;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceException;
import tools.CSARParser;
import tools.Parser;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/application/")
public class Application {
    
    // IS calls
    @GET
    @Path("{id}/")
    public ApplicationInfo getApplicationInfo(@PathParam("id") String id) {
        ApplicationInfo info = new ApplicationInfo();
        info.setId(id);
        info.setDescription("Hello world :)");
        info.setSubmitted(new Date().getTime());
        info.setVersion("1.1.0");
        return info;
    }
    
    @GET
    @Path("search/")
    public List<ApplicationInfo> searchApplicationsByProperty(
            @DefaultValue("0") @QueryParam("submitted_start") long submittedStart,
            @DefaultValue("0") @QueryParam("submitted_end") long submittedEnd,
            @DefaultValue("Null") @QueryParam("description") String description,
            @DefaultValue("0") @QueryParam("user_id") int userid, 
            @DefaultValue("Null") @QueryParam("module_name") String moduleName,
            @DefaultValue("Null") @QueryParam("component_description") String componentDescription,
            @DefaultValue("Null") @QueryParam("provided_resource_id") String providedResourceId) {
        List<ApplicationInfo> list = new LinkedList<>();
        list.add(new ApplicationInfo("ID1", submittedStart+100, description, "1.0.0"));
        list.add(new ApplicationInfo("ID2", submittedStart+200, description, "1.2.0"));
        list.add(new ApplicationInfo("ID3", submittedStart+1000, description, "0.2.0"));
        return list;
    }    
    
    @GET
    @Path("{id}/description/")
    public ApplicationInfo getApplicationDescription(@PathParam("id") String id) {
        ApplicationInfo info = new ApplicationInfo();
        info.setId(id);
        info.setDescription("This xml will be replaced by a TOSCA (or CSAR??) file...");
        info.setSubmitted(new Date().getTime());
        info.setVersion("1.1.0");
        return info;
    }
    
    @POST
    @Path("describe/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public ApplicationInfo describe(@Context HttpServletRequest request, InputStream input) throws IOException, Exception {
        
        // fetching csar file and store it to local fs
        String filename = "/tmp/csar/"+System.currentTimeMillis()+".csar";
        byte[] buffer = new byte[1024];
        OutputStream file = new FileOutputStream(filename);
        int count, sum=0;
        while(input.available()>0) {
            count=input.read(buffer);
            sum+=count;
            file.write(buffer, 0, count);
        }
        input.close();
        
        
        InputStream propsInput = Application.class.getClassLoader().getResourceAsStream("slipstream.properties");
        Properties properties = new Properties();
        if(propsInput!=null)
            properties.load(propsInput);
        else
            throw new WebServiceException("Wrong server configuration; SlipStream not accessible");
        System.out.println(properties);
        String  username=properties.getProperty("slipstream.username"),
                password=properties.getProperty("slipstream.password"),
                slipstreamHost=properties.getProperty("slipstream.url");
        // start slipstream description
        SlipStreamSSService ssservise = new SlipStreamSSService(username, password, slipstreamHost);

        //create a Parser instance
        Parser tc = new CSARParser(filename);

        //application name and version
        String ssApplicationName = System.currentTimeMillis()+"-"+tc.getAppName();
        System.out.println("Application: " + tc.getAppName() + " v" + tc.getAppVersion());
        String appName = ssservise.createApplication(ssApplicationName, tc.getAppVersion());
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        //iterate through modules
        for (String module : tc.getModules()) {
            System.out.println("\t" + module);

            //module dependecies
            System.out.println("\t\tdepends on: " + tc.getModuleDependencies(module));

            //iterate through components
            for (String component : tc.getModuleComponents(module)) {
                System.out.println("\t\t" + component);
                ImageModule imModule = new ImageModule(appName + "/" + component);
                Authz auth = new Authz(ssservise.getUser(), imModule);
                imModule.setAuthz(auth);

                //component dependencies
                System.out.println("\t\t\tdepends on: " + tc.getComponentDependencies(component));

                //component properties
                Set<Target> targets = new HashSet<Target>();
                for (Map.Entry prop : tc.getComponentProperties(component).entrySet()) {
                    //System.out.println("\t\t\t"+prop.getKey()+": "+prop.getValue());
                    if (prop.getKey().toString().equals("VMI")) {
                        imModule.setModuleReference("examples/images/ubuntu-12.04");
                    } else if (prop.getKey().toString().equals("executeScript")) {
                        System.out.println("script");
                        Target t = new Target(Target.EXECUTE_TARGET, prop.getValue().toString());
                        targets.add(t);

                    }
                }
                imModule.setTargets(targets);

                String parameterName = "ready";
                String description = "Server ready";

                ModuleParameter parameter = new ModuleParameter(parameterName, "", description);
                parameter.setCategory("Output");
                imModule.setParameter(parameter);

                parameterName = "loaded";
                description = "Data loaded";

                parameter = new ModuleParameter(parameterName, "", description);
                parameter.setCategory("Output");
                imModule.setParameter(parameter);

                parameterName = "Flexiant.ram";
                description = "ram";
                String value = "2048";

                parameter = new ModuleParameter(parameterName, value, description);
                parameter.setCategory("Flexiant");
                parameter.setDefaultValue("2048");
                imModule.setParameter(parameter);

                parameterName = "Flexiant.cpu";
                description = "cpu";
                value = "2";

                parameter = new ModuleParameter(parameterName, value, description);
                parameter.setCategory("Flexiant");
                parameter.setDefaultValue("2");
                imModule.setParameter(parameter);

                parameterName = "okeanos.instance.type";
                description = "Flavor";
                value = "C2R2048D10ext_vlmc";

                parameter = new ModuleParameter(parameterName, value, description);
                parameter.setCategory("okeanos");
                parameter.setDefaultValue("C2R2048D10ext_vlmc");
                imModule.setParameter(parameter);

                parameterName = "okeanos.security.groups";
                description = "Security Groups (comma separated list)";
                value = "default";

                parameter = new ModuleParameter(parameterName, value, description);
                parameter.setCategory("okeanos");
                parameter.setDefaultValue("default");
                imModule.setParameter(parameter);

                ssservise.putModule(imModule);
                nodes.put(imModule.getShortName(), new Node(imModule.getShortName(), imModule));

            }
        }

        //add DeploymentModule 
        String name = appName + "/" + appName;
        DeploymentModule deployment = new DeploymentModule(name);
        Authz auth = new Authz(ssservise.getUser(), deployment);
        deployment.setAuthz(auth);
        System.out.println(nodes);
        deployment.setNodes(nodes);
        
        
        ApplicationInfo info = new ApplicationInfo();
        info.setId(UUID.randomUUID().toString());
        info.setSubmitted(System.currentTimeMillis());
        info.setVersion("1.0");
        info.setDescription("No description for now dude!");
        info.setSlipstreamName(appName);
        
        ApplicationCache.insertApplication(info);

        return info;
    }
    
    @POST
    @Path("{id}/deploy/")
    public DeploymentInfo launchDeployment(@PathParam("id") String applicationId){
         ApplicationInfo app = ApplicationCache.getApplicationById(applicationId);
         
         
         DeploymentInfo deployment = new DeploymentInfo();
         deployment.setApplication(app);
         deployment.setStartTime(System.currentTimeMillis());
         deployment.setEndTime(-1);
         deployment.setStatus(DeploymentStatus.BOOTSTRAPPING);
         return new DeploymentInfo();
    }
}
