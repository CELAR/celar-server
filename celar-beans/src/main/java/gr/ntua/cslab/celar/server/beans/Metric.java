package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  The Metric entity 
 * @author cmantas
 */
@XmlRootElement
public class Metric extends IDEntity{
    public int component_ID;
    public MyTimestamp timestamp;
    
    /**
     * Default constructor for all DBEntities
     */
    public Metric(){
        super();
    }
    
    

    /**
     * Creates an entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception in case no Entity is found with the given ID in this table
     */
    public Metric(int id) throws Exception{
        super(id);
    }
    
    
    public Metric(Component c){
        this.component_ID=c.getId();
        this.timestamp = new MyTimestamp(System.currentTimeMillis());
    }


    
    
}
