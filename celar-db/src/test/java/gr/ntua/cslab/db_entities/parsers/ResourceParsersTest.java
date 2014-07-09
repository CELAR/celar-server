/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities.parsers;

import gr.ntua.cslab.db_entities.parsers.ResourceParsers;
import gr.ntua.cslab.db_entities.DBException;
import gr.ntua.cslab.db_entities.ProvidedResource;
import gr.ntua.cslab.db_entities.ResourceType;
import gr.ntua.cslab.db_entities.Spec;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cmantas
 */
public class ResourceParsersTest {
    
    public ResourceParsersTest() {
    }


    @Test
    public void testExportProvidedResources() {
        try {
            ResourceType resourceType = new ResourceType("res_type");
            resourceType.store();
            ProvidedResource providedResource = new ProvidedResource("sample_vm_image", resourceType);
            providedResource.store();
            Spec spec = new Spec(providedResource, "cpu_count", "2");
            spec.store();
            Spec spec2 = new Spec(providedResource, "ram_thing", "3");
            spec2.store();
            
            System.out.println(ResourceParsers.exportProvidedResourcesByType(resourceType.getTypeName()).toString(3));
            //System.out.println(ResourceParsers.exportProvidedResourcesByType("VM_IMAGE").toString(3));
            
            spec2.delete();
            spec.delete();
            providedResource.delete();
            resourceType.delete();
        } catch (DBException ex) {
            System.out.println(ex);
            fail("failed to parse resources");
        }
    }
    
}

