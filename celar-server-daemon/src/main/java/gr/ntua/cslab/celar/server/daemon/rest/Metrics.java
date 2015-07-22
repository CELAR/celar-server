package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentById;
import static gr.ntua.cslab.database.EntityGetters.getMetrics;
import gr.ntua.cslab.database.EntitySearchers;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.eclipse.jetty.server.Response;


/**
 *
 * @author cmantas
 */
@Path("/metrics/")
public class Metrics {
    
    static Logger LOG = getLogger(Metrics.class);
    
    @POST
    @Path("put/")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public static Metric put(@Context HttpServletRequest request, InputStream input, @QueryParam("token") String token) throws JAXBException, DBException {
        if(!token.equals("4c021c79-6862-4337-bc24-923320e41354"))
            throw  new WebApplicationException(Response.SC_FORBIDDEN);
        Metric m = new Metric();
        m.unmarshal(input);
        m.timestamp = new MyTimestamp(System.currentTimeMillis());
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
            @DefaultValue("-1") @QueryParam("deployment_Id") String deploymentID,
            @DefaultValue("-1") @QueryParam("start_time") long start,
            @DefaultValue("-1") @QueryParam("end_time") long end
                              ) throws JAXBException, DBException, Exception {
        Metric m = new Metric(id);
        if(m.id==0) return null;
        Timestamp tstart=null, tend =null;
        if (start>=0) tstart = new Timestamp(0);
        if (end>0) tend = new Timestamp(end);
        Deployment dep = null;
        if(!deploymentID.equals("-1")) dep=getDeploymentById(deploymentID);
        List<MetricValue> mvl = EntitySearchers.searchMetricValues(dep, m ,tstart, tend);
        REList<MetricValue> rv = new REList();
        rv.values.addAll(mvl);
        return rv;

    }
        
    @GET
    @Path("component/{component_id}")
    public static REList<Metric> getComponentMetrics(  @PathParam("component_id") int id) throws Exception{
        Component c = new Component(id);
        return new REList(getMetrics(c));
    }
    
    @POST
    @Path("values/put")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public static REList<MetricValue>  putMetrics(@Context HttpServletRequest request, InputStream input) throws JAXBException, DBException {
        REList<MetricValue> metrics = new REList<>();
        metrics.unmarshal(input);
        LOG.info("Received "+metrics.size()+ " metrics.");
        for(Object o: metrics){
            MetricValue m = (MetricValue) o;
            store(m);
        }
        return metrics;
    }

    
}