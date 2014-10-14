/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database;

import fasolakia.ResourceType;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getResourceTypeByName;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import static gr.ntua.cslab.database.EntityTools.store;
import static gr.ntua.cslab.database.EntityTools.store;
import static org.junit.Assert.*;
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
public class ResourceTypeTest {
    
    public ResourceTypeTest() {
    }

    @Test
    public void test_01_create_store_load_delete() {
        ResourceType rt = new ResourceType("vm_type");
        try {
            store(rt);
        } catch (DBException ex) { 
            System.out.println(ex);
            fail("could not store resource type");
        }
        
        //load from id and check if equals to the original
        try {
             ResourceType rt2 = new ResourceType(rt.getId());
             assertTrue(rt2.equals(rt));
        } catch (Exception ex) { 
            System.out.println(ex);
            fail("could not store resource type");
        }
        
        try {
            delete(rt);
        } catch (Exception ex) {
            System.out.println(ex);
            fail("could not delete resource type");
        }
        
    }
    
    @Test
    public void test_02_getByName(){
        try {
            System.out.println("testing resource Tpe getByName");
            String name = "custom_resource_name";
            ResourceType rt = new ResourceType(name);
            store(rt);
            ResourceType rt2 = getResourceTypeByName(name);
            assertTrue(rt.equals(rt2));
            delete(rt2);
            
        } catch (Exception ex) {
            fail("failure in creating and retreiving resource type by name");
        }
    }
    
}
