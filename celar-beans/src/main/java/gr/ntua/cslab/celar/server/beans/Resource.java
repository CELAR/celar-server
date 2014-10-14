package gr.ntua.cslab.celar.server.beans;
import java.sql.Timestamp;


/**
 * The Resource entity
 * @author cmantas
 */
public class Resource extends IDEntity{
    public int deployment_Id, component_Id, provided_Resource_Id;
    public Timestamp start_Time, end_Time;
    
    /**
     * Default constructor for all DBEntities
     */
    public Resource(){
        super();
    }
    
    /**
     * Constructs a Deployment from its father Application with a start time of now
     * @param depl
     * @param comp
     * @param provRes
     */
    public Resource(Deployment depl, Component comp, ProvidedResource provRes){
        super();
        deployment_Id = depl.getId();
        component_Id = comp.getId();
        provided_Resource_Id = provRes.getId();
        end_Time = null;
        start_Time = new Timestamp(System.currentTimeMillis());
        
    }
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found with the given ID in this table
     */
    public Resource(int id) throws Exception{
        super(id);
    }
    
}
