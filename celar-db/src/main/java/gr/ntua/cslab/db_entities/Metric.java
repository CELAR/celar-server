/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
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
    int component_ID;
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
        this.component_ID=c.getId();
        this.timestamp = new Timestamp(System.currentTimeMillis());
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
