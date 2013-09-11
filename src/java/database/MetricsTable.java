package database;

import database.entities.Metric;
import java.sql.Timestamp;
import java.util.Map;

public class MetricsTable extends IDTable {
	
	public MetricsTable() {super();}
	
	
	public MetricsTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "METRICS";
	}
	
	
	/**
	 * Inserts a metric  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
	 * @param COMPONENT_id the component this metric is for 
	 * @param timestamp the timestamp this metric was submitted 
	 * @return true if success false if not
	 */
	public boolean insertMetric(int id, int COMPONENT_id, Timestamp timestamp ){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("COMPONENT_id",Integer.toString(COMPONENT_id));
		data.put("timestamp",timestamp.toString());
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts a component  with the specified data next available id 
	 * @param COMPONENT_id the component this metric is for 
	 * @param timestamp the timestamp this metric was submitted 
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertMetric(int COMPONENT_id, Timestamp timestamp){
		int id=this.getNextId();
		if(insertMetric(id, COMPONENT_id,  timestamp))
			return id;
		else return -1;
	}

	public Metric getMetric(int id){
		return new  Metric(id);
	}

}