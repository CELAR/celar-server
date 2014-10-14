package fasolakia;

/**
 * The Metric Value entity representing an entry in the Metric Value table 
 * @author cmantas
 */

public class MetricValue extends IDEntity{
    public int metrics_Id, resources_Id;
    
    /**
     * Default constructor for all DBEntities
     */
    public MetricValue(){
        super();
    }

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception 
     */
    public MetricValue(int id) throws Exception{
        super(id);
    }
    
    
    public MetricValue(Metric metric, Resource res){
        this.metrics_Id = metric.getId();
        this.resources_Id = res.getId();
    }


}
