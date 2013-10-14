/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.ComponentDependencyTable;
import gr.ntua.cslab.database.ModuleDependencyTable;
import gr.ntua.cslab.database.Tables;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Dependency extends DBEntity{
	String type, entity;
	int from, to;
        public final static String COMPONENT="COMPONENT";
        public final static String MODULE="MODULE";



    public Dependency(DBIDEntity from, DBIDEntity to, String type, String entity) {
        this.type = type;
        this.from = from.id;
        this.to = to.id;
        this.entity = entity;
        if (entity.equals("COMPONENT")) {
            table = Tables.compDep;
        } else if (entity.equals("MODULE")) {
            table = Tables.modDep;
        }
    }
    
    	/**
	 * Creates an entity from the fields of the database entry.
	 * Calls then fromMap method which is implemented in the subclasses
	 * @param fields
	 * @param table
	 */
	public Dependency(Map <String,String> fields, String entity){
		this.entity=entity;
                this.table=entity.equals(COMPONENT)?Tables.componentTable:Tables.moduleTable;
		fromMap(fields);
		modified=false;
        }
        
        public Dependency(JSONObject jo, String entity){
            this.entity=entity;
            fromJSON(jo);
            this.table=entity.equals(Dependency.COMPONENT)?Tables.compDep:Tables.modDep;
            modified=false;
        }

	@Override
	public String toString() {
		return  type + ":{" + from + "-->" + to + '}';
	}

    @Override
    protected void fromMap(Map<String, String> fields) {
                String fk=entity+"_from_id";
		this.from=Integer.parseInt(fields.get(fk));
		this.to=Integer.parseInt(fields.get(entity+"_to_id"));
		this.type= fields.get("type");
    }

    @Override
    public boolean store() {
        if(entity.equals("COMPONENT")){
        ComponentDependencyTable t=(ComponentDependencyTable) table;
	return t.insertModuleDependency(from, to, type);
        }else if (entity.equals("MODULE")){
            ModuleDependencyTable t=(ModuleDependencyTable) table;
            return t.insertModuleDependency(from, to, type);
        }else return false;
    }

    
        @Override
     public JSONObject toJSONObject() {
        String fromField=entity+"_from_id";
        String toField=entity+"_to_id";
        JSONObject json = new JSONObject();
        json.put(fromField, this.from);
        json.put(toField, this.to);
        json.put("type", ""+type);        
        return json;        
     }

    
        @Override
    void fromJSON(JSONObject jo) {
        String from=entity+"_from_id";
        String to=entity+"_to_id";
        this.from = jo.getInt(from);
        this.to = jo.getInt(to);
        this.type = jo.getString("type");      
    }

	
	
}
