package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

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
    @Path("search/properties/")
    public List<ApplicationInfo> searchApplicationsByProperty(
            @DefaultValue("0") @QueryParam("submitted") long submitted,
            @DefaultValue("Null") @QueryParam("description") String description,
            @DefaultValue("0") @QueryParam("user_id") int userid) {
        List<ApplicationInfo> list = new LinkedList<>();
        list.add(new ApplicationInfo("ID1", submitted+100, description, "1.0.0"));
        list.add(new ApplicationInfo("ID2", submitted+200, description, "1.2.0"));
        list.add(new ApplicationInfo("ID3", submitted+300, description, "0.2.0"));
        return list;
    }
    
    @GET
    @Path("search/topology/")
    public List<ApplicationInfo> searchApplicationsByTopology(
            @DefaultValue("Null") @QueryParam("module_name") String moduleName,
            @DefaultValue("Null") @QueryParam("component_description") String description,
            @DefaultValue("Null") @QueryParam("provided_resource_id") String providedResourceId) {
        List<ApplicationInfo> list = new LinkedList<>();
        list.add(new ApplicationInfo("ID1", new Date().getTime()+10000, description, "1.0.0"));
        list.add(new ApplicationInfo("ID2", new Date().getTime()+50000, description, "1.2.0"));
        list.add(new ApplicationInfo("ID3", new Date().getTime()+100000, description, "0.2.0"));
        return list;
    }
}
