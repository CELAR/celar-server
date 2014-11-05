/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.Module;
import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import gr.ntua.cslab.celar.server.beans.ResourceType;
import gr.ntua.cslab.celar.server.beans.Spec;
import gr.ntua.cslab.celar.server.beans.User;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.beans.structured.ComponentInfo;
import gr.ntua.cslab.celar.server.beans.structured.EntityList;
import gr.ntua.cslab.celar.server.beans.structured.ModuleInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author cmantas
 */

public class Testing {

    public static void main(String args[]) throws CloneNotSupportedException, JAXBException, FileNotFoundException, IOException {

        //create a user entity
        User user = new User("takis");
        ResourceType resourceType = new ResourceType("VM_IMAGE");
        ProvidedResource providedResource = new ProvidedResource("sample_vm_image", resourceType);
        Spec spec1 = new Spec(providedResource, "cpu_count", "2");

        ApplicationInfo app = new ApplicationInfo(new Application("test_application", user, "/dummy/location"));
//                app.marshal(System.out);

        ModuleInfo module = new ModuleInfo(new Module("test_module", app));

//                module.marshal(System.out);
        ComponentInfo component = new ComponentInfo(new Component(module, "test module", resourceType));

        module.addComponent(component);
        module.addComponent(component);
        app.addModule(module);
        app.addModule(module);
        System.out.println(app);

        //write out
        FileOutputStream fo = new FileOutputStream("test");
        System.out.println("Marshal");
        app.marshal(fo);
        app.marshal(System.out);
        fo.close();

        FileInputStream fi = new FileInputStream("test");
//                ReflectiveEntity appIn = ReflectiveEntity.unmarshal(fi);
//                System.out.println(appIn);

        ApplicationInfo inai = new ApplicationInfo();
        inai.unmarshal(fi);

        System.out.println(inai);
        System.out.println(inai.toString(true));

//                    Metric metric = new Metric(component);
//                    metric.marshal(System.out);
//                //write out
//                fo = new FileOutputStream("test_list");
//                EntityList tl = new EntityList();
//                tl.contents.add(tl);
//                tl.contents.add(tl);
//                tl.marshal(fo);
//                fo.close();
//                fi = new FileInputStream("test_list");
//                tl = new EntityList();
//                tl.unmarshal(fi);
//                System.out.println(tl);
        fi.close();
    }

}
