package gr.ntua.cslab.celar.server.daemon.rest.beans.deployment;

import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sixsq.slipstream.statemachine.States;

/**
 *
 * @author Giannis Giannakopoulos
 */
@XmlRootElement(name = "deployment")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeploymentInfo {

    private long startTime, endTime;
    private States state;
    private ApplicationInfo application;
    private String deploymentID;
    private String description;
    
    public DeploymentInfo() {
    }

    public DeploymentInfo(String deploymentID, ApplicationInfo applicationInfo, long startTime, long endTime, States state, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.application = applicationInfo;
        this.deploymentID = deploymentID;
        this.description = description;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public ApplicationInfo getApplication() {
        return application;
    }

    public void setApplication(ApplicationInfo application) {
        this.application = application;
    }
    
    public String getDeploymentID() {
        return deploymentID;
    }

    public void setDeploymentID(String deploymentID) {
        this.deploymentID = deploymentID;
    }
}
