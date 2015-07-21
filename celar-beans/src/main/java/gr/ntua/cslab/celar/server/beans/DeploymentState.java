package gr.ntua.cslab.celar.server.beans;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author cmantas
 */

@XmlRootElement
public class DeploymentState extends ReflectiveEntity{
    
    public String deployment_id;
    public Map deployment_state;
    public MyTimestamp timestamp;

    public DeploymentState() {
        super();
        timestamp=new MyTimestamp(System.currentTimeMillis());
    }

   
    public DeploymentState(Map state, Deployment dep) {
        this.deployment_state = state;
        this.deployment_id=dep.getId();
        timestamp=new MyTimestamp(System.currentTimeMillis());
    }
    
    
    
    
}
