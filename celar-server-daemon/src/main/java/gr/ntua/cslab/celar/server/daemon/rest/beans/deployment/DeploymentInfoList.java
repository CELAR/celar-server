package gr.ntua.cslab.celar.server.daemon.rest.beans.deployment;

import gr.ntua.cslab.celar.server.beans.Deployment;
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
    
    private List<Deployment> deployments;

    public DeploymentInfoList() {
    }

    public DeploymentInfoList(List<Deployment> deployments) {
        this.deployments = deployments;
    }

    public List<Deployment> getDeployments() {
        return deployments;
    }

    public void setDeployments(List<Deployment> deployments) {
        this.deployments = deployments;
    }
    
    
}
