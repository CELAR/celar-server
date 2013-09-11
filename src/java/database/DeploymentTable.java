package database;

import database.entities.Deployment;
import java.sql.Timestamp;
import java.util.Map;

public class DeploymentTable extends IDTable {
	
	public DeploymentTable() {super();}
	
	
	public DeploymentTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "DEPLOYMENT";
	}
	
	
	/**
	 * Inserts a deployment  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
	 * @param APPLICATION_id the application this is a deployment of
	 * @param start_time the timestamp this deployment was started
	 * @param end_time the timestamp this deployment was ended
	 * @return true if success false if not
	 */
	public boolean insertDeployment(int id, int APPLICATION_id, Timestamp start_time, Timestamp end_time){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("APPLICATION_id", Integer.toString(APPLICATION_id));
		data.put("start_time",start_time.toString());
		String end_time_string=(end_time!=null)?end_time.toString():"NULL";
		data.put("end_time", end_time_string);
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts a deployment  with the specified data at the next available id 
	 * @param APPLICATION_id the application this is a deployment of
	 * @param start_time the timestamp this deployment was started
	 * @param end_time the timestamp this deployment was ended
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertDeployment(int APPLICATION_id, Timestamp start_time, Timestamp end_time){
		int id=this.getNextId();
		if(insertDeployment(id, APPLICATION_id, start_time, end_time))
			return id;
		else return -1;
	}


	public Deployment getDeployment(int id){
		return new Deployment(id);
	}

}
