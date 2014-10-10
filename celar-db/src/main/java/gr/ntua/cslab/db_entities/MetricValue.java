/*
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
 * The Metric Value entity representing an entry in the Metric Value table 
 * @author cmantas
 */

public class MetricValue extends DBIDEntity{
    int metricsId, resourcesId;
    
    /**
     * Default constructor for all DBEntities
     */
    public MetricValue(){
        super();
    }
    
    /**
     * Creates an entity from a JSONObject
     * @param jo 
     */
    public MetricValue(JSONObject jo){
        super(jo);
    }
    
    public MetricValue(Map<String, String> inmap){
        super(inmap);
    }
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public MetricValue(int id) throws DBException{
        super(id);
    }
    
    
    public MetricValue(Metric metric, Resource res){
        this.metricsId = metric.getId();
        this.resourcesId = res.getId();
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
        this.metricsId=Integer.parseInt(fields.get("METRICS_id"));
        this.resourcesId=Integer.parseInt(fields.get("RESOURCES_id"));
    }

    /**
     * creates a map of field--> value for all the fields of the Entity
     * @return 
     */
    @Override
    protected Map<String, String> toMap() {
        Map<String, String> m = new java.util.TreeMap();
        m.put("id", ""+id);
        m.put("METRICS_id", ""+metricsId);
        m.put("RESOURCES_id", ""+resourcesId);
        return m;
    }

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "METRIC_VALUES";
    }

    
    public static List<MetricValue> getByMetric(Metric m){
        MetricValue dummy = new MetricValue();
        return dummy.<MetricValue>getByField("METRICS_id", ""+m.getId());
    }
    
    
    public static List<MetricValue> getByMetric(Metric m, Timestamp start, Timestamp end){
        MetricValue dummy = new MetricValue();
        return dummy.<MetricValue>getByField("METRICS_id", ""+m.getId());
    }
    
    
}
