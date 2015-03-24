package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Represents a 'Deployment' entity as it is stored in celarDB
 * @author cmantas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Deployment extends ReflectiveEntity{
    public String id, application_Id, orchestrator_IP;
    public MyTimestamp start_Time ,end_Time;
    public String state;
    
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
     * @param orchestrator_IP
     */
    public Deployment(Application app, String deploymentid, String orchestrator_IP){
        id = deploymentid;
        this.orchestrator_IP= orchestrator_IP;
        application_Id = app.getId();
        end_Time = null;
        start_Time = new MyTimestamp(System.currentTimeMillis());
        
    }

    public String getId(){
        return this.id;
    }
       public void setId(String id){
        this.id= id;
    }

    public String getApplication_Id() {
        return application_Id;
    }

    public void setApplication_Id(String application_Id) {
        this.application_Id = application_Id;
    }

    public String getOrchestrator_IP() {
        return orchestrator_IP;
    }

    public void setOrchestrator_IP(String orchestrator_IP) {
        this.orchestrator_IP = orchestrator_IP;
    }

    public MyTimestamp getStart_Time() {
        return start_Time;
    }

    public void setStart_Time(MyTimestamp start_Time) {
        this.start_Time = start_Time;
    }

    public MyTimestamp getEnd_Time() {
        return end_Time;
    }

    public void setEnd_Time(MyTimestamp end_Time) {
        this.end_Time = end_Time;
    }

    public String getState() {
        return state;
    }

    
    public void setState(Object state) {
        if(state instanceof String) this.state=(String)state;
        else if(state==null) this.state=null;
        else this.state = state.toString();
    }
    
     String toString( int indent) {
         String s = super.toString(indent);    
         indent = indent>0?--indent:0;
        String sindent = indent>0?"|":"";
        sindent  =indent>0? "| "+sindent+"--":"|--";
        s += sindent+"state:"+state+"\n";
        

        return s;
    }
    
    
}
