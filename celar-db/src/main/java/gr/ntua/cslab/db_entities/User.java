package gr.ntua.cslab.db_entities;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * Represents an entity in the 'User' table
 * @author cmantas
 */
public class User extends DBIDEntity{
    
    /**
     * the user name
     */
    String name;
    
    /**
     * default constructor
     */
    public User(){
        super();
    }
    
    /**
     * Creates a user from a JSONObject
     * @param jo 
     */
    public User(JSONObject jo){
        super(jo);
    }
    
    
    User(Map<String, String> inmap){
        super(inmap);
    }
    
    /**
     * Creates an User entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public User(int id) throws DBException{
        super(id);
    }
    
    
    /**
     * Creates a User entity by its name
     * @param name 
     */
    public User(String name){
        this.name=name;
    }

    @Override
    protected void fromMap(Map<String, String> fields) {
        if(fields.containsKey("id"))
            this.id=Integer.parseInt(fields.get("id"));
        else this.id = -1;
        this.name=fields.get("name");
    }

    @Override
    protected Map<String, String> toMap() {
        Map<String, String> m = new java.util.TreeMap();
        m.put("id", ""+id);
        m.put("name", name);
        return m;
    }

    @Override
    public String getTableName() {
        return "USER";
    }

    

    /**
     * gets the user with the specified name (assumed unique)
     * 
     * @param name
     * @return a List of Users
     * @throws gr.ntua.cslab.db_entities.DBException 
    */
    public static User getByName(String name) throws DBException{
        User dummy = new User();
        List<User> list = dummy.getByField("name", name);
        if(list.isEmpty()){
           // throw new DBException(DBException.NO_SUCH_ENTRY, "No Users found with name: "+name);
            return null;
        }
        return list.get(0);

    }
    
    /**
     * gets all User entities
     * @return a List of Users
     */
    public static List<User> getAllUsers(){
        User dummy = new User();
        return dummy.<User>getAll();
    }
    
}
