package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.ProvidedResourceTable;
import gr.ntua.cslab.database.Tables;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class ProvidedResource extends DBIDEntity {
	
	String type, description;

	/**
	 * Creates an unstored providedResource from a type, submitted and the providedResource table
	 * @param type
	 * @param description
	 * @param table a providedResource table
	 */
	public ProvidedResource(String type, String description) throws NotInDBaseException {
		super(Tables.provResTable);
		this.type=type;
		this.description=description;
	}

	
	
	/**
	 * Creates an previously stored providedResource directly from  the database
	 * @param id
	 * @param table 
	 */
	public ProvidedResource(int id){
		super(id, Tables.provResTable);
	}
	

         /**
         * creates an unstored Proved Resource from a json object
         * @param jo
         */
        public ProvidedResource(JSONObject jo) {
            super(jo, Tables.provResTable);
        }
	

	/**
	 * Stores the providedResource in the database and retrieves the id he was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		ProvidedResourceTable t=(ProvidedResourceTable) table;
		this.id=t.insertProvidedResource(type,description);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "ProvidedResource: "+type+", id:"+id+" description:"+description;
	}


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.type=fields.get("type");
		this.description=fields.get("description");
	}

    @Override
    public JSONObject toJSONObject() {
       JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("type", type);
        json.put("description", ""+description);
        return json;
    }

    @Override
    void fromJSON(JSONObject jo) {
        this.description=jo.getString("description");
        this.type=jo.getString("type");
    }

    /**
     * gets all the provided resources of the given type
     * 
    */
    static List<ProvidedResource> getByType(String type){
        List<ProvidedResource> prs=new java.util.LinkedList();
        List<Integer> pr_ids= Tables.provResTable.getIdsForSelection("type", type);
        for(Integer id: pr_ids){
            prs.add(new ProvidedResource(id));
        }
        return prs;
    }

	
}
