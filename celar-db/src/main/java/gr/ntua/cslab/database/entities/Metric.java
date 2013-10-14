package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.MetricsTable;
import gr.ntua.cslab.database.Tables;
import java.sql.Timestamp;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Metric extends DBIDEntity{
	
	int componentId;
	Timestamp timestamp;

	/**
	 * Creates an unstored resource from a type, submitted and the resource table
	 * @param component the component this metric is from 
	 */
	public Metric(Component component, Timestamp timestamp) 
							throws NotInDBaseException {
		super(Tables.metricsTable);
		//check if the component is not strored in the database (for consistency reasons)
		if(component.id==0||component.modified)
			throw new NotInDBaseException("the component must be stored "
				+ "in Database before the resource is created");
		this.componentId=component.id;
		this.timestamp=timestamp;
	}

	
	
	/**
	 * Creates an previously stored resource directly from  the database
	 * @param id
	 */
	public Metric(int id){
		super(id, Tables.metricsTable);
	}
	
        /**
         * creates an unstored Metric from a json object
         * @param jo
         * @throws NotInDBaseException 
         */
        public Metric(JSONObject jo) {
            super(jo, Tables.metricsTable);
        }
        

	/**
	 * Stores the resource in the database and retrieves the id it was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		MetricsTable t=(MetricsTable) table;
		this.id=t.insertMetric(componentId, timestamp);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "Metric id:"+id+" componentid:"+componentId
			+" timestamp :"+timestamp;
	}


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.componentId=Integer.parseInt(fields.get("COMPONENT_id"));
		this.timestamp= Timestamp.valueOf(fields.get("timestamp"));
	}

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("COMPONENT_id",componentId);
        json.put("timestamp", timestamp);    
        return json;
    }

   
        @Override
        void fromJSON(JSONObject jo)  {   
            String timestampStr=jo.getString("timestamp");
            this.timestamp=Timestamp.valueOf(timestampStr);
            componentId=jo.getInt("COMPONENT_id");
    }
        
  


	
}
