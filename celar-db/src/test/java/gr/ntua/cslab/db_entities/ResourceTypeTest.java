/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.ResourceType;
import gr.ntua.cslab.db_entities.DBException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author cmantas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceTypeTest {
    
    public ResourceTypeTest() {
    }

    @Test
    public void test_01_create_store_load_delete() {
        ResourceType rt = new ResourceType("vm_type");
        try {
            rt.store();
        } catch (DBException ex) { 
            System.out.println(ex);
            fail("could not store resource type");
        }
        
        //load from id and check if equals to the original
        try {
             ResourceType rt2 = new ResourceType(rt.getId());
             assertTrue(rt2.equals(rt));
        } catch (DBException ex) { 
            System.out.println(ex);
            fail("could not store resource type");
        }
        
        try {
            rt.delete();
        } catch (DBException ex) {
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
            rt.store();
            ResourceType rt2 = ResourceType.getByName(name);
            assertTrue(rt.equals(rt2));
            rt2.delete();
            
        } catch (DBException ex) {
            fail("failure in creating and retreiving resource type by name");
        }
    }
    
}
