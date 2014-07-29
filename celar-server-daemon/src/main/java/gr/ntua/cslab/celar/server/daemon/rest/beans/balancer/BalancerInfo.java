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
public class BalancerInfo {
    
    private double unbalancedRatio;
    private int balancedDatabases;

    public BalancerInfo() {
    }

    public BalancerInfo(double unbalancedRatio, int balancedDatabases) {
        this.unbalancedRatio = unbalancedRatio;
        this.balancedDatabases = balancedDatabases;
    }

    public double getUnbalancedRatio() {
        return unbalancedRatio;
    }

    public void setUnbalancedRatio(double unbalancedRatio) {
        this.unbalancedRatio = unbalancedRatio;
    }

    public int getBalancedDatabases() {
        return balancedDatabases;
    }

    public void setBalancedDatabases(int balancedDatabases) {
        this.balancedDatabases = balancedDatabases;
    }
    
}
