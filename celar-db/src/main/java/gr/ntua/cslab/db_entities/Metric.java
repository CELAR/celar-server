/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * The Metric entity
 * @author cmantas
 */

/**
 * Entity representing an entry in the Metric table 
 * @author cmantas
 */
public class Metric extends DBIDEntity{
    int componentID;
    Timestamp timestamp;
    
    /**
     * Default constructor for all DBEntities
     */
    public Metric(){
        super();
    }
    
    /**
     * Creates an entity from a JSONObject
     * @param jo 
     */
    public Metric(JSONObject jo){
        super(jo);
    }
    
    public Metric(Map<String, String> inmap){
        super(inmap);
    }
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Metric(int id) throws DBException{
        super(id);
    }
    
    
    public Metric(Component c){
        this.componentID=c.getId();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * looks up all the fields of the Entity from the input map and updates the 
     * relevant fields of this instance
     * @param fields 
     */
    @Override
    protected void fromMap(Map<String, String> fields) {
        if(fields.containsKey("id"))
            this.id=Integer.parseInt(fields.get("id"));
        this.componentID=Integer.parseInt(fields.get("COMPONENT_id"));
        this.timestamp = Timestamp.valueOf(fields.get("timestamp"));
    }

    /**
     * creates a map of field--> value for all the fields of the Entity
     * @return 
     */
    @Override
    protected Map<String, String> toMap() {
        Map<String, String> m = new java.util.TreeMap();
        m.put("id", ""+id);
        m.put("COMPONENT_id", ""+componentID);
        m.put("timestamp", ""+timestamp);
        return m;
    }

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "METRICS";
    }
    
    
    public static List<Metric> getByComponent(Component c){
        Metric dummy = new Metric();
        return dummy.<Metric>getByField("COMPONENT_id", "" + c.getId());
    }

    
    
}
