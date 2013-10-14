package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.SpecsTable;
import gr.ntua.cslab.database.Tables;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Spec extends DBIDEntity{
	
	int providedResourceId;
	String  description;

	/**
	 * Creates an unstored spec from a provided resource, description and the spec table
	 * @param type
	 * @param description
	 * @param table a spec table
	 */
	public Spec(ProvidedResource pr, String description) throws NotInDBaseException {
		super(Tables.specsTable);
		if(pr.id==0||pr.modified)
			throw new NotInDBaseException("the provided resource must be stored "
				+ "in Database before the spec is created");
		this.providedResourceId=pr.id;
		this.description=description;
	}

	
	
	/**
	 * Creates an previously stored spec directly from  the database
	 * @param id
	 * @param table 
	 */
	public Spec(int id){
		super(id, Tables.specsTable);
	}
	
        
        /**
         * creates an unstored Spec from a json object
         * @param jo
         */
        public Spec(JSONObject jo) {
            super(jo, Tables.specsTable);
        }


	/**
	 * Stores the spec in the database and retrieves the id he was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		SpecsTable t=(SpecsTable) table;
		this.id=t.insertSpecs(providedResourceId,description);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "Spec, prov.Resource: "+providedResourceId+", id:"+id+" description:"+description;
	}


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.providedResourceId=Integer.parseInt(fields.get("PROVIDED_RESOURCE_id"));
		this.description=fields.get("description");
	}

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("PROVIDED_RESOURCE_id",providedResourceId);
        json.put("description", ""+description);
        return json;
    }

    @Override
    void fromJSON(JSONObject jo) {
        this.description=jo.getString("description");
        this.providedResourceId=jo.getInt("PROVIDED_RESOURCE_id");
    }


	
}
