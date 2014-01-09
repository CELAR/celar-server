package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.MetricValueTable;
import gr.ntua.cslab.database.Tables;
import java.sql.Timestamp;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class MetricValue extends DBIDEntity{
	
	int metricId, resourceId;
	Timestamp timestamp;

	/**
	 * Creates an unstored resource from a type, submitted and the resource table
	 * @param component the component this metric is from 
	 */
	public MetricValue(Metric metric, Resource resource, Timestamp timestamp) 
							throws NotInDBaseException {
		super(Tables.mvTable);
		//check if the component is not strored in the database (for consistency reasons)
		if(metric.id==0||metric.modified)
			throw new NotInDBaseException("the metric must be stored "
				+ "in Database before the metric value is created");
		if(resource.id==0||resource.modified)
			throw new NotInDBaseException("the resurce must be stored "
				+ "in Database before the metric value is created");
		this.metricId=metric.id;
		this.resourceId=resource.id;
		this.timestamp=timestamp;
	}

	
	
	/**
	 * Creates an previously stored resource directly from  the database
	 * @param id
	 */
	public MetricValue(int id){
		super(id, Tables.mvTable);
	}
	
        
        /**
         * creates an unstored MetricValue from a json object
         * @param jo
         * @throws NotInDBaseException 
         */
        public MetricValue(JSONObject jo) {
            super(jo, Tables.mvTable);
        }

	/**
	 * Stores the resource in the database and retrieves the id it was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		MetricValueTable t=(MetricValueTable) table;
		this.id=t.insertMetricValue(metricId,resourceId, timestamp);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "Metric value id:"+id+" metricId:"+metricId
			+"resourceId:"+resourceId+"timestamp :"+timestamp;
	}


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.metricId=Integer.parseInt(fields.get("METRICS_id"));
		this.resourceId=Integer.parseInt(fields.get("RESOURCES_id"));
		this.timestamp= Timestamp.valueOf(fields.get("timestamp"));
	}

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("METRICS_id", metricId);
        json.put("RESOURCES_id", resourceId);
        json.put("timestamp", ""+timestamp);        
        return json;
    }

   
       @Override
     void fromJSON(JSONObject jo)  {   
        this.metricId=jo.getInt("METRICS_id");
        this.resourceId=jo.getInt("RESOURCES_id");
        String timestampStr=jo.getString("timestamp");
        this.timestamp=Timestamp.valueOf(timestampStr);

    }


	
}
