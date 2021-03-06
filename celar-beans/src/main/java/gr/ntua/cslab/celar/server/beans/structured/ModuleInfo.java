
package gr.ntua.cslab.celar.server.beans.structured;

/**
 *
 * @author cmantas
 */

import gr.ntua.cslab.celar.server.beans.Module;
import java.util.List;

/**
 *
 * @author cmantas
 */
public class ModuleInfo extends Module{
    public List<ComponentInfo> components=new java.util.LinkedList();
    
    
    /**
     * Default constructor
     */
   public ModuleInfo(){}
    /**
     * Copy from father constructor
     * @param m 
     */
   public ModuleInfo(Module m){
        super(m);
    }
   
   public void addComponent(ComponentInfo c){
       components.add(c);
   }
   
   public List<ComponentInfo> getComponents(){
       return components;
   }
       
}
