package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a 'ResizingAction' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
public class ResizingAction extends IDEntity{
    public String type;
    public int component_ID, module_id;
    
    /**
     * Default constructor for all DBEntities
     */
    public ResizingAction(){
        super();
    }
    
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found with the given ID in this table
     */
    public ResizingAction(int id) throws Exception{
        super(id);
    }
    
    /**
     * Create a ResizingAction with a name from a Component object 
     * @param c
     * @param m
     * @param name 
     */
    public ResizingAction(Component c, Module m, String name){
        this.component_ID=c.getId();
        this.module_id=m.getId();
        this.type=name;
    }


    
    
}
