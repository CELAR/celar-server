
package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.Resource;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmantas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeployedApplication extends ApplicationInfo {

    public Deployment deployment;
    
    public DeployedApplication(){}
    
    public DeployedApplication(Application app, Deployment deployment){
        super(app);
        this.deployment = deployment;
    }
    /**
     * Gets the resources used by all the components of all modules
     * @return 
     */
    public List<Resource> getAllResources() {
        List<Resource> rv = new java.util.LinkedList<>();
        for (ModuleInfo mi : modules) {
            for (ComponentInfo ci : mi.components) {
                rv.addAll(ci.resources);
            }
        }
        return rv;
    }
    
    public Deployment getDeployment(){
        return deployment;
    }
    
    public void setDeployment(Deployment dep){
        deployment=dep;
    }
}
