
package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.Resource;
import static gr.ntua.cslab.celar.server.daemon.rest.ApplicationTest.depl;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentById;
import static gr.ntua.cslab.database.EntityGetters.getMetricValue;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
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
            List<MetricValue> mvl = getMetricValue( metric, new Timestamp(0), new Timestamp(System.currentTimeMillis()));
            System.out.println("Metric Values: "+mvl);
    }
        
        public static void destroy() throws DBException{
            try{
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
            
            System.out.println("tralala");
            Deployments.terminateDeployment(rv.id);
            System.out.println("trallo");
            Deployments.terminateDeployment(rv.id);
            
            destroy();
        }
}
