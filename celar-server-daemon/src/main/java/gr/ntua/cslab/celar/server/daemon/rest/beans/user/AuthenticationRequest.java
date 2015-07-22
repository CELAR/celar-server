package gr.ntua.cslab.celar.server.daemon.rest.beans.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Giannis Giannakopoulos
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthenticationRequest {
    
    private String cloud;
    private String projectId;
    private String token;
    private String uuid;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String cloud, String projectId, String token, String uuid) {
        this.cloud = cloud;
        this.projectId = projectId;
        this.token = token;
        this.uuid = uuid;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String authenticationUrl) {
        this.projectId = authenticationUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
}
