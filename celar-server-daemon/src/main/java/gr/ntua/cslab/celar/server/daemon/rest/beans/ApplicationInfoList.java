
package gr.ntua.cslab.celar.server.daemon.rest.beans;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
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
public class ApplicationInfoList {
    
    private List<ApplicationInfo> applications;

    public ApplicationInfoList() {
        applications = new java.util.LinkedList();
    }

    public ApplicationInfoList(List<ApplicationInfo> applications) {
        this.applications = applications;
    }

    public List<ApplicationInfo> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationInfo> applications) {
        this.applications = applications;
    }
    
}
