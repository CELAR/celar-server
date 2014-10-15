package gr.ntua.cslab.celar.server.beans;

import java.sql.Timestamp;


/**
 * Entity representing an entry in the Application table 
 * @author cmantas
 */
public class Application extends ReflectiveEntity {
    /**
     * The application fields
     */
    public String id, description; /*the description*/
    public Timestamp submitted;
    public int user_Id;
    int unique_Id, major_Version, minor_Version;

    /**
     * The default constructor
     */
    public Application() {
        super();
        id="0 ";
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
        this.user_Id = user.id;
        this.unique_Id = uniqueId;
        this.major_Version = majorVersion;
        this.minor_Version = minorVersion;
        this.id = makeStringID(uniqueId, majorVersion, minorVersion);
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
    
    /**
     * Creates an application based on its versions, description, and father User. 
     * @param majorVersion
     * @param minorVersion
     * @param description
     * @param user 
     */
    public Application(int majorVersion, int minorVersion, String description, User user) {
        this(0,majorVersion,minorVersion, description, new Timestamp(System.currentTimeMillis()), user);
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
 uniqueID.major_Version.minor_Version
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
        return user_Id;
    }
    
    
    
     /**
     * Constructs the String "id" for the application table
     * @param uniqueId the unique application identifier
     * @param majorVersion the major version of the application
     * @param minorVersion the minor version of the application
     * @return an 18-digit String (uniqueId.major_Version.minor_Version)based on the given parameters
     */
    
    public static String makeStringID(int uniqueId, int majorVersion, int minorVersion){
       String rv = String.format("%010d", uniqueId) +"."+
               String.format("%03d", majorVersion)+"."+
               String.format("%03d", minorVersion);
        return rv;
    }
    
    public int[] breakId(){
        int[] rv =new int[3];
        int di;
        String s = new String(id);
        for(int i=0; i <3; i++){
            di= s.indexOf(".");
            di= di==-1?s.length():di;
            rv[i] = Integer.parseInt(s.substring(0, di));
            if (di==s.length()) break;
            s=s.substring(di+1, s.length());
        }        
        return rv;
    }
    
}
