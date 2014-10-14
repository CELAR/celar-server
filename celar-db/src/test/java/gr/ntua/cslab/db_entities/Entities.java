/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.parsers.ApplicationParser;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
public class Entities {

    ResourceType resourceType;
    ProvidedResource providedResource;
    Spec spec1, spec2;
    User user;
    Application app;
    Component component;

    @Test
    @Ignore
    public void test_00_showOff() {
        try {

            String username = "ggian";
            
            //create a user entity
                user = new User(username);

            //the user has not been given an id yet (0)
                System.out.println("User.id=" + user.getId());

            //store him in the DB. Now he has an id
                user.store();
                System.out.println(user);

            //you can retrieve an entity by its id (if it has one)
                user = new User(user.getId());
                System.out.println("Retrieved "+user);
                
                

            // search a user by its name
                User user3 = User.getByName(username);
                System.out.println("Found user: " + user3);

            //create and store a Resource type
                resourceType = new ResourceType("VM_IMAGE");
                resourceType.store();
                System.out.println("Retrieved: " + new ResourceType(resourceType.getId()));

            // create and store a Provided resource
            // Note that you need to know the father entity of an entity that 
            // is the child of other entities (has foreing keys to other tables)
                providedResource = new ProvidedResource("sample_vm_image", resourceType);
                providedResource.store();
                assertTrue(providedResource.equals(new ProvidedResource(providedResource.getId())));

            // search the provided resources by a type (father)
                List<ProvidedResource> prl = ProvidedResource.getByType(resourceType);
                System.out.println(prl);
//
            //create and store some specs 
                spec1 = new Spec(providedResource, "cpu_count", "2");
                spec1.store();
                spec2 = new Spec(providedResource, "ram_size", "3");
                spec2.store();

            //search the specs by their father provided resource
                List<Spec> spl = Spec.getByProvidedResource(providedResource);
                System.out.println(spl);

            // Create an application structure
                app = new Application("test_application", user);
                app.store();
                app = new Application (app.getId());
                System.out.println(app);
                Module module = new Module("test_module", app);
                module.store();
                component = new Component(module, "test module", resourceType);
                component.store();
                System.out.println(component);
                System.out.println(new Component(component.getId()));
                assertTrue(component.equals(new Component(component.getId())));
                
                Metric metric = new Metric(component);
                metric.store();
                assertTrue(metric.equals( new Metric(metric.getId())));
//            
//            //==========  This creates a structured JSON. Maybe not useful... I dunno.=================    
//                //export the application description to a JSONObject
//                    JSONObject appDescriptionJson = ApplicationParser.exportApplicationDescription(app);
//                    System.out.println(appDescriptionJson.toString(3));
//            //============================================================================================
//                    
//            /*  delete the applciation structure
//                note that we delete the "child" fields first so as not to cause
//                any foreign key violations
//            */            
                metric.delete();
                component.delete();
                module.delete();
                app.delete();
//
            // delete resources. etc
                spec1.delete();
                spec2.delete();
                providedResource.delete();
                resourceType.delete();
//
            //delete the user
                user.delete();
        } catch (Exception ex) {
            System.out.println(ex);
            fail("failed to create resources");
        }
    }
    

    @Test
    public void test_01_testDeployment(){
        try {
            
            //================ Prep =========================
            user = new User("name");
            user.store();
            app = new Application("test_application", user);
            app.store();
            
            Timestamp t = app.submitted;
            app.submitted = null;
            app.fromFieldMap(app.getFieldMap());
            System.out.println("DONE");
            app.submitted = t;
            
            Module module = new Module("test_module", app);
            module.store();
            
            Module module2 = new Module("test_module2", app);
            module2.store();
            
            ModuleDependency mdep = new ModuleDependency(module, module2);
            mdep.store();
            
            resourceType = new ResourceType("VM_IMAGE");
            resourceType.store();
            providedResource = new ProvidedResource("sample_vm_image", resourceType);
            providedResource.store();
            
            component = new Component(module, "test module", resourceType);
            component.store();
            Metric metric = new Metric(component);
            metric.store();
            
            //================ Actual deployment testing ==============
            Deployment depl = new Deployment(app);
            depl.store();
            assertTrue(depl.equals(new Deployment(depl.getId())));
            
            Resource res = new Resource(depl, component, providedResource);
            res.store();
            res = new Resource(res.getId());
            System.out.println(res);
            assertTrue(res.equals(new Resource(res.getId())));
            
            //metric value testing
            MetricValue mv = new MetricValue(metric, res);
            mv.store();
            mv = new MetricValue(mv.getId());
            
            
            mv.delete();
            
            res.delete();            
            depl.delete();
            //=================  TearDown ====================
            providedResource.delete();
            metric.delete();
            component.delete();
            resourceType.delete();
            mdep.delete();
            module2.delete();
            module.delete();
            app.delete();
            user.delete();
            
        } catch (DBException ex) {
            System.out.println(ex);
            fail("failed the test deployment");
        }
    }

}
