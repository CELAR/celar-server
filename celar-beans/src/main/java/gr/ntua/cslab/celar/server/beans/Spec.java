package gr.ntua.cslab.celar.server.beans;

/**
 *
 * @author cmantas
 */
public class Spec extends IDEntity {

    public int provided_Resource_Id;
    public String property, value;

    /**
     * Default Constructor
     */
    public Spec() {
        super();
    }

    /**
     * Creates a Spec entity based on its ProvidedResource father and 
     * the property - value pair for this Spec
     * @param pr
     * @param property
     * @param value 
     */
    public Spec(ProvidedResource pr, String property, String value) {
        this.provided_Resource_Id = pr.id;
        this.property = property;
        this.value = value;
    }


    /**
     * Creates a Spec entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found
     */
    public Spec(int id) throws Exception {
        super(id);
    }
    
    
}
