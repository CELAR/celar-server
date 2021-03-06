/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database.parsers;

import gr.ntua.cslab.celar.server.beans.*;

import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.beans.structured.DeployedApplication;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import static gr.ntua.cslab.database.parsers.ApplicationParser.*;
import java.io.FileOutputStream;
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
    static Resource resource;
    static Deployment depl;
    
    
    public static void createBaseline(){
        try{
            
        chris = new User("chris", "dummy cred");
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
        app = new Application("test_application", new MyTimestamp(System.currentTimeMillis()), chris, "/dummy/csar");
        store(app);
        module = new Module("test_module", app);
        store(module);
        component = new Component(module, "test module", vm);
        store(component);
        depl = new Deployment(app, ""+(new java.util.Random()).nextInt(), "1.2.3.4");
        store(depl);
        resource = new Resource(depl, component, tinyVM);
        System.out.println(resource);
        store(resource);
        
        } catch(Exception e){
            e.printStackTrace();
            fail("failed to create baseline entities to before testing JSON Tools");
        }
    }
    
    
    
    public static void clear() throws DBException{
            System.out.println("------> Clearing the DB ");
            
            delete(resource);
            delete(coreCount);
            delete(ramSize);
            delete(tinyVM);
            delete(component);
            delete(vm);
            delete(module);
            delete(depl);
            delete(app);
            delete(chris);            

            

        

        
    }
    
    static JSONObject appDescriptionJson;

    public static void exportApplicationDescriptionJson() throws Exception {
       
        //export the application description to a JSONObject
        appDescriptionJson = exportApplicationDescriptionJ(app);
        System.out.println(appDescriptionJson.toString(3));
        

    }
    
    public static void exportApplicationDescriptionObject() throws Exception {
     
        //export the application description to a JSONObject
        System.out.println("here");
        DeployedApplication ai = exportApplication(app, depl.getId());
        System.out.println("Hello");
        System.out.println(ai.toString(true));
        FileOutputStream fo = new FileOutputStream("test");
        ai.marshal(fo);
        fo.close();
        
    }
    
    public  static void main(String args[]) throws Exception{
        createBaseline();
        exportApplicationDescriptionObject();
        clear();
        
    }

    
}
