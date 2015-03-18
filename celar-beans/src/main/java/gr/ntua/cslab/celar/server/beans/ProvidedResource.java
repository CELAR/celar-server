package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a 'Provided Resource' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
public class ProvidedResource  extends IDEntity{
        /**
         * The values of the table fields
         */
        public String name;
        public int resource_Type_Id;
    
    /**
     *Default Entity Constructor
     */
    public ProvidedResource(){
        super();
    }
    
    /**
     * Creates an unstored providedResource from a given name and its
     * "Resourcetype" father
     *
     * @param name
     * @param resourceType
     */
	public ProvidedResource(String name, ResourceType resourceType){
		super();
		this.resource_Type_Id=resourceType.getId();
		this.name=name;
	}
    
    
    /**
     * Creates a ProvidedResource given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found with the given ID
     */
    public ProvidedResource(int id) throws Exception{
        super(id);
    }


    public String getName() {
        return name;
    }

    public int getResourceTypeId() {
        return resource_Type_Id;
    }
    
    
}
