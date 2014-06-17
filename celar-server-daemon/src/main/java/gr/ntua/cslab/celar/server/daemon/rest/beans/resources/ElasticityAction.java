package gr.ntua.cslab.celar.server.daemon.rest.beans.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Giannis Giannakopoulos
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ElasticityAction {
    private int id;
    private String name;

    public ElasticityAction() {
    }

    public ElasticityAction(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
