package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.rest.beans.resources.ElasticityAction;
import gr.ntua.cslab.celar.server.daemon.rest.beans.resources.Flavor;
import gr.ntua.cslab.celar.server.daemon.rest.beans.resources.Image;
import gr.ntua.cslab.celar.server.daemon.rest.beans.resources.Probe;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Giannis Giannakopoulos
 */
@Path("/resources/")
public class Resources {
    
    @GET
    @Path("flavors/")
    public List<Flavor> getFlavors() {
        // sample flavor list, it will be replaced by a query in the db
        List<Flavor> list = new LinkedList<>();
        list.add(new Flavor(141, "C8R4096D80drbd"));
        list.add(new Flavor(237, "C4R2048D40ext_vlmc"));
        list.add(new Flavor(254, "C4R8192D100ext_vlmc"));
        list.add(new Flavor(91, "C2R8192D100drbd"));
        return list;
    }
    
    @GET
    @Path("images/")
    public List<Image> getImages() {
        List<Image> images = new LinkedList<>();
        
        images.add(new Image("78e96a57-2436-45c8-96b5-5eda9eb69be9", "Debian Base"));
        images.add(new Image("fc1ed744-ae05-469a-a5b9-e561428ec0e0", "Ubuntu Server LTS"));
        images.add(new Image("48644f1e-4faa-45d9-9ae7-be6b0f51c063", "Fedora"));
        images.add(new Image("df6dcbbf-4fe2-4550-8bfd-f65b57dc45eb", "CentOS"));
        return images;
    }
    
    @GET
    @Path("probes/")
    public List<Probe> getProbes() {
        List<Probe> probes = new LinkedList<>();
        Probe def = new Probe();
        def.setName("CPU Probe");
        def.addMetric("CPU Usage");
        def.addMetric("CPU Idle");
        def.addMetric("CPU User");
        def.addMetric("CPU Kernel");
        
        probes.add(def);
        
        Probe mem = new Probe();
        mem.setName("Memory probe");
        mem.addMetric("Free memory");
        mem.addMetric("Swap memory");
        mem.addMetric("Cached memory");
        
        probes.add(mem);
        
        return probes;
    }

    @GET
    @Path("actions/")
    public List<ElasticityAction> getElasticityActions() {
        List<ElasticityAction> actions = new LinkedList<>();
        actions.add(new ElasticityAction(1, "AddVM"));
        actions.add(new ElasticityAction(2, "RemoveVM"));
        return actions;
    }

}
