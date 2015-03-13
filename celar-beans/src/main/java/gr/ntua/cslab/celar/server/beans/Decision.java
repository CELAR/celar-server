package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a 'Decision' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
public class Decision extends IDEntity{
    public String deployment_id;
    public int resizing_action_id, size;
    public MyTimestamp timestamp;
    
    /**
     * Default constructor for all DBEntities
     */
    public Decision(){
        super();
    }
    
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found with the given ID in this table
     */
    public Decision(int id) throws Exception{
        super(id);
    }
    
    /**
     * Create a Decision with a name from a Component object 
     * @param ra The resizing action that this decision chooses to do
     * @param dep the deployment that this decision is about
     * @param size the size (count) of the decision
     */
    public Decision(ResizingAction ra, Deployment dep, int size){
        this.deployment_id=dep.getId();
        this.resizing_action_id = ra.getId();
        this.deployment_id = dep.getId();
        this.size = size;
        timestamp = new MyTimestamp(System.currentTimeMillis());
    }


    
    
}
