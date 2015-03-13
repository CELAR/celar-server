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
import gr.ntua.cslab.celar.server.beans.Module;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import gr.ntua.cslab.database.DBTools.Constrain;
import static gr.ntua.cslab.database.EntityGetters.searchApplication;
import static gr.ntua.cslab.database.EntityGetters.searchDecisions;
import static gr.ntua.cslab.database.EntityGetters.searchDecisions2;
import static gr.ntua.cslab.database.EntityTools.joinedTableName;
import static gr.ntua.cslab.database.EntityTools.joiner;
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
        List<Application> result = searchApplication(0, System.currentTimeMillis(),
                app.description,user.id, module.name, component.description, null);
        
        System.out.println(result);

        
        createDeployment();
        
        List<Decision> decisions = searchDecisions2(depl, 0, System.currentTimeMillis(),"ADD", component.id, module.id);
        
        System.out.println("Found decisions: "+ decisions);
        assertTrue(decisions.get(0).equals(decision));
        
        deleteDeployment();
        
        
        delete_structure();
    }
    
}
