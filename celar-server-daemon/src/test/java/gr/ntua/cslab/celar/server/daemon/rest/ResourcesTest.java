/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.ResourceType;
import gr.ntua.cslab.celar.server.beans.Spec;
import gr.ntua.cslab.celar.server.beans.structured.ProvidedResourceInfo;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import static gr.ntua.cslab.celar.server.daemon.rest.ApplicationTest.spec1;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntitySearchers.searchProvidedResourceSpecs;
import static gr.ntua.cslab.database.EntityTools.delete;
import static gr.ntua.cslab.database.EntityTools.store;
import java.util.List;

/**
 *
 * @author cmantas
 */
public class ResourcesTest {
    
        static ResourceType resourceType;
    static ProvidedResource providedResource;
    static Spec spec1, spec2;
    
    
    static void create() throws DBException{
                //create and store a Resource type
        resourceType = new ResourceType("VM_IMAGE");
        store(resourceType);

        // create and store a Provided resource
        providedResource = new ProvidedResource("sample_vm_image", resourceType);
        store(providedResource); 

//
        //create and store some specs 
        spec1 = new Spec(providedResource, "cpu_count", "2");
        store(spec1);
        spec2 = new Spec(providedResource, "ram_size", "3");
        store(spec2);
        
    }
    
    static void destroy() throws DBException{
            // delete resources. etc
            delete(spec1);
            delete(spec2);
            delete(providedResource);
            delete(resourceType);
    }
    
    public static void main(String args[]) throws Exception{
        create();
        REList<ProvidedResourceInfo> prl = Resources.getImages();
        prl.marshal(System.out);        
        
        destroy();

    }
}
