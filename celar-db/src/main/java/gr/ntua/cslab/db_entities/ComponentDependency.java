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
public class ComponentDependency extends Dependency{

    public ComponentDependency(Component from, Component to) {
        super(from, to);
    }



    /**
     * looks up all the fields of the Entity from the input map and updates the 
     * relevant fields of this instance
     * @param fields 
     */
    @Override
    protected void fromMap(Map<String, String> fields) {
        this.from=Integer.parseInt(fields.get("COMPONENT_from_id"));
        this.to=Integer.parseInt(fields.get("COMPONENT_to_id"));
    }

    /**
     * creates a map of field--> value for all the fields of the Entity
     * @return 
     */
    @Override
    protected Map<String, String> toMap() {
        Map<String, String> m = new java.util.TreeMap();
        m.put("COMPONENT_from_id", ""+from);
        m.put("COMPONENT_to_id", ""+to);
        return m;
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
