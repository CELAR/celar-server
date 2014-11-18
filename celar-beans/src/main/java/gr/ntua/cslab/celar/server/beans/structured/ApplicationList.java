
package gr.ntua.cslab.celar.server.beans.structured;
import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
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
public class ApplicationList extends ReflectiveEntity {
    
    public List<Application> applications;

    public ApplicationList() {
        applications = new java.util.LinkedList();
    }

    public ApplicationList(List<Application> applications) {
        this.applications = applications;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
    
}
