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
public abstract class Dependency extends DBEntity{
    int from, to ;
    
    /**
     * Default constructor for all DBEntities
     */
    public Dependency(){
        super();
    }
    
    /**
     * Creates an entity from a JSONObject
     * @param jo 
     */
    public Dependency(JSONObject jo){
        super(jo);
    }
    
    public Dependency(Map<String, String> inmap){
        super(inmap);
    }
    

    
    
    public Dependency(DBIDEntity from, DBIDEntity to){
        this.from = from.id;
        this.to = to.id;
    }
    
    
    
    
}
