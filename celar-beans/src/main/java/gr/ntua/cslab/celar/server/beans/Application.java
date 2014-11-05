package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents an 'Application' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Application extends ReflectiveEntity {
    /**
     * The application fields
     */
    public String id, description;
    //@XmlTransient
    public String description_file_location;
    public MyTimestamp submitted;
    public int user_Id;
    public int unique_Id, major_Version, minor_Version;

    /**
     * The default constructor
     */
    public Application() {
        super();
    }
    

    /**
     * Copy constructor
     * @param app 
     */
   public Application(Application app) {
        super(app);
    }
        
    /**
     * Creates an Application assuming that all its fields are known
     * @param uniqueId
     * @param majorVersion
     * @param minorVersion
     * @param description
     * @param submitted
     * @param user 
     * @param description_file_location the location where the description file is stored in the celar server
     */
    public Application(int uniqueId, int majorVersion, int minorVersion, String description, MyTimestamp submitted, User user, String description_file_location) {
        this.description = description;
        this.submitted = submitted;
        this.user_Id = user.id;
        this.unique_Id = uniqueId;
        this.major_Version = majorVersion;
        this.minor_Version = minorVersion;
        this.description_file_location = description_file_location;
        setId();
    }
    
   /**
    * Creates an application based on its description, timestamp and father User.
    * The given version is 0.0 and the uninqueId is automatically created
    * @param description
    * @param submitted
    * @param user 
     * @param description_file_location the location where the description file is stored in the celar server
    */
    public Application(String description, MyTimestamp submitted, User user, String description_file_location) {
        this(0,0,0, description, submitted, user, description_file_location);
    }
    
   /**
    * Creates an application based on its description, and father User.
    * The given version is 0.0, the uninqueId is automatically created and the 
    * timestamp is the current time
    * @param description
    * @param user
     * @param description_file_location the location where the description file is stored in the celar server
     * */
    public Application(String description, User user, String description_file_location) {
        this(0,0,0, description, new MyTimestamp(System.currentTimeMillis()), user, description_file_location);
    }
    
       /**
    * Creates an application based on its description, and father User.
    * The given version is 0.0, the uninqueId is automatically created and the 
    * timestamp is the current time
    * @param description
    * @param user
     * */
    public Application(String description, User user) {
        this(0,0,0, description, new MyTimestamp(System.currentTimeMillis()), user, null);
    }
    
    /**
     * Creates an application based on its versions, description, and father User. 
     * @param majorVersion
     * @param minorVersion
     * @param description
     * @param user 
     * @param description_file_location 
     */
    public Application(int majorVersion, int minorVersion, String description, User user, String description_file_location) {
        this(0,majorVersion,minorVersion, description, new MyTimestamp(System.currentTimeMillis()), user, description_file_location);
    }
    
    /**
     * The human readable description of the application
     * @return 
     */
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String d) {
        description = d;
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
    public MyTimestamp getSubmitted() {
        return submitted;
    }
    
   public void setSubmitted(MyTimestamp s) {
        submitted = s;
    }


    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public int getUnique_Id() {
        return unique_Id;
    }

    public void setUnique_Id(int unique_Id) {
        this.unique_Id = unique_Id;
        setId();
    }

    public int getMajor_Version() {
        return major_Version;
    }

    public void setMajor_Version(int major_Version) {
        this.major_Version = major_Version;
        setId();
    }

    public int getMinor_Version() {
        return minor_Version;        
    }

    public void setMinor_Version(int minor_Version) {
        this.minor_Version = minor_Version;
        setId();
    }
    
    private void setId(){
        id = makeStringID(unique_Id, major_Version, minor_Version);
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
    
//    public int[] breakId(){
//        int[] rv =new int[3];
//        int di;
//        String s = new String(id);
//        for(int i=0; i <3; i++){
//            di= s.indexOf(".");
//            di= di==-1?s.length():di;
//            rv[i] = Integer.parseInt(s.substring(0, di));
//            if (di==s.length()) break;
//            s=s.substring(di+1, s.length());
//        }        
//        return rv;
//    }
    
}
