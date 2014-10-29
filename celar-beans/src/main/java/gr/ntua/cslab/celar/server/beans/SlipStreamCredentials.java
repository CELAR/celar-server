package gr.ntua.cslab.celar.server.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds the SS username and password.
 * @author Giannis Giannakopoulos
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SlipStreamCredentials {
    
    private String username, password;

    public SlipStreamCredentials() {
    }

    public SlipStreamCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
