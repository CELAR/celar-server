/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database;
import gr.ntua.cslab.celar.server.beans.Application;
import static gr.ntua.cslab.database.EntityGetters.searchApplication;
import java.util.List;

/**
 *
 * @author cmantas
 */
public class SearchApp extends Entities{
    
    public static void main(String args[]) throws Exception{
        createApplicationStructure();
        
//            public static List<Application> searchApplication(
//            long submittedStart, long submittedEnd,
//            String description, int userid,  String moduleName, String componentDescription,
//            String providedResourceId){
        List<Application> result = searchApplication(0, 0, null,0, null, null, null);
        System.out.println(result);
        
        
        delete_structure();
    }
    
}
