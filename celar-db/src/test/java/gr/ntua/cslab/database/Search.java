/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database;
import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Decision;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import static gr.ntua.cslab.database.Entities.depl;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author cmantas
 */
public class Search extends Entities{
    

    
    public static void main(String args[]) throws Exception{
        createApplicationStructure();
        
//            public static List<Application> searchApplication(
//            long submittedStart, long submittedEnd,
//            String description, int userid,  String moduleName, String componentDescription,
//            String providedResourceId){
        List<Application> result = EntitySearchers.searchApplication(0, System.currentTimeMillis(),
                app.description,user.id, module.name, component.description, null);
        
        System.out.println(result);

        
        createDeployment();
        
        List<MetricValue> mvl = EntitySearchers.searchMetricValues(depl, metric, new MyTimestamp((long)0), new MyTimestamp(System.currentTimeMillis()));
        System.out.println("Metric Values: "+mvl);
        
        List<Decision> decisions = EntitySearchers.searchDecisions(depl, 0, System.currentTimeMillis(),"ADD", component.id, module.id);
        
        System.out.println("Found decisions: "+ decisions);
        assertTrue(decisions.get(0).equals(decision));
        

        deleteDeployment();
        
        
        delete_structure();
    }
    
}
