package gr.ntua.cslab.celar.server.daemon.rest.beans.deployment;

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
    
    private int id;
    private long startTime, endTime;
    private DeploymentStatus status;
    private String applicationId;

    public DeploymentInfo() {
    }

    public DeploymentInfo(int id, String applicationId, long startTime, long endTime, DeploymentStatus status) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.applicationId = applicationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public DeploymentStatus getStatus() {
        return status;
    }

    public void setStatus(DeploymentStatus status) {
        this.status = status;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    
}