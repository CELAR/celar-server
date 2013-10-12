package database;

import database.entities.MetricValue;
import java.sql.Timestamp;
import java.util.Map;

public class MetricValueTable extends IDTable {
	
	public MetricValueTable() {super();}
	
	
	public MetricValueTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "METRIC_VALUES";
	}
	
	
	/**
	 * Inserts a metric value  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
	 * @param METRICS_id the metric this metric value is from
	 * @param RESOURCES_id the resource these metric value is taken from
	 * @param timestamp the timestamp these metric value was sampled at
	 * @return true if success false if not
	 */
	public boolean insertMetricValue(int id, int METRICS_id, int RESOURCES_id, Timestamp timestamp ){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("METRICS_id",Integer.toString(METRICS_id));
		data.put("RESOURCES_id",Integer.toString(RESOURCES_id));
		data.put("timestamp",timestamp.toString());
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts a metric value with the specified data at the next available id 
	 * @param METRICS_id the metric this metric value is from
	 * @param RESOURCES_id the resource these metric value is taken from
	 * @param timestamp the timestamp these metric value was sampled at
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertMetricValue(int METRICS_id, int RESOURCES_id, Timestamp timestamp){
		int id=this.getNextId();
		if(insertMetricValue(id, METRICS_id,  RESOURCES_id, timestamp))
			return id;
		else return -1;
	}

	public MetricValue getMetricValue(int id){
		return new MetricValue(id);
	}

}