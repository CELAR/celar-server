package gr.ntua.cslab.database;

import gr.ntua.cslab.database.entities.ResizingAction;
import java.util.Map;

public class ResizingActionsTable extends IDTable {
	
	public ResizingActionsTable() {super();}
	
	
	public ResizingActionsTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "RESIZING_ACTIONS";
	}
	
	
	/**
	 * Inserts a resizing action  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param id
	 * @param MODULE_id the id of the module this action resizes
	 * @param COMPONENT_id the id of the component this action resizes 
	 * @param type the type of the resizing action 
	 * @return true if success false if not
	 */
	public boolean insertResizingAction(int id, int MODULE_id, int COMPONENT_id, String type){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("id", Integer.toString(id));
		data.put("MODULE_id", Integer.toString(MODULE_id));
		data.put("COMPONENT_id", Integer.toString(COMPONENT_id));
		data.put("type", type);
		return	this.insertData(data);
	}
	

	
	/**
	 * Inserts a  resource  with the specified data at the next available id 
	 * @param APPLICATION_id the component id that this component is from
	 * @param name the name of the component
	 * @return the given id if successful, -1 if not. 
	 */
	public int insertResizingAction(int MODULE_id, int COMPONENT_id, String type){
		int id=this.getNextId();
		if(insertResizingAction(id, MODULE_id, COMPONENT_id, type))
			return id;
		else return -1;
	}

	public ResizingAction getResizingAction(int id){
		return new ResizingAction(id);	
	}

}
