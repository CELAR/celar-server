package gr.ntua.cslab.celar.server.beans;

/**
 *
 * @author cmantas
 */
public class ResourceType extends IDEntity{

    /**
     * The type name of this ResourceType
     */
    public String type;
    
    /**
     * The default constructor
     */
    public ResourceType(){
        super();
    }
    
    /**
     * Creates a ResourceType Entity from the given string
     * @param type 
     */
    public ResourceType(String type){
        this.type=type;
    }
    
    /**
     * Creates a ResourceType entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found with the given ID in this table
     */
    public ResourceType(int id) throws Exception{
        super(id);
    }

    
    /**
     * Returns the string type of this ResourceType
     * @return 
     */
    public String getTypeName(){
        return this.type;
    }
    
}
