/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database.parsers;

import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.ResourceType;
import gr.ntua.cslab.celar.server.beans.Spec;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import static gr.ntua.cslab.database.EntityTools.store;
import gr.ntua.cslab.database.parsers.ResourceParsers;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author cmantas
 */
@Ignore 
public class ResourceParsersTest {
    
    public ResourceParsersTest() {
    }


    @Test
    public void testExportProvidedResources() {
        try {
            ResourceType resourceType = new ResourceType("res_type");
            store(resourceType);
            ProvidedResource providedResource = new ProvidedResource("sample_vm_image", resourceType);
            store(providedResource);
            Spec spec = new Spec(providedResource, "cpu_count", "2");
            store(spec);
            Spec spec2 = new Spec(providedResource, "ram_thing", "3");
            store(spec2);
            
            System.out.println(ResourceParsers.exportProvidedResourcesByType(resourceType.getTypeName()).toString(3));
            //System.out.println(ResourceParsers.exportProvidedResourcesByType("VM_IMAGE").toString(3));
            
            delete(spec2);
            delete(spec);
            delete(providedResource);
            delete(resourceType);
        } catch (DBException ex) {
            System.out.println(ex);
            fail("failed to parse resources");
        }
    }
    
}

