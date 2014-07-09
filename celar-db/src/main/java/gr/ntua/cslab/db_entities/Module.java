/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * Entity representing an entry in the Module table 
 * @author cmantas
 */
public class Module extends DBIDEntity{
    String name,applicationId;
    
    /**
     * Default constructor
     */
    public Module(){
        super();
    }
    
    /**
     * Creates a Module entity from a given name and its father "Application"
     * @param name the name of the module
     * @param app the Application instance whose child is this Module
     */
    public Module(String name, Application app){
        this.name=name;
        this.name=name;
        this.applicationId=app.id;
    }
    
    /**
     * Creates a Module entity from a JSONObject
     * @param jo 
     */
    public Module(JSONObject jo){
        super(jo);
    }
    
    
    Module(Map<String, String> inmap){
        super(inmap);
    }
    
    /**
     * Creates a Module entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Module(int id) throws DBException{
        super(id);
    }

    @Override
    protected void fromMap(Map<String, String> fields) {
        if(fields.containsKey("id"))
            this.id=Integer.parseInt(fields.get("id"));
        this.name = fields.get("name");
        this.applicationId = fields.get("APPLICATION_id");
    }

    @Override
    protected Map<String, String> toMap() {
       Map<String, String>  map = new java.util.TreeMap();
        map.put("id", ""+id);
        map.put("APPLICATION_id",""+applicationId);
        map.put("name",name);
        return map;
    }

    /**Returns the name of the table this Entity will be stored to
     * @return 
     */
    @Override
    public String getTableName() {
        return "MODULE";
    }
    
    /**
     * Returns a list of all the modules whose father is the given Application
     * @param app
     * @return a List of Modules
     * @throws DBException in case of an error
     */
    public static List<Module> getByApplication(Application app) throws DBException {
        List<Module> rv = new java.util.LinkedList();
        Module dummy = new Module();

        for(Object e: dummy.getByField("APPLICATION_id", ""+app.getId())){
            rv.add((Module) e);
        }
        return rv;
    }
}
