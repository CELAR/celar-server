package gr.ntua.cslab.celar.server.beans;

/**
 * Represents a 'User' entity as it is stored in celarDB
 * @author cmantas
 */
public class User extends IDEntity{
    
    /**
     * the user name
     */
    public String name, iaas_credentials;
    
    /**
     * default constructor
     */
    public User(){}
    
    /**
     * Creates an User entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found with the given ID in this table
     */
    public User(int id, String iaas_credentials) throws Exception{
        super(id);
        this.iaas_credentials = iaas_credentials;
    }
    
    
    /**
     * Creates a User entity by its name
     * @param name 
     */
    public User(String name){
        this.name=name;
    }
    
}
