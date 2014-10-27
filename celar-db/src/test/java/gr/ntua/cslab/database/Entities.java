/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database;

import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.Module;
import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.Resource;
import gr.ntua.cslab.celar.server.beans.ResourceType;
import gr.ntua.cslab.celar.server.beans.Spec;
import gr.ntua.cslab.celar.server.beans.User;
import static gr.ntua.cslab.database.EntityGetters.*;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author cmantas
 */

public class Entities {

    static ResourceType resourceType;
    static ProvidedResource providedResource;
    static Spec spec1, spec2;
    static User user;
    static Application app;
    static Component component;
    static Metric metric;
    static Module module;
    static Deployment depl;
    
    static Random random = new Random();
    
    public static void createApplicationStructure() {
        try {

            String username = "ggian";
            
            //create a user entity
                user = new User(username);

            //the user has not been given an id yet (0)
                System.out.println("User.id=" + user.getId());

            //store him in the DB. Now he has an id
                store(user);
                System.out.println(user);

            //you can retrieve an entity by its id (if it has one)
                System.out.println("retrieving user");
                user = new User(user.getId());
                System.out.println("Retrieved "+user);
                
                

            // search a user by its name
                User user3 = getUserByName(username);
                System.out.println("Found user: " + user3);

            //create and store a Resource type
                resourceType = new ResourceType("VM_IMAGE");
                store(resourceType);
                System.out.println("Retrieved: " + new ResourceType(resourceType.getId()));

            // create and store a Provided resource
            // Note that you need to know the father entity of an entity that 
            // is the child of other entities (has foreing keys to other tables)
                providedResource = new ProvidedResource("sample_vm_image", resourceType);
                store(providedResource);
                assertTrue(providedResource.equals(new ProvidedResource(providedResource.getId())));

            // search the provided resources by a type (father)
                List<ProvidedResource> prl = getProvidedResourceByType(resourceType);
                System.out.println(prl);
//
            //create and store some specs 
                spec1 = new Spec(providedResource, "cpu_count", "2");
                store(spec1);
                spec2 = new Spec(providedResource, "ram_size", "3");
                store(spec2);

            //search the specs by their father provided resource
                List<Spec> spl = getSpecsByProvidedResource(providedResource);
                System.out.println(spl);

            // Create an application structure
                app = new Application("test_application", user);
                System.out.println("storiing app");
                store(app);
                //app = new Application (app.getId());
                System.out.println(app);
                module = new Module("test_module", app);
                store(module);
                component = new Component(module, "test module", resourceType);
                store(component);
                System.out.println(component);
                System.out.println(new Component(component.getId()));
                assertTrue(component.equals(new Component(component.getId())));
                
                metric = new Metric(component);
                store(metric);
                assertTrue(metric.equals( new Metric(metric.getId())));
        
        } catch (Exception ex) {
            System.out.println(ex);
            fail("failed to create resources");
        }
    }
    
    public static void testDeployment() throws Exception{
        //================ Actual deployment testing ==============
            depl = new Deployment(app, ""+random.nextInt());
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


    
    
    public static void main(String args[]) throws Exception{
        createApplicationStructure();
        testDeployment();
        delete_structure();
    }

}
