/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.util.List;
import java.util.Map;

/**
 *
 * @author cmantas
 */
public class ResourceType extends DBIDEntity{

    /**
     * The type name of this ResourceType
     */
    String type;
    
    /**
     * The default constructor
     */
    public ResourceType(){
        super();
    }
    
    /**
     * Creates a ResourceType Entity from the given string
     * @param type 
     */
    public ResourceType(String type){
        this.type=type;
    }
    
    ResourceType(Map<String, String> inmap){
        super(inmap);
    }
    
    /**
     * Creates a ResourceType entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public ResourceType(int id) throws DBException{
        super(id);
    }

    @Override
    public String getTableName() {
        return "RESOURCE_TYPE";
    }
    
    
    /**
     * Returns the string type of this ResourceType
     * @return 
     */
    public String getTypeName(){
        return this.type;
    }

    

    /**
     * Gets the resource type with the specified String type (assumed unique)
     * 
     * @param type
     * @return 
     * @throws gr.ntua.cslab.db_entities.DBException 
    */
    public static ResourceType getByName(String type) throws DBException{
        ResourceType dummy = new ResourceType();
        List<ResourceType> list = dummy.getByField("type", type);
        if(list.isEmpty()){
            throw new DBException(DBException.NO_SUCH_ENTRY, "No resources found of type: "+type);
        }
        return list.get(0);

    }
    
}
