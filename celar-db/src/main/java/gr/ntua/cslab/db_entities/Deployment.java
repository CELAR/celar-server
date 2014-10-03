/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.sql.Timestamp;
import java.util.Map;
import org.json.JSONObject;

/**
 * The Deployment entity
 * @author cmantas
 */

/**
 * Entity representing an entry in the Dummy table 
 * @author cmantas
 */
public class Deployment extends DBIDEntity{
    String applicationId;
    Timestamp startTime, endTime;
    
    /**
     * Default constructor for all DBEntities
     */
    public Deployment(){
        super();
    }
    
    /**
     * Constructs a Deployment from its father Application with a start time of now
     * @param app
     */
    public Deployment(Application app){
        super();
        applicationId = app.getId();
        endTime = null;
        startTime = new Timestamp(System.currentTimeMillis());
        
    }
    
    /**
     * Creates an entity from a JSONObject
     * @param jo 
     */
    public Deployment(JSONObject jo){
        super(jo);
    }
    
    public Deployment(Map<String, String> inmap){
        super(inmap);
    }
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Deployment(int id) throws DBException{
        super(id);
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
        this.applicationId=fields.get("APPLICATION_id");
        this.startTime = Timestamp.valueOf(fields.get("start_time"));
        String endTimeString = fields.get("end_time");
        if(endTimeString==null)
            this.endTime = null;
        else this.endTime = Timestamp.valueOf(fields.get("end_time"));
    }

    /**
     * creates a map of field--> value for all the fields of the Entity
     * @return 
     */
    @Override
    protected Map<String, String> toMap() {
        Map<String, String> m = new java.util.TreeMap();
        m.put("id", ""+id);
        m.put("APPLICATION_id", applicationId);
        m.put("start_time", ""+startTime);
        m.put("end_time", ""+endTime);
        return m;
    }

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "DEPLOYMENT";
    }
    
    

    
    
}
