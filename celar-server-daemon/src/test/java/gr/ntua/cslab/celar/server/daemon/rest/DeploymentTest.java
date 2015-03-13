
package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.Decision;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.Resource;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import static gr.ntua.cslab.celar.server.daemon.rest.ApplicationTest.depl;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentById;
import static gr.ntua.cslab.database.EntityGetters.getMetricValues;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author cmantas
 */
public class DeploymentTest extends ApplicationTest {
    static MetricValue mv;
     static Resource res;
     static Decision des;
        public static void create() throws Exception{
            ApplicationTest.create();
        //================ Actual deployment testing ==============
            depl = new Deployment(app, ""+random.nextInt(), "1.2.3.4");
            store(depl);
            assertTrue(depl.equals(getDeploymentById(depl.getId())));
            
            res= new Resource(depl, component, providedResource);
            store(res);
            res = new Resource(res.getId());
            System.out.println(res);
            assertTrue(res.equals(new Resource(res.getId())));
            
            //metric value testing
            mv = new MetricValue(metric, res);
            store(mv);
            List<MetricValue> mvl = getMetricValues( metric, new Timestamp(0), new Timestamp(System.currentTimeMillis()));
            
            //decision testing
            des = new Decision(ra, depl, 2);
            store(des);
            
    }
        
        public static void destroy() throws DBException{
            try{
                delete(des);
                delete(mv);            
                delete(res);            
                delete(depl);
            }
            catch (Exception ex){
                System.err.println("***** Failed to delete deployment. Is that OK???");
            }
            ApplicationTest.destroy();
        }
        
        public static void main(String args[]) throws Exception{
            create();
            
            Deployment rv = Deployments.getDeployment(depl.id);
            System.out.println(rv.toString(true));
            
//            REList<Metric> ml = Deployments.getMetrics(depl.id);
//            System.out.println("Got Metrics: "+ml);
            
            REList<Decision> dl = Deployments.getDecisions(depl.id, -1, -1, module.id, component.id);
            
            System.out.println(dl);
            
            dl.marshal(new FileOutputStream("decisions.xml"));
            
            Deployments.terminateDeployment(rv.id);
//            Deployments.terminateDeployment(rv.id);
            
            destroy();
        }
}
