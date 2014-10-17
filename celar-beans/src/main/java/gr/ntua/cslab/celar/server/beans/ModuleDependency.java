package gr.ntua.cslab.celar.server.beans;

/**
 * Represents a 'Module Dependency' entity as it is stored in celarDB
 * @author cmantas
 */
public class ModuleDependency extends ReflectiveEntity{
    
    public int module_from_Id, module_to_Id;

    public ModuleDependency(Module from, Module to) {
        module_from_Id= from.getId();
        module_to_Id = to.getId();
    }


    
}
