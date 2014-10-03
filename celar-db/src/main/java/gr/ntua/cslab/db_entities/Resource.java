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
 * The Resource entity
 * @author cmantas
 */

/**
 * Entity representing an entry in the Dummy table 
 * @author cmantas
 */
public class Resource extends DBIDEntity{
    int deploymentId, componentId, providedResourceId;
    Timestamp startTime, endTime;
    
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
        deploymentId = depl.getId();
        componentId = comp.getId();
        providedResourceId = provRes.getId();
        endTime = null;
        startTime = new Timestamp(System.currentTimeMillis());
        
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
     * looks up all the fields of the Entity from the input map and updates the 
     * relevant fields of this instance
     * @param fields 
     */
    @Override
    protected void fromMap(Map<String, String> fields) {
        if(fields.containsKey("id"))
            this.id=Integer.parseInt(fields.get("id"));
        this.deploymentId=Integer.parseInt(fields.get("DEPLOYMENT_id"));
        this.componentId=Integer.parseInt(fields.get("COMPONENT_id"));
        this.providedResourceId=Integer.parseInt(fields.get("PROVIDED_RESOURCE_id"));        
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
        m.put("DEPLOYMENT_id", ""+deploymentId);
        m.put("COMPONENT_id", ""+componentId);
        m.put("PROVIDED_RESOURCE_id", ""+providedResourceId);
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
        return "RESOURCES";
    }
    
    

    
    
}
