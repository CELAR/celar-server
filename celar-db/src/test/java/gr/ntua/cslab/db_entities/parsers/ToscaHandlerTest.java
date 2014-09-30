/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities.parsers;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cmantas
 */
public class ToscaHandlerTest {
    
    public ToscaHandlerTest() {
    }

    @Test
    public void test_00_showoff() {
        try {
            
            //creates the tosca handler object from a scar file path
            ToscaHandler tp = new ToscaHandler("app_7.csar");
            
            //stores the application Description in the database
            tp.storeDescription();
            
            //stores the application Configuration in the database
            //unimplemented
            //tp.storeConfiguration();
            
            //removes the application description from the database
            tp.removeDescription();
        } catch (Exception ex) {
            fail("failed the showoff test case");
        }
        
    }
    
}
