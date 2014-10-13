/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
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
import org.json.JSONObject;

/**
 * Entity representing an Entry in the Component table
 * @author cmantas
 */
public class Component extends DBIDEntity{
    
    /**
     * The fields of the Entity
     */
    int module_Id, resource_Type_Id;
    String description;
    
    /**
     * Default constructor
     */
    public Component(){
        super();
    }
    
    /**
     * Creates a Component entity from its father Module, its father ResourceType
     * and a description
     * 
     * @param module 
     * @param description
     * @param rt 
     */
    public Component(Module module, String description, ResourceType rt){
                //check if the app is not strored in the database (for consistency reasons)
        this.module_Id = module.id;
        this.resource_Type_Id = rt.id;
        this.description=description;
    }
    
    /**
     * Creates a Component entity from a JSONObject
     * @param jo 
     */
    public Component(JSONObject jo){
        super(jo);
    }
    
    
    Component(Map<String, String> inmap){
        super(inmap);
    }
    
    /**
     * Creates an Component entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Component(int id) throws DBException{
        super(id);
    }

    /**
     * Retrieves the resource type by its name and creates a Component with that 
     * resource type and the given module and description
     * @param m
     * @param description
     * @param resourceTypeName
     * @throws DBException 
     */
    public Component(Module m, String description, String resourceTypeName) throws DBException {
        ResourceType rt = ResourceType.getByName(resourceTypeName);
        this.resource_Type_Id = rt.getId();
        this.description=description;
        this.module_Id = m.id;
    }


    @Override
    public String getTableName() {
        return "COMPONENT";
    }
    
    /**
     * Returns the id of the resource type that is father to this Component
     * @return 
     */
    public int getResourceTypeId(){
        return this.resource_Type_Id;
    }

    /**
     * Returns the id of the Module that is father to this Component
     * @return 
     */
    public int getModuleId() {
        return module_Id;
    }

    /**
     * Returns the description of this Component
     * @return 
     */
    public String getDescription() {
        return description;
    }
    
    
    /**
     * Returns a list of Components that have the given Module as their father
     * @param module the father of the given Components
     * @return
     * @throws DBException 
     */
    public static List<Component> getByModule(Module module) throws DBException {
        Component dummy = new Component();
        return dummy.<Component>getByField("MODULE_id", "" + module.getId());
    }
}
