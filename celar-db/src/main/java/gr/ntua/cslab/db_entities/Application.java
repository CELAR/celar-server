/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.sql.Timestamp;
import java.util.Map;
import org.apache.log4j.Logger;
import static org.apache.log4j.Level.*;
import org.json.JSONObject;


/**
 * Entity representing an entry in the Application table 
 * @author cmantas
 */
public class Application extends DBEntity {
    /**
     * The application fields
     */
    String id, description; /*the description*/
    Timestamp submitted;
    int userId, uniqueId, majorVersion, minorVersion;
    
    static Logger LOG = Logger.getLogger(Application.class);
    
    static{
        //LOG.setLevel(DEBUG);
    }

    /**
     * The default constructor
     */
    public Application() {
        super();
        id="0 ";
    }
    
    /**
     * Creates an Application Entity from a JSONObject
     * @param jo 
     */
    public Application(JSONObject jo){
        super(jo);
    }

    /**
     * Creates an Application assuming that all its fields are known
     * @param uniqueId
     * @param majorVersion
     * @param minorVersion
     * @param description
     * @param submitted
     * @param user 
     */
    public Application(int uniqueId, int majorVersion, int minorVersion, String description, Timestamp submitted, User user) {
        this.description = description;
        this.submitted = submitted;
        this.userId = user.id;
        this.uniqueId = uniqueId;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        
        if(this.uniqueId==0){
             String prevMax = DBTools.maxValue(this.getTableName(), "unique_id");
             this.uniqueId= prevMax!=null? Integer.parseInt(prevMax)+1:1;
             this.id = makeStringID(this.uniqueId, majorVersion, minorVersion);
        }
    }
    
   /**
    * Creates an application based on its description, timestamp and father User.
    * The given version is 0.0 and the uninqueId is automatically created
    * @param description
    * @param submitted
    * @param user 
    */
    public Application(String description, Timestamp submitted, User user) {
        this(0,0,0, description, submitted, user);
    }
    
   /**
    * Creates an application based on its description, and father User.
    * The given version is 0.0, the uninqueId is automatically created and the 
    * timestamp is the current time
    * @param description
    * @param user 
    */
    public Application(String description, User user) {
        this(0,0,0, description, new Timestamp(System.currentTimeMillis()), user);
    }

    @Override
    protected void fromMap(Map<String, String> fields) {
        this.id = fields.get("id");
        this.uniqueId = Integer.parseInt(fields.get("unique_id"));
        this.majorVersion = Integer.parseInt(fields.get("major_version"));
        this.minorVersion = Integer.parseInt(fields.get("minor_version"));
        this.description = fields.get("description");
        this.submitted = Timestamp.valueOf(fields.get("submitted"));
        this.userId = Integer.parseInt(fields.get("USER_id"));
    }

    @Override
    protected Map<String, String> toMap() {
        Map<String, String> map = new java.util.TreeMap();
        map.put("id",id);
        map.put("unique_id",""+uniqueId);
        map.put("major_version", ""+majorVersion);
        map.put("minor_version", ""+minorVersion);
        map.put("description", ""+description);
        map.put("submitted", submitted.toString());    
        map.put("USER_id", ""+userId);
        return map;
    }

    /**
     * The name of the table this application will be stored to 
     * @return 
     */
    @Override
    public String getTableName() {
        return "APPLICATION";
    }

    /**
     * The human readable description of the application
     * @return 
     */
    public String getDescription() {
        return description;
    }

    /**
     * The String id of the application
     * @return a string like this 0000000001.001.002, representing the 
     * uniqueID.majorVersion.minorVersion
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the timestamp of when this Application was submitted
     * @return 
     */
    public Timestamp getSubmitted() {
        return submitted;
    }

    public int getUserId() {
        return userId;
    }
    
    
    
     /**
     * Constructs the String "id" for the application table
     * @param uniqueId the unique application identifier
     * @param majorVersion the major version of the application
     * @param minorVersion the minor version of the application
     * @return an 18-digit String (uniqueId.majorVersion.minorVersion)based on the given parameters
     */
    
    public static String makeStringID(int uniqueId, int majorVersion, int minorVersion){
       String rv = String.format("%010d", uniqueId) +"."+
               String.format("%03d", majorVersion)+"."+
               String.format("%03d", minorVersion);
        return rv;
    }
    
    
}
