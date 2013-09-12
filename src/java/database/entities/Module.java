package database.entities;

import database.ModuleTable;
import database.Tables;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Module extends DBIDEntity implements JSONExportable{
	
	String name;
	int applicationId;

	/**
	 * Creates an unstored module from a name, submitted and the module table
	 * @param name
	 * @param app
	 * @param table a module table
	 */
	public Module(String name, Application app) throws NotInDBaseException {
		super(Tables.moduleTable);
		this.name=name;
		//check if the app is not strored in the database (for consistency reasons)
		if(app.id==0||app.modified)
			throw new NotInDBaseException("the User must be stored "
				+ "in Database before the module is created");
		this.applicationId=app.id;
	}

	
	
	/**
	 * Creates an previously stored module directly from  the database
	 * @param id
	 * @param table 
	 */
	public Module(int id){
		super(id, Tables.moduleTable);
	}
	


	/**
	 * Stores the module in the database and retrieves the id he was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		ModuleTable t=(ModuleTable) table;
		this.id=t.insertModule(applicationId, name);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "Module: "+name+", id:"+id+" appId:"+applicationId;
	}


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.name=fields.get("name");
		this.applicationId=Integer.parseInt(fields.get("APPLICATION_id"));
	}

    @Override
    public JSONObject toJSONObject() {
       JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("APPLICATION_id",applicationId);
        json.put("name",name);
        return json;
    }


	
}
