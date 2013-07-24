package database;

import java.util.Map;

public class ProvidedResourceTable extends IDTable {
	
	public ProvidedResourceTable() {super();}
	
	
	public ProvidedResourceTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "PROVIDED_RESOURCE";
	}
	
	
	/**
	 * Inserts a provided resource  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
	 * @param type the type of the provided resource 
	 * @param description the description of this resource
	 * @return true if success false if not
	 */
	public boolean insertProvidedResource(int id, String type, String description){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("type",type);
		data.put("description",description);
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts a provided resource  with the specified data at the next available id 
	 * @param APPLICATION_id the component id that this component is from
	 * @param name the name of the component
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertProvidedResource( String type, String description){
		int id=this.getNextId();
		if(insertProvidedResource(id, type, description))
			return id;
		else return -1;
	}

}
