
package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.*;
import gr.ntua.cslab.celar.server.beans.structured.*;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentById;
import static gr.ntua.cslab.database.EntityGetters.searchMetricValues;
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
public class DeploymentsTest extends ApplicationTest {
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
            mv = new MetricValue(metric, res, 5);
            store(mv);
            assertTrue(mv.equals(new MetricValue(mv.id)));
            
            List<MetricValue> mvl = searchMetricValues(depl, metric, new Timestamp(0), new Timestamp(System.currentTimeMillis()));
            System.out.println(mvl);
            

            
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
            
            // test resources
            REList<Resource> resources = Deployments.getDeploymentResources(depl.id, -1);            
            System.out.println("RESOURCES:\n"+resources.toString(true));
            
            // test decision
            des = Deployments.addDecision(depl.id, component.id, "ADD", 2);
            System.out.println("I put decision: "+ des);
            REList<Decision> dl = Deployments.getDecisions(depl.id, -1, -1, module.id, component.id, "ADD");
            delete(des);
            
            System.out.println(dl);
            
            dl.marshal(new FileOutputStream("decisions.xml"));
            
            
            System.exit(0);
            
            
            Deployments.terminateDeployment(rv.id);
//            Deployments.terminateDeployment(rv.id);
            
            destroy();
        }
}
