package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.Application;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmantas
 */
@XmlRootElement
//@XmlAccessorType(XmlAccessType.PROPERTY)
public class ApplicationInfo extends Application implements Structured{
    
    public List<ModuleInfo> modules = new java.util.LinkedList();
        
    
    /**
     * Default contstructor
     */
    public ApplicationInfo(){}
    
    
   /**
    * Creates an applicationInfo object based on an Application
     * @param app
     * @throws java.lang.CloneNotSupportedException
    */
    public ApplicationInfo(Application app) throws CloneNotSupportedException{
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
    
    public static void main(String args[]) throws Exception{

    }
    
    public String printStructured(){
        return StrucuredPrinter.print(this, 0);
    }
    
    public String printStructured(int indent){
        return StrucuredPrinter.print(this, indent);
    }
}
