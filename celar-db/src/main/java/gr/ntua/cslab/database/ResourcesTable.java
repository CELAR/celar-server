package gr.ntua.cslab.database;

import gr.ntua.cslab.database.entities.Resource;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResourcesTable extends IDTable {
	
	public ResourcesTable() {super();}
	
	
	public ResourcesTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "RESOURCES";
	}
	
	
	/**
	 * Inserts a resource  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
	 * @param DEPLOYMENT_id the id of the deployment that this resource belongs to
	 * @param COMPONENT_id the id of the component that uses this resource
	 * @param PROVIDED_RESOURCE_id the id of the provided resource, 
	 * 				this resource is an istance of 
	 * @param start_time the time this resource was created
	 * @param end_time the time this resource was destroyed
	 * @return true if success false if not
	 */
	public boolean insertResource(int id, int DEPLOYMENT_id, int COMPONENT_id,
		int PROVIDED_RESOURCE_id, Timestamp start_time, Timestamp end_time){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("DEPLOYMENT_id", Integer.toString(DEPLOYMENT_id));
		data.put("COMPONENT_id", Integer.toString(COMPONENT_id));
		data.put("PROVIDED_RESOURCE_id", Integer.toString(PROVIDED_RESOURCE_id));
		data.put("start_time",start_time.toString());
		String end=end_time!=null?end_time.toString():"NULL";
		data.put("end_time",end);
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts a  resource  with the specified data at the next available id 
	 * @param APPLICATION_id the component id that this component is from
	 * @param name the name of the component
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertResource( int DEPLOYMENT_id, int COMPONENT_id,
		int PROVIDED_RESOURCE_id, Timestamp start_time, Timestamp end_time){
		int id=this.getNextId();
		if(insertResource(id, DEPLOYMENT_id, COMPONENT_id,
			PROVIDED_RESOURCE_id, start_time, end_time))
			return id;
		else return -1;
	}

	public Resource getResource(int id){
		return new Resource(id);
	}
        
                /**
         * Retrieves all the components for the module with the given id
         * @param moduleId
         */
        public List<Resource> getComponentResources(int ComponentId, Timestamp ts) {  
            List<Resource> results=new LinkedList();
            String field="id";
            String testField="COMPONENT_id";
            String condition="COMPONENT_id='"+ComponentId+"' AND start_time<='"+ts+"' AND (end_time>='"+ts+"' OR end_time IS NULL)";
            List<String> IDs=doSelect(field, condition).get(field);
            //for each of the ids create the component
            for(String id : IDs){
                results.add(new Resource(Integer.parseInt(id)));
            }
            return  results;
        }

}
