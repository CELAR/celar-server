/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.*;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.*;
import static gr.ntua.cslab.database.EntityTools.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;

/**
 *
 * @author cmantas
 */
public class ApplicationTest {
    
        static ResourceType resourceType;
    static ProvidedResource providedResource;
    static Spec spec1, spec2;
    static gr.ntua.cslab.celar.server.beans.User user;
    static gr.ntua.cslab.celar.server.beans.Application app;
    static Component component;
    static Metric metric;
    static Module module;
    static gr.ntua.cslab.celar.server.beans.Deployment depl;
    static Application dummy = new Application();
    
    static Random random = new Random();
    
        public static void createApplicationStructure() {
        try {

            String username = "ggian";
            
            //create a user entity
                user = new gr.ntua.cslab.celar.server.beans.User(username);
            //store him in the DB. Now he has an id
                store(user);
           //you can retrieve an entity by its id (if it has one)

            //create and store a Resource type
                resourceType = new ResourceType("VM_IMAGE");
                store(resourceType);

            // create and store a Provided resource
                providedResource = new ProvidedResource("sample_vm_image", resourceType);
                store(providedResource);
         
//
            //create and store some specs 
                spec1 = new Spec(providedResource, "cpu_count", "2");
                store(spec1);
                spec2 = new Spec(providedResource, "ram_size", "3");
                store(spec2);

            // Create an application structure
                app = new gr.ntua.cslab.celar.server.beans.Application("test_application", user);
                store(app);
                module = new Module("test_module", app);
                store(module);
                component = new Component(module, "test module", resourceType);
                store(component);
                
                metric = new Metric(component);
                store(metric);
        
        } catch (Exception ex) {
            System.out.println(ex);
            fail("failed to create resources");
        }
    }
    
    public static void testDeployment() throws Exception{
        //================ Actual deployment testing ==============
            depl = new gr.ntua.cslab.celar.server.beans.Deployment(app, ""+random.nextInt());
            store(depl);
            assertTrue(depl.equals(getDeploymentById(depl.getId())));
            
            Resource res = new Resource(depl, component, providedResource);
            store(res);
            res = new Resource(res.getId());
            System.out.println(res);
            assertTrue(res.equals(new Resource(res.getId())));
            
            //metric value testing
            MetricValue mv = new MetricValue(metric, res);
            store(mv);
            List<MetricValue> mvl = getMetricValue( metric, new Timestamp(0), new Timestamp(System.currentTimeMillis()));
            System.out.println("Metric Values: "+mvl);
            
            
            delete(mv);            
            delete(res);            
            delete(depl);
    }
    
    public static void delete_structure() throws DBException{
                delete(metric);
                delete(component);
                delete(module);
                delete(app);
//
            // delete resources. etc
                delete(spec1);
                delete(spec2);
                delete(providedResource);
                delete(resourceType);
//
            //delete the user
                delete(user);                delete(metric);
                delete(component);
                delete(module);
                delete(app);
//
            // delete resources. etc
                delete(spec1);
                delete(spec2);
                delete(providedResource);
                delete(resourceType);
//
            //delete the user
                delete(user);
    }


    static void testCSAR(String filename) throws Exception{

        
        FileInputStream csar = new FileInputStream(filename);
        ApplicationInfo ai = dummy.describe2(null, csar);
        csar = new FileInputStream(filename);
        ai = dummy.deploy2(null, ai.getId(), csar);
        //System.out.println(ai.toString(true));
        ai.marshal(System.out);
        removeApplication(ai);
    }
    
    
    public static void main(String args[]) throws Exception{
        createApplicationStructure();
        
//        ApplicationInfo info = dummy.getApplicationInfo(app.getId());
//        System.out.println(info.toString(true));
//        ApplicationInfo ai =dummy.getApplicationInfo(app.id);
//        System.out.println(ai.toString(true));
        
        testCSAR("../celar-db/testApp09.csar");
        delete_structure();        
        
        
  
    }

}
    
    