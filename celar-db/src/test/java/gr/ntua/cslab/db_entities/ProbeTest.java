/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.Probe;
import gr.ntua.cslab.db_entities.DBException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author cmantas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProbeTest {
    
    public ProbeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of fromMap method, of class Probes.
     */
    @Test
    @Ignore
    public void test_02_maping() {
        System.out.println("fromMap");
        Probe instance = new Probe("my_probe", "dummy content of details");
//        Probe pr2 = new Probe(instance.toJSONObject());
//        pr2.fromMap(pr2.toMap());
//        System.out.println(pr2);
        
    }
    
    @Test
    @Ignore
    public void test_02_toJSON(){
        Probe instance = new Probe("my_probe", "dummy content of details");
        System.out.println(instance.toJSONObject().toString(3));
    }
    
    @Test
    @Ignore
    public void test_04_store(){
        Probe instance = new Probe("my_probe", "dummy content of details");
        try {
            System.out.println("storing");
            instance.store();
        } catch (DBException ex) {
            ex.printStackTrace();
            fail("failed to store "+instance);
        }
    }

    
}
