package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.IDEntity;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmantas
 * @param <E>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class  REList <E extends IDEntity> extends ReflectiveEntity  {
     public List<E> values;

    public REList() {
        values = new java.util.LinkedList();
    }

    public REList(List<E> values) {
        this.values = values;
    }

    @XmlAnyElement(lax=true)
    public List<E> getValues() {
        return values;
    }

    public void setValues(List<E> values) {
        this.values = values;
    }
}
