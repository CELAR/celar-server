package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;


/**
 *
 * @author cmantas
 */
@Path("/metrics/")
public class Metrics {
    
    @POST
    @Path("put/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public static Metric put(@Context HttpServletRequest request, InputStream input) throws JAXBException, DBException {
        Metric m = new Metric();
        m.unmarshal(input);
        store(m);
        return m;
    }
    
    @GET
    @Path("remove/{id}")
    public static String remove(@PathParam("id") int id) throws JAXBException, DBException, Exception {
        Metric m = new Metric(id);
        if(m.id==0) return "Metric("+id+") does not exist";
        delete(m);
        return "OK";
    }
    

    
}