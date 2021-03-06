package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a 'Metric' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
public class Metric extends IDEntity{
    public String name;
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
    
    /**
     * Create a metric with a name from a Component object 
     * @param c
     * @param name 
     */
    public Metric(Component c, String name){
        this.component_ID=c.getId();
        this.timestamp = new MyTimestamp(System.currentTimeMillis());
        this.name=name;
    }


    
    
}
