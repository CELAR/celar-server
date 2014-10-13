/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.util.Map;

/**
 * The dummy entity includes all the constructors and methods any DBIDEntity should have
 * @author cmantas
 */

/**
 * Entity representing an entry in the Dummy table 
 * @author cmantas
 */
public class ModuleDependency extends DBEntity{
    
    int module_from_Id, module_to_Id;

    public ModuleDependency(Module from, Module to) {
        module_from_Id= from.getId();
        module_to_Id = to.getId();
    }



    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "MODULE_DEPENDENCY";
    }

    
    
}
