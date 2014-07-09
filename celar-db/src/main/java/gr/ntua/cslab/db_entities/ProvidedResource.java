/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.util.List;
import java.util.Map;

/**
 *Entity representing an entry in the Provided Resource table
 * @author cmantas
 */

public class ProvidedResource  extends DBIDEntity{
        /**
         * The values of the table fields
         */
        String name;
        int resourceTypeId;
    
    /**
     *Default Entity Constructor
     */
    public ProvidedResource(){
        super();
    }
    
    /**
     * Creates an unstored providedResource from a given name and its
     * "Resourcetype" father
     *
     * @param name
     * @param resourceType
     * @throws gr.ntua.cslab.db_entities.DBException
     */
	public ProvidedResource(String name, ResourceType resourceType) throws DBException {
		super();
		this.resourceTypeId=resourceType.getId();
		this.name=name;
	}
    

    ProvidedResource(Map<String, String> inmap){
        super(inmap);
    }
    
    /**
     * Creates a ProvidedResource given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID
     */
    public ProvidedResource(int id) throws DBException{
        super(id);
    }
    
    
    @Override
    protected void fromMap(Map<String, String> fields) {
        if(fields.containsKey("id"))
            this.id=Integer.parseInt(fields.get("id"));
        else this.id = -1;
        this.resourceTypeId=Integer.parseInt( fields.get("RESOURCE_TYPE_id") );
	this.name=fields.get("name");
    }


    @Override
    protected Map<String, String> toMap() {
        Map<String, String> m = new java.util.TreeMap();
        m.put("id",""+id);
        m.put("RESOURCE_TYPE_id",""+this.resourceTypeId);
        m.put("name", name);
        return m;
    }

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "PROVIDED_RESOURCE";
    }

    

    /**
     * Returns a List of all the ProvidedResources of this type
     * 
     * @param type the ResourceType father of all the ProvidedResources returned
     * @return 
     * @throws gr.ntua.cslab.db_entities.DBException 
    */
    public static List<ProvidedResource> getByType(ResourceType type) throws DBException{
        List<ProvidedResource> rv = new java.util.LinkedList();
        ResourceType rt = ResourceType.getByName(type.type);
        ProvidedResource dummy = new ProvidedResource();
        List generic= dummy.getByField("RESOURCE_TYPE_id", ""+rt.getId());
        for(Object e: generic){
            rv.add((ProvidedResource) e);
        }
        return rv;
    }
}
