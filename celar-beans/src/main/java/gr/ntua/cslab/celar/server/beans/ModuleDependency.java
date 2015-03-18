package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a 'Module Dependency' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
public class ModuleDependency extends ReflectiveEntity{
    
    public int module_from_Id, module_to_Id;
    public String type;

    public ModuleDependency(){}
    
    public ModuleDependency(Module from, Module to, String type) {
        module_from_Id= from.getId();
        module_to_Id = to.getId();
        this.type=type;
    }


    
}
