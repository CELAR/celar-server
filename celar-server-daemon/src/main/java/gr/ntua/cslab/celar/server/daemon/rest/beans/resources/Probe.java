package gr.ntua.cslab.celar.server.daemon.rest.beans.resources;

import java.util.LinkedList;
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
public class Probe {
    
    private String name;
    private List<String> metrics;

    public Probe() {
        this.metrics = new LinkedList<>();
    }

    public Probe(String name, List<String> metrics) {
        this.name = name;
        this.metrics = metrics;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }
    
    public void addMetric(String metric) {
        this.metrics.add(metric);
    }
    
}
