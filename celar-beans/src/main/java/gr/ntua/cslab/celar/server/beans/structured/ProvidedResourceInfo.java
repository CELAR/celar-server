package gr.ntua.cslab.celar.server.beans.structured;
import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.Spec;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Container class for Provided Resources and Their Specs
 * @author cmantas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ProvidedResourceInfo extends ProvidedResource{
    public List<Spec> specs;

    public ProvidedResourceInfo() {
    }

    
    public ProvidedResourceInfo(ProvidedResource pr) throws Exception {
        super(pr);
        specs = new java.util.LinkedList();
    }

    public List<Spec> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Spec> specs) {
        this.specs = specs;
    }
    
    
    
    
}
