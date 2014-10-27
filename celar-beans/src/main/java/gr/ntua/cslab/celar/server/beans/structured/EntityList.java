
package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmantas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class  EntityList <E extends ReflectiveEntity> extends ReflectiveEntity{
    
    public LinkedList contents=new LinkedList();
}
