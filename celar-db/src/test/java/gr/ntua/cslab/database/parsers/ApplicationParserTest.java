/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database.parsers;

import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Module;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.ResourceType;
import gr.ntua.cslab.celar.server.beans.Spec;
import gr.ntua.cslab.celar.server.beans.User;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import static gr.ntua.cslab.database.EntityTools.store;
import static gr.ntua.cslab.database.EntityTools.store;
import static gr.ntua.cslab.database.parsers.ApplicationParser.*;
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
    public static  Module module;
    public static Component component;
    static Application app ;
    
    
    @Test
    public void test_00_createBaseline(){
        try{
            
        chris = new User("chris");
        store(chris);        
        //create, save and retrieve a resource type
        vm = new ResourceType("VM_FLAVOR");
        store(vm);
        //create, save and retrieve a provided resource
        tinyVM = new ProvidedResource("tiny VM", vm);
        store(tinyVM);
        //create and retreive a resource spec
        coreCount = new Spec(tinyVM, "cores", "2");
        store(coreCount);
        ramSize = new Spec(tinyVM, "ram", "2048");
        store(ramSize);
        app = new Application("test_application", new MyTimestamp(System.currentTimeMillis()), chris);
        store(app);
        module = new Module("test_module", app);
        store(module);
        component = new Component(module, "test module", vm);
        store(component);
        
        } catch(Exception e){
            e.printStackTrace();
            fail("failed to create baseline entities to before testing JSON Tools");
        }
    }
    
    @Test
    public void test_99_clear() throws DBException{
            System.out.println("------> Clearing the DB ");
            delete(coreCount);
            delete(ramSize);
            delete(tinyVM);
            System.out.println(component);
            delete(component);
            delete(vm);
            delete(module);
            delete(app);
            delete(chris);

        
    }
    
    static JSONObject appDescriptionJson;

    @Test
    @Ignore
    public void test_01_exportApplicationDescriptionJson() throws Exception {
       
        //export the application description to a JSONObject
        appDescriptionJson = exportApplicationDescriptionJ(app);
        System.out.println(appDescriptionJson.toString(3));
        

    }
    
    @Test
    public void test_02_exportApplicationDescriptionObject() throws Exception {
     
        //export the application description to a JSONObject
        System.out.println("here");
        ApplicationInfo ai = exportApplicationDescription(app);
        System.out.println("Hello");
        System.out.println(ai.printStructured());
        
    }

    
}
