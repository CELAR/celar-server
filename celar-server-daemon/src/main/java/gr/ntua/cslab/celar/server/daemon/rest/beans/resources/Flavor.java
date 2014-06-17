package gr.ntua.cslab.celar.server.daemon.rest.beans.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Flavor Java Bean
 * @author Giannis Giannakopoulos
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Flavor {

    private int id;
    private String name;

    public Flavor() {
    }

    public Flavor(int id, String name) {
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
