package gr.ntua.cslab.celar.server.beans;

/**
 * Represents a 'Component Dependency' entity as it is stored in celarDB
 * @author cmantas
 */
public class ComponentDependency extends ReflectiveEntity{
    
    int component_from_id, component_to_id;

    public ComponentDependency(Component from, Component to) {
        component_from_id = from.getId();
        component_to_id = to.getId();
     }

    
    
}
