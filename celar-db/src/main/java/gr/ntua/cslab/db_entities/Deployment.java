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

import java.sql.Timestamp;
import java.util.Map;
import org.json.JSONObject;

/**
 * The Deployment entity
 * @author cmantas
 */

/**
 * Entity representing an entry in the Dummy table 
 * @author cmantas
 */
public class Deployment extends DBIDEntity{
    String application_Id;
    Timestamp start_Time ,end_Time;
    
    /**
     * Default constructor for all DBEntities
     */
    public Deployment(){
        super();
    }
    
    /**
     * Constructs a Deployment from its father Application with a start time of now
     * @param app
     */
    public Deployment(Application app){
        super();
        application_Id = app.getId();
        end_Time = null;
        start_Time = new Timestamp(System.currentTimeMillis());
        
    }
    
    /**
     * Creates an entity from a JSONObject
     * @param jo 
     */
    public Deployment(JSONObject jo){
        super(jo);
    }
    
    public Deployment(Map<String, String> inmap){
        super(inmap);
    }
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Deployment(int id) throws DBException{
        super(id);
    }
    
    

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "DEPLOYMENT";
    }
    
    

    
    
}
