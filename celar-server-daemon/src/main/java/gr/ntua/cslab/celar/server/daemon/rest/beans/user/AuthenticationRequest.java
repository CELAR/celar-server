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
    private String authenticationUrl;
    private String token;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String cloud, String authenticationUrl, String token) {
        this.cloud = cloud;
        this.authenticationUrl = authenticationUrl;
        this.token = token;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getAuthenticationUrl() {
        return authenticationUrl;
    }

    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
