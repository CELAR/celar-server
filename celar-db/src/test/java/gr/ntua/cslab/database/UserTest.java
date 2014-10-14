/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database;

import gr.ntua.cslab.database.DBException;
import fasolakia.User;
import static gr.ntua.cslab.database.EntityGetters.getAllUsers;
import static gr.ntua.cslab.database.EntityGetters.getUserByName;
import static gr.ntua.cslab.database.EntityTools.delete;
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
// important that the tests run in this order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
    
    


    
    
    @Test
    public void test_03_store_delete(){
        User user = new User("christos");
        try {
            store(user);
            delete(user);
        } catch (DBException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            fail("failed to store user");
        }
        System.out.println(user);
        
    }
    
    @Test
    public void test_03_retrieve() throws Exception{
        User user = new User("christos");
        try {
            store(user);
            User user2 = new User(user.getId());
            assertTrue("retrieved user does not equal stored", user2.equals(user));
            
            User user3 = getUserByName("christos");
            System.out.println("Users found with name: "+ user3);
            
            delete(user);
        } catch (DBException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            fail("failed to store user");
        }
        System.out.println(user);
        
    }
    
    @Test
    public void test_05_getAll(){
        System.out.println(getAllUsers());
    }



    
}
