package gr.ntua.cslab.celar.server.beans;

import gr.ntua.cslab.celar.server.beans.structured.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This is the father of all the celar entities.
 * Its functionality is that it can produce a mapping of {field --> value} for
 * all the public fields in a descendant class. It can also use that map to print
 * entity and check for equality
 * @author cmantas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Application.class, Component.class, ComponentDependency.class,
    Decision.class, Deployment.class, Metric.class, MetricValue.class, Module.class,
    ModuleDependency.class, MyTimestamp.class, Probe.class, ProvidedResource.class,
    ResizingAction.class, Resource.class, ResourceType.class, SlipStreamCredentials.class,
    Spec.class, User.class, DeploymentState.class, ProvidedResourceInfo.class})

public abstract class ReflectiveEntity extends SimpleReflectiveEntity {
    
    /**
     * Default constructor
     */
    public ReflectiveEntity(){}
    
    /**
     * The copy constructor
     * @param re Where to copy from
     */
    public ReflectiveEntity(ReflectiveEntity re) {
        super(re);
    }
    
    
    /**
     * Marshals the entity
     * @param out
     * @throws JAXBException 
     */
    public void marshal(OutputStream out) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(this.getClass());
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this, out);
    }
    
    /**
     * Unmarshals the entity
     * @param in
     * @throws JAXBException 
     */
    public void unmarshal(InputStream in) throws JAXBException{
         JAXBContext jc = JAXBContext.newInstance(this.getClass());
         Unmarshaller um = jc.createUnmarshaller();        
         ReflectiveEntity e = (ReflectiveEntity)um.unmarshal(in);
         this.mirror(e);
    }    

}
