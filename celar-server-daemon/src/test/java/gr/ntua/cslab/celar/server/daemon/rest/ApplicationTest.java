package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.*;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationList;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.*;
import static gr.ntua.cslab.database.EntityTools.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
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
    static Applications dummy = new Applications();
    static ResizingAction ra;
    
    static Random random = new Random();
    
        public static void create() throws Exception{
        try {

            String username = "ggian";
            
            //create a user entity
                user = new gr.ntua.cslab.celar.server.beans.User(username, "dummy cred");
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
                app = new Application("test_application", user, "dummy");
                store(app);
                module = new Module("test_module", app);
                store(module);
                component = new Component(module, "test module", resourceType);
                store(component);
                
                metric = new Metric(component, "my metric");
                store(metric);
                
             //resizing action
                ra = new ResizingAction(component, module,"ADD");
                store(ra);
        
        } catch (Exception ex) {
            System.out.println(ex);
            fail("failed to create resources");
        }
    }
    

    
    public static void destroy() throws DBException{
                delete(metric);
                delete(ra);
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
        ApplicationInfo ai = dummy.describe(null, csar);
        csar = new FileInputStream(filename);
        ai = Applications.launchDeployment(null, ai.getId(), csar);
        //System.out.println(ai.toString(true));
        ai.marshal(System.out);
        removeApplication(ai);
    }
    
    static void search() throws Exception{
//        ApplicationInfoList ail = Applications.searchApplicationsByProperty(0, System.currentTimeMillis(),app.description,user.id, module.name, component.description, null);
         ApplicationList ail = Applications.searchApplicationsByProperty(0, 0,app.description,user.id, module.name, component.description, null);
         ail.marshal(System.out);
         
         FileOutputStream fos = new FileOutputStream("testSearch");
         ail.marshal(fos);
         fos.close();
         
         InputStream is = new FileInputStream("testSearch");
         ApplicationList al = new ApplicationList();
         al.unmarshal(is);
         
         
    }
    
    
    public static void main(String args[]) throws Exception{
        create();
        search();
        destroy();

}
    
}
    
    
