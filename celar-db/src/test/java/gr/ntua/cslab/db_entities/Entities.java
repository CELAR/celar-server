/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.parsers.ApplicationParser;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import static org.junit.Assert.fail;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author cmantas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Entities {

    ResourceType resourceType;
    ProvidedResource providedResource;
    Spec spec1, spec2;
    User user;

    @Test
    public void showOff() {
        try {

            String username = "ggian";
            
            //create a user entity
                user = new User(username);

            //the user has not been given an id yet (0)
                System.out.println("User.id=" + user.getId());

            //store him in the DB. Now he has an id
                user.store();
                System.out.println(user);

            //export the entity to JSON
                JSONObject userJson = user.toJSONObject();

            // create another entity with the same fields (but 0 id)
            // if we stored it, it whould get a new id
                User user2 = new User(userJson);

            // search a user by its name
                User user3 = User.getByName(username);
                System.out.println("Found user: " + user3);

            //create and store a Resource type
                resourceType = new ResourceType("VM_IMAGE");
                resourceType.store();

            // create and store a Provided resource
            // Note that you need to know the father entity of an entity that 
            // is the child of other entities (has foreing keys to other tables)
                providedResource = new ProvidedResource("sample_vm_image", resourceType);
                providedResource.store();

            // search the provided resources by a type (father)
                List<ProvidedResource> prl = ProvidedResource.getByType(resourceType);
                System.out.println(prl);

            //create and store some specs 
                spec1 = new Spec(providedResource, "cpu_count", "2");
                spec1.store();
                spec2 = new Spec(providedResource, "ram_size", "3");
                spec2.store();

            //search the specs by their father provided resource
                List<Spec> spl = Spec.getByProvidedResource(providedResource);
                System.out.println(spl);

            // Create an application structure
                Application app = new Application("test_application", user);
                app.store();
                Module module = new Module("test_module", app);
                module.store();
                Component component = new Component(module, "test module", resourceType);
                component.store();
            
            //==========  This creates a structured JSON. Maybe not useful... I dunno.=================    
                //export the application description to a JSONObject
                    JSONObject appDescriptionJson = ApplicationParser.exportApplicationDescription(app, new Timestamp(System.currentTimeMillis()));
                    System.out.println(appDescriptionJson.toString(3));
            //============================================================================================
                    
            /*  delete the applciation structure
                note that we delete the "child" fields first so as not to cause
                any foreign key violations
            */            
                component.delete();
                module.delete();
                app.delete();

            // delete resources. etc
                spec1.delete();
                spec2.delete();
                providedResource.delete();
                resourceType.delete();

            //delete the user
                user.delete();
        } catch (DBException ex) {
            System.out.println(ex);
            fail("failed to create resources");
        }
    }
    
    @Test
    public void testtest(){
                    System.out.println("hellllooooo");
            
            ClassLoader cl = ClassLoader.getSystemClassLoader();

            URL[] urls = ((URLClassLoader) cl).getURLs();

            for (URL url : urls) {
                System.out.println(url.getFile());
            }
    }

}
