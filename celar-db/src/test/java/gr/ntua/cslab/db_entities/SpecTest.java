/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

import gr.ntua.cslab.db_entities.DBTools.Constrain;

/**
 *
 * @author cmantas
 */
public class SpecTest {
    
    public SpecTest() {
    }

    @Test
    public void testSomeMethod() throws DBException {
        
        Spec c = new Spec();
        List<Integer> prids = Spec.getProvidedResourceIDsByFlavor(2, 1024, 20);
        System.out.println(prids);
        

    }
    
}
