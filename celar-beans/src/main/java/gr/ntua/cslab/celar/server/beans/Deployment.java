package fasolakia;

import java.sql.Timestamp;

/**
 * The Deployment entity
 * @author cmantas
 */

public class Deployment extends IDEntity{
    public String application_Id;
    public Timestamp start_Time ,end_Time;
    
    /**
     * Default constructor for all DBEntities
     */
    public Deployment(){
        super();
    }
    
    /**
     * Constructs a Deployment from its father Application with a start time of now
     * @param app
     */
    public Deployment(Application app){
        super();
        application_Id = app.getId();
        end_Time = null;
        start_Time = new Timestamp(System.currentTimeMillis());
        
    }

    
    
}
