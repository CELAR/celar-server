package fasolakia;

/**
 * The ModuleDependency Entity
 * @author cmantas
 */
public class ModuleDependency extends ReflectiveEntity{
    
    public int module_from_Id, module_to_Id;

    public ModuleDependency(Module from, Module to) {
        module_from_Id= from.getId();
        module_to_Id = to.getId();
    }


    
}
