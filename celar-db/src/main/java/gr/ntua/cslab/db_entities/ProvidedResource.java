/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
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
        int resource_Type_Id;
    
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
		this.resource_Type_Id=resourceType.getId();
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

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "PROVIDED_RESOURCE";
    }

    

    /**
     * Returns a List of all the ProvidedResources of the given type
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
    
    /**
     * Returns a List of all the provided (flavor) resources with the given that flavor parameters
     * @param cores
     * @param ram
     * @param disk
     * @return
     * @throws DBException 
     */
    public static List<ProvidedResource> getByFlavorInfo(int cores, int ram, int disk) throws DBException{
        List<ProvidedResource> rv = new java.util.LinkedList();
        for (Integer id: Spec.getProvidedResourceIDsByFlavor(cores, ram, disk)){
            rv.add(new ProvidedResource(id));
        }
        return rv;
    }

    public String getName() {
        return name;
    }

    public int getResourceTypeId() {
        return resource_Type_Id;
    }
    
    
}
