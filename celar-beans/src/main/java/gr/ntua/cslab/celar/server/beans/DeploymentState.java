package gr.ntua.cslab.celar.server.beans;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author cmantas
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentState extends ReflectiveEntity{
    
    public String deployment_id;
    public Map deployment_state;
    public long timestamp;

    public DeploymentState() {
        super();
    }

   
    public DeploymentState(Map state, Deployment dep) {
        this.deployment_state = state;
        this.deployment_id=dep.getId();
        timestamp=System.currentTimeMillis();
    }
    
    public DeploymentState(Map state, String depId) {
        this.deployment_state = state;
        this.deployment_id=depId;
        this.timestamp=System.currentTimeMillis();
    }

  
    
}
