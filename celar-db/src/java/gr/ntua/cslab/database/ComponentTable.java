package gr.ntua.cslab.database;

import gr.ntua.cslab.database.entities.Component;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ComponentTable extends IDTable {
	
	public ComponentTable() {super();}
	
	
	public ComponentTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "COMPONENT";
	}
	
	
	/**
	 * Inserts an component  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
         * @param description
	 * @param MODULE_id the component id that this component is from
	 * @param PROVIDED_RESOURCE_id the provided resource this component is an istance of
	 * @return true if success false if not
	 */
	public boolean insertComponent(int id, String description, int MODULE_id, int PROVIDED_RESOURCE_id){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("MODULE_id",Integer.toString(MODULE_id));
		data.put("PROVIDED_RESOURCE_id",Integer.toString(PROVIDED_RESOURCE_id));
                data.put("description",description);
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts an component  with the specified data next available id 
	 * @param APPLICATION_id the component id that this component is from
	 * @param name the name of the component
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertComponent(String description, int MODULE_id, int PROVIDED_RESOURCE_id){
		int id=this.getNextId();
		if(insertComponent(id, description, MODULE_id,  PROVIDED_RESOURCE_id))
			return id;
		else return -1;
	}


	public Component getComponent(int id){
		return new Component(id);
	}

        /**
         * Retrieves all the components for the module with the given id
         * @param moduleId
         * @return a list of components
         */
        public List<Component> getModuleComponents(int moduleId) {  
            List<Component> results=new LinkedList();
            String field="id";
            String testField="MODULE_id";            
            List<String> IDs=doSelectEquals(field, testField, ""+moduleId).get(field);
            //for each of the ids create the component
            for(String id : IDs){
                results.add(new Component(Integer.parseInt(id)));
            }
            return  results;
        }
        
        public Component getComponentByDescription(String description){
            System.out.println("geting description: "+description);
            List<Integer> IDs=getIdsForSelection("description",description);
            return new Component(IDs.get(0));
        }

}