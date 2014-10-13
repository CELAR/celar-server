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
public class ComponentDependency extends DBEntity{
    
    int component_from_id, component_to_id;

    public ComponentDependency(Component from, Component to) {
        component_from_id = from.getId();
        component_to_id = to.getId();
     }




    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "COMPONENT__DEPENDENCY";
    }

    
    
}
