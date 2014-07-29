/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.Application;
import gr.ntua.cslab.db_entities.DBException;
import gr.ntua.cslab.db_entities.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author cmantas
 */
@Ignore 
public class ApplicationTest {
    
    public ApplicationTest() {
    }

    @Test
    public void test_01_create_store_delete() {
        try {
            User u = new User("chris");
            u.store();
            Application app = new Application("this  is a dummy app", u);
            app.store();
            app.delete();
            u.delete();
        } catch (DBException ex) {
            System.out.println(ex);
            fail("failed to store application");
        }
    }
    
    @Test
    public void test_02_JSON(){
        try {
            User u = new User("chris");
            Application app = new Application("this  is a dummy app", u);
            Application app2 = new Application(app.toJSONObject());
            assertTrue(app.equals(app2));
            u.store();
        } catch (DBException ex) {
            System.out.println(ex);
            fail("failed to store application");
        }
        
    }

}
