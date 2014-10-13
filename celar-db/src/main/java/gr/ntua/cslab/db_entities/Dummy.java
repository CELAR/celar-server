/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.util.Map;
import org.json.JSONObject;

/**
 * The dummy entity includes all the constructors and methods any DBIDEntity should have
 * @author cmantas
 */

/**
 * Entity representing an entry in the Dummy table 
 * @author cmantas
 */
public class Dummy extends DBIDEntity{
    String name;
    
    /**
     * Default constructor for all DBEntities
     */
    public Dummy(){
        super();
    }
    
    /**
     * Creates an entity from a JSONObject
     * @param jo 
     */
    public Dummy(JSONObject jo){
        super(jo);
    }
    
    public Dummy(Map<String, String> inmap){
        super(inmap);
    }
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Dummy(int id) throws DBException{
        super(id);
    }
    
    
    public Dummy(String name){
        this.name=name;
    }


    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "DUMMY";
    }

    
    
}
