package gr.ntua.cslab.celar.server.beans;


/**
 * Represents a 'Deployment' entity as it is stored in celarDB
 * @author cmantas
 */
public class Deployment extends ReflectiveEntity{
    public String id, application_Id;
    public MyTimestamp start_Time ,end_Time;
    
    /**
     * Default constructor for all DBEntities
     */
    public Deployment(){
        super();
    }
    
    /**
     * Constructs a Deployment from its father Application with a start time of now
     * @param app
     * @param deploymentid
     */
    public Deployment(Application app, String deploymentid){
        id = deploymentid;
        application_Id = app.getId();
        end_Time = null;
        start_Time = new MyTimestamp(System.currentTimeMillis());
        
    }

    public String getId(){
        return this.id;
    }
    
}
