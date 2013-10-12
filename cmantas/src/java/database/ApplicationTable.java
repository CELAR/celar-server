package database;

import database.entities.Application;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ApplicationTable extends IDTable {
	
	public ApplicationTable() {super();}
	
	
	public ApplicationTable(boolean connect) {super(connect);}


	

	@Override
	protected String getTableName() {
		return "APPLICATION";
	}
	
	
	/**
	 * Inserts an application  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
	 * @param description the application description
	 * @param submitted the time submitted
	 * @param USER_id the foreign key of the owner
	 */
	public boolean insertApplication(int id, String description, Timestamp submitted, int USER_id){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("description",description);
		data.put("submitted",submitted.toString());
		data.put("USER_id",Integer.toString(USER_id));
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts an application  with the specified data next available id 
	 * @param description the application description 
	 * @param submitted the time submitted
	 * @param USER_id the foreign key of the owner
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertApplication(String description, Timestamp submitted, int USER_id){
		int id=this.getNextId();
		if(insertApplication(id,description, submitted, USER_id))
			return id;
		else return -1;
	}

	/**
	 * Inserts an application  with the specified data next available id, with current timestamp 
	 * @param description the application description
	 * @param USER_id the foreign key of the owner. 
	 */
	public int insertApplication(String description, int USER_id){
		int id=this.getNextId();
		if(insertApplication(id,description, new Timestamp(System.currentTimeMillis()), USER_id))
			return id;
		else return -1;
	}


	public Application getApplication(int id){
		return new Application(id);
	}

        
                /**
         * Retrieves all the Applications for the user with the given id
         * @param userId 
         */
        public  List<Application> getUserApplications(int userId) {  
            List<Application> results=new java.util.LinkedList();
            String field="id";
            String testField="USER_id";            
            List<String> IDs=doSelectEquals(field, testField, userId).get(field);
            //for each of the ids create the module
            for(String id : IDs){
                results.add(new Application(Integer.parseInt(id)));
            }
            return  results;
        }

}