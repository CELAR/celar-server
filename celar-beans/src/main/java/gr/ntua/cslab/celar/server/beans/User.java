package gr.ntua.cslab.celar.server.beans;

/**
 * Represents an entity in the 'User' table
 * @author cmantas
 */
public class User extends IDEntity{
    
    /**
     * the user name
     */
    public String name;
    
    /**
     * default constructor
     */
    public User(){}
    
    /**
     * Creates an User entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public User(int id) throws Exception{
        super(id);
    }
    
    
    /**
     * Creates a User entity by its name
     * @param name 
     */
    public User(String name){
        this.name=name;
    }
    
}
