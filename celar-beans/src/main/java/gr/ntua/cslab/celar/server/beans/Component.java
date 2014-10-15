package gr.ntua.cslab.celar.server.beans;

/**
 * Entity representing an Entry in the Component table
 * @author cmantas
 */
public class Component extends IDEntity{
    
    /**
     * The fields of the Entity
     */
    public int module_Id, resource_Type_Id;
    public String description;
    
    /**
     * Default constructor
     */
    public Component(){
        super();
    }
    
    public Component(int id) throws Exception{
        super(id);
    }
    
    /**
     * Creates a Component entity from its father Module, its father ResourceType
     * and a description
     * 
     * @param module 
     * @param description
     * @param rt 
     */
    public Component(Module module, String description, ResourceType rt){
                //check if the app is not strored in the database (for consistency reasons)
        this.module_Id = module.id;
        this.resource_Type_Id = rt.id;
        this.description=description;
    }


    
    /**
     * Returns the id of the resource type that is father to this Component
     * @return 
     */
    public int getResourceTypeId(){
        return this.resource_Type_Id;
    }

    /**
     * Returns the id of the Module that is father to this Component
     * @return 
     */
    public int getModuleId() {
        return module_Id;
    }

    /**
     * Returns the description of this Component
     * @return 
     */
    public String getDescription() {
        return description;
    }
    
    

}
