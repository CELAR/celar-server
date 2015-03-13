package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getMetricValues;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
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
    
    @GET
    @Path("{id}/values")
    public static REList<MetricValue> getValues(
            @PathParam("id") int id,
            @DefaultValue("-1") @QueryParam("start_time") long start,
            @DefaultValue("-1") @QueryParam("end_time") long end    
                              ) throws JAXBException, DBException, Exception {
        Metric m = new Metric(id);
        if(m.id==0) return null;
        Timestamp tstart=null, tend =null;
        if (start>=0) tstart = new Timestamp(0);
        if (end>0) tend = new Timestamp(end);
        List<MetricValue> mvl = getMetricValues(m ,tstart, tend);
        REList<MetricValue> rv = new REList();
        rv.values.addAll(mvl);
        return rv;

    }
    

    
}