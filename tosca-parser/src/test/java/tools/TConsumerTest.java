/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author cmantas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TConsumerTest {
    
    public TConsumerTest() {
    }

    
     @org.junit.Test
    public void test_01_showCase(){
        
        try {
            CSARParser tc  = new CSARParser("src/main/resources/app_test.csar");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("failed the showcase");
        }
        
    }
    
    @org.junit.Test
    @Ignore
    public void test_99_nonexisting_csar() {
        try{
            CSARParser tc = new CSARParser("asdffasdgpkoljasdf");
        }
        catch (Exception e){
            //OK
            return;}
        fail("Did not catch innexistent file");
        
    }
    
}
