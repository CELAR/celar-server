package gr.ntua.cslab.celar.server.daemon.rest.beans.balancer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Giannis Giannakopoulos
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StartBalancer {
    private BalancerType type;
    private double load;
    private double threshold;

    public StartBalancer() {
    }

    public StartBalancer(BalancerType type, double load, double threshold) {
        this.type = type;
        this.load = load;
        this.threshold = threshold;
    }

    public BalancerType getType() {
        return type;
    }

    public void setType(BalancerType type) {
        this.type = type;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}


enum BalancerType { PLACEMENT, ACCESS};
