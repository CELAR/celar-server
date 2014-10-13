package gr.ntua.cslab.db_entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * The Resource entity
 * @author cmantas
 */

/**
 * Entity representing an entry in the Dummy table 
 * @author cmantas
 */
public class Resource extends DBIDEntity{
    int deployment_Id, component_Id, provided_Resource_Id;
    Timestamp start_Time, end_Time;
    
    /**
     * Default constructor for all DBEntities
     */
    public Resource(){
        super();
    }
    
    /**
     * Constructs a Deployment from its father Application with a start time of now
     * @param depl
     * @param comp
     * @param provRes
     */
    public Resource(Deployment depl, Component comp, ProvidedResource provRes){
        super();
        deployment_Id = depl.getId();
        component_Id = comp.getId();
        provided_Resource_Id = provRes.getId();
        end_Time = null;
        start_Time = new Timestamp(System.currentTimeMillis());
        
    }
    
    /**
     * Creates an entity from a JSONObject
     * @param jo 
     */
    public Resource(JSONObject jo){
        super(jo);
    }
    
    public Resource(Map<String, String> inmap){
        super(inmap);
    }
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Resource(int id) throws DBException{
        super(id);
    }
    

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "RESOURCES";
    }
    
        /**
     * Gets the Resources
     * 
     * @param c the Comp
     * @return 
     * @throws gr.ntua.cslab.db_entities.DBException 
    */
    public static List<Resource> getByComponent(Component c) throws DBException{
        Resource dummy = new Resource();
        List<Resource> list = dummy.getByField("COMPONENT_id", ""+c.getId());
        return list;

    }

    
    
}
