package fasolakia;

/**
 * The dummy entity includes all the constructors and methods any DBIDEntity should have
 * @author cmantas
 */

/**
 * Entity representing a ComponentDependency 
 * @author cmantas
 */
public class ComponentDependency extends ReflectiveEntity{
    
    int component_from_id, component_to_id;

    public ComponentDependency(Component from, Component to) {
        component_from_id = from.getId();
        component_to_id = to.getId();
     }

    
    
}
