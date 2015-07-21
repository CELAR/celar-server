package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.structured.ProvidedResourceInfo;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import static gr.ntua.cslab.database.EntitySearchers.searchProvidedResourceSpecs;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Giannis Giannakopoulos edit cmantas
 */
@Path("/resources/")
public class Resources {
    
    @GET
    @Path("flavors/")
    public static REList<ProvidedResourceInfo> getFlavors() throws Exception {
          return new REList(searchProvidedResourceSpecs("VM_FLAVOR"));
    }
    
    @GET
    @Path("images/")
    public static REList<ProvidedResourceInfo>  getImages() throws  Exception {
        return new REList(searchProvidedResourceSpecs("VM_IMAGE"));
    }
    


}
