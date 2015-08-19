package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.DeploymentState;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Deployment with a Deployment State map and Timestamp
 * @author cmantas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DeploymentInfo extends Deployment{
    public Map stateMap;
    public MyTimestamp timestamp=null;

    public DeploymentInfo(Deployment dep, DeploymentState depState) {
        this.mirror(dep);
        stateMap = depState.deployment_state;
        timestamp=depState.timestamp;
        
        if(stateMap.containsKey("state"))
            state=stateMap.get("state").toString();
    }
    
    
}
