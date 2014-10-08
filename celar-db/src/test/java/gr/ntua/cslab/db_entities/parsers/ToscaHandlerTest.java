/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities.parsers;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import static gr.ntua.cslab.db_entities.parsers.ApplicationParser.exportApplication;

/**
 *
 * @author cmantas
 */
public class ToscaHandlerTest {
    
    public ToscaHandlerTest() {
    }

    @Test
    //@Ignore
    public void test_00_showoff() {
        try {
            
            //creates the tosca handler object from a scar file path
            ToscaHandler tp = new ToscaHandler("testApp09.csar");
            
            //stores the application Description in the database
            tp.storeDescription();
            
            //stores the application Deployment in the database
            tp.storeDeployment();
            
            System.out.println(exportApplication(tp.getApplication()).toString(3));
            
            tp.removeDeployment();
            //removes the application description from the database
            tp.removeDescription();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("failed the showoff test case");
        }
        
    }
    
}
