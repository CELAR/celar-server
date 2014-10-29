package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Resource;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmantas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ApplicationInfo extends Application{
    
    public List<ModuleInfo> modules = new java.util.LinkedList();
        
    
    /**
     * Default contstructor
     */
    public ApplicationInfo(){}
    
    
   /**
    * Creates an applicationInfo object based on an Application
     * @param app
    */
    public ApplicationInfo(Application app){
        super(app);
    }
    
    public void addModule(ModuleInfo m){
        modules.add(m);
    }
    
    public List<ModuleInfo> getModules(){
        return modules;
    }
    
    public void setModules(List<ModuleInfo> m){
        modules = m;
    }
    
    public List<Resource> getAllResources(){
        List<Resource> rv = new java.util.LinkedList<>();
        for(ModuleInfo mi: modules)
            for(ComponentInfo ci: mi.components)
                rv.addAll(ci.resources);
        return rv;
    }
    
    public static void main(String args[]) throws Exception{

    }

}
