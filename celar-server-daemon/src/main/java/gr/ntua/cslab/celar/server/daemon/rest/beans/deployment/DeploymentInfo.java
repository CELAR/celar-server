package gr.ntua.cslab.celar.server.daemon.rest.beans.deployment;

import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Giannis Giannakopoulos
 */

@XmlRootElement(name = "deployment")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentInfo {
    
    private long startTime, endTime;
    private String status;
    private ApplicationInfo application;
    private String deploymentID;

    public String getDeploymentID() {
		return deploymentID;
	}

	public void setDeploymentID(String deploymentID) {
		this.deploymentID = deploymentID;
	}

	public DeploymentInfo() {
    }

    public DeploymentInfo(String deploymentID, ApplicationInfo applicationInfo, long startTime, long endTime, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.application = applicationInfo;
        this.deploymentID = deploymentID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ApplicationInfo getApplication() {
        return application;
    }

    public void setApplication(ApplicationInfo application) {
        this.application = application;
    }
    
}
