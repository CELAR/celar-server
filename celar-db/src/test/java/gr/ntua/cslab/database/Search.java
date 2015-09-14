/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database;
import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Decision;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.Module;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.database.DBTools.Constrain;
import static gr.ntua.cslab.database.Entities.depl;
import static gr.ntua.cslab.database.EntityGetters.getProvidedResourceByFlavorInfo;
import static gr.ntua.cslab.database.EntitySearchers.*;

import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author cmantas
 */
public class Search extends Entities{
    

    
    public static void main(String args[]) throws Exception{
        DBTools.loadProperties();
        DBTools.HOST="109.231.126.66";
        DBTools.openConnectionWithSetProps();
//        createApplicationStructure();
        
//            public static List<Application> searchApplication(
//            long submittedStart, long submittedEnd,
//            String description, int userid,  String moduleName, String componentDescription,
//            String providedResourceId){
        int userId=1;
        List<Application> result = searchApplication(0, System.currentTimeMillis(), null, userId, null, null, null);
        
        System.out.println(result.size());
        
         List<ProvidedResource> flavors = getProvidedResourceByFlavorInfo(1, 512, 20);
         
         System.out.println("Flavors count: "+flavors.size());
         for (ProvidedResource pr: flavors) {
             System.out.println(pr);            
        }
        
        
        
//        System.exit(0);
//        
//        createDeployment();
//        
//        List<MetricValue> mvl = searchMetricValues(depl, metric, new MyTimestamp((long)0), new MyTimestamp(System.currentTimeMillis()));
//        System.out.println("Metric Values: "+mvl);
//        
//        List<Decision> decisions = searchDecisions(depl, 0, System.currentTimeMillis(),"ADD", component.id, module.id);
//        
//        System.out.println("Found decisions: "+ decisions);
//        assertTrue(decisions.get(0).equals(decision));
//        
        //get d
        

//        deleteDeployment();
//        
//        
//        delete_structure();
    }
    
}
