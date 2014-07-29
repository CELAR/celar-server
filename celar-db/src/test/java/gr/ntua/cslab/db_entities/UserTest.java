/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.DBException;
import gr.ntua.cslab.db_entities.User;
import java.util.List;
import java.util.Map;
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
// important that the tests run in this order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
    
    


    
        /**
     * Test of fromMap method, of class User.
     */
    @Test
    public void test_02_FromMap() {
        System.out.println("fromMap");
        Map<String, String> fields = null;
        User instance = new User("christaras");
        instance = new User(instance.toJSONObject());
        // TODO review the generated test code and remove the default call to fail.
        System.out.println(instance);
    }
    
    @Test
    public void test_03_store_delete(){
        User user = new User("christos");
        try {
            user.store();
            user.delete();
        } catch (DBException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            fail("failed to store user");
        }
        System.out.println(user);
        
    }
    
    @Test
    public void test_03_retrieve() throws InstantiationException, IllegalAccessException{
        User user = new User("christos");
        try {
            user.store();
            User user2 = new User(user.getId());
            assertTrue("retrieved user does not equal stored", user2.equals(user));
            
            User user3 = User.getByName("christos");
            System.out.println("Users found with name: "+ user3);
            
            user.delete();
        } catch (DBException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            fail("failed to store user");
        }
        System.out.println(user);
        
    }
    
    @Test
    public void test_05_getAll(){
        System.out.println(User.getAllUsers());
    }



    
}
