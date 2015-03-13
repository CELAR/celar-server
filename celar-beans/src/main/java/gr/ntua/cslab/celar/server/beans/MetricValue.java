package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a 'Metric value' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricValue extends IDEntity{
    public int metrics_Id, resources_Id;
    public MyTimestamp timestamp;
    
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
        this.timestamp = new MyTimestamp(System.currentTimeMillis());
    }



    

}
