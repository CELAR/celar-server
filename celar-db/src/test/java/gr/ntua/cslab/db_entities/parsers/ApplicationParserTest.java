/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities.parsers;

import gr.ntua.cslab.db_entities.Application;
import gr.ntua.cslab.db_entities.Component;
import gr.ntua.cslab.db_entities.DBException;
import gr.ntua.cslab.db_entities.Module;
import gr.ntua.cslab.db_entities.ProvidedResource;
import gr.ntua.cslab.db_entities.ResourceType;
import gr.ntua.cslab.db_entities.Spec;
import gr.ntua.cslab.db_entities.User;
import static gr.ntua.cslab.db_entities.parsers.ApplicationParser.*;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author cmantas
 */
@Ignore 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationParserTest {
    
    public ApplicationParserTest() {
    }
    
      public static User chris;
    public static ProvidedResource tinyVM;
    public static Spec coreCount, ramSize;
    public static ResourceType vm;
    
    
    @Test
    public void test_00_createBaseline(){
        try{
            
        chris = new User("chris");
        chris.store();        
        //create, save and retrieve a resource type
        vm = new ResourceType("VM_FLAVOR");
        vm.store();
        //create, save and retrieve a provided resource
        tinyVM = new ProvidedResource("tiny VM", vm);
        tinyVM.store();
        //create and retreive a resource spec
        coreCount = new Spec(tinyVM, "cores", "2");
        coreCount.store();
        ramSize = new Spec(tinyVM, "ram", "2048");
        ramSize.store();
        
        } catch(Exception e){
            e.printStackTrace();
            fail("failed to create baseline entities to before testing JSON Tools");
        }
    }
    
    @Test
    public void test_99_clear() throws DBException{
            System.out.println("------> Clearing the DB ");
            coreCount.delete();
            ramSize.delete();
            tinyVM.delete();
            vm.delete();
            chris.delete();

        
    }
    
    static JSONObject appDescriptionJson;

    @Test
    public void test_01_exportApplicationDescription() throws Exception {
        Application app = new Application("test_application", new Timestamp(System.currentTimeMillis()), chris);
        app.store();
        Module module = new Module("test_module", app);
        module.store();
        Component component = new Component(module, "test module", vm);
        component.store();
        //export the application description to a JSONObject
        appDescriptionJson = exportApplicationDescription(app);
        System.out.println(appDescriptionJson.toString(3));
        
        component.delete();
        module.delete();
        app.delete();
    }
    
    @Test
    public void test_01_parseApplicationDescription() throws Exception {
        //create again the application structure from its JSON description and store it
        System.out.println("\n ================  TEST Auto-generated JSON  ===========================");
        try{
            Application a = parseApplicationDescription(appDescriptionJson, false);
            //a.delete();
        }catch(Exception e){
            System.out.println("Error Message: "+ e.getMessage());
            e.printStackTrace();
            fail("failed to import an application description from JSON");
        }
    }
    
}
