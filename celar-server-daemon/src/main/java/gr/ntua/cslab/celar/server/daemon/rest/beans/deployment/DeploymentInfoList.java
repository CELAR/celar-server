package gr.ntua.cslab.celar.server.daemon.rest.beans.deployment;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Giannis Giannakopoulos
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentInfoList {
    
    private List<DeploymentInfo> deployments;

    public DeploymentInfoList() {
    }

    public DeploymentInfoList(List<DeploymentInfo> deployments) {
        this.deployments = deployments;
    }

    public List<DeploymentInfo> getDeployments() {
        return deployments;
    }

    public void setDeployments(List<DeploymentInfo> deployments) {
        this.deployments = deployments;
    }
    
    
}
