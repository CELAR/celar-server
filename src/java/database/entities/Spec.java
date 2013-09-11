package database.entities;

import database.SpecsTable;import database.Tables;
import java.util.Map;

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


	
}
