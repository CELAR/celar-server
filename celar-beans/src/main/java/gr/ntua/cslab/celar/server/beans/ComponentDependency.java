package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a 'Component Dependency' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
public class ComponentDependency extends ReflectiveEntity{
    
   public  int component_from_id, component_to_id;
   public String type;
    
    public ComponentDependency(){}

    public ComponentDependency(Component from, Component to, String type) {
        component_from_id = from.getId();
        component_to_id = to.getId();
        this.type=type;
     }

    
    
}
