package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.Component;
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
public class ComponentInfo extends Component implements Structured{
    
    public List<Resource> resources = null;
   /**
    * Default constructor
    */
    public ComponentInfo(){}
    /**
     * Copy from father constructor
     * @param component
     * @throws CloneNotSupportedException 
     */
    public ComponentInfo(Component component) throws CloneNotSupportedException {
       super(component);
    }

    public String toStucturedString() {
       return StrucuredPrinter.print(this, 0);
    }
    
   public String toStructuredString(int indent) {
       return StrucuredPrinter.print(this, indent);
    }

}
