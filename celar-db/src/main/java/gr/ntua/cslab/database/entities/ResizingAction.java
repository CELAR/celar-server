package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.ResizingActionsTable;
import gr.ntua.cslab.database.Tables;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class ResizingAction extends DBIDEntity{
	
	String type;
	int moduleId, componentId;

	/**
	 * Creates an unstored resizingAction from a type, submitted and the resizingAction table
	 * @param module the module this resizing action is for
	 * @param component the component this resizing action applies to
	 * @param table a resizingAction table
	 */
	public ResizingAction(Module module, Component component, String type) 
		throws NotInDBaseException {
		super(Tables.raTable);
		this.type=type;
		//check if the app is not strored in the database (for consistency reasons)
		if(module.id==0||module.modified)
			throw new NotInDBaseException("the module must be stored "
				+ "in Database before the resizingAction is created");
		if(component.id==0||component.modified)
			throw new NotInDBaseException("the component must be stored "
				+ "in Database before the resizingAction is created");		
		this.moduleId=module.id;
		this.componentId=component.id;
	}

	
	
	/**
	 * Creates an previously stored resizingAction directly from  the database
	 * @param id
	 * @param table 
	 */
	public ResizingAction(int id){
		super(id, Tables.raTable);
	}
	


	/**
	 * Stores the resizingAction in the database and retrieves the id it was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		ResizingActionsTable t=(ResizingActionsTable) table;
		this.id=t.insertResizingAction(moduleId, componentId, type);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "ResizingAction: "+type+", id:"+id+" module:"+moduleId+" compoenentID:"+componentId;
	}


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.type=fields.get("type");
		this.moduleId=Integer.parseInt(fields.get("MODULE_id"));
		this.componentId=Integer.parseInt(fields.get("COMPONENT_id"));
	}

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("MODULE_id", moduleId);
        json.put("COMPONENT_id", componentId);
        json.put("type",type);        
        return json;
    }

   
       @Override
     void fromJSON(JSONObject jo)  {   
        this.id=jo.getInt("id");
        this.moduleId=jo.getInt("MODULE_id");
        this.componentId=jo.getInt("COMPONENT_id");
        this.type=jo.getString("type");

    }


	
}
