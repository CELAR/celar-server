package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getProvidedResourceByTypeName;
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
    public REList<ProvidedResource> getFlavors() throws DBException {
          return new REList(getProvidedResourceByTypeName("VM_FLAVOR"));
    }
    
    @GET
    @Path("images/")
    public REList<ProvidedResource>  getImages() throws DBException {
        return new REList(getProvidedResourceByTypeName("VM_IMAGE"));
    }
    


}
