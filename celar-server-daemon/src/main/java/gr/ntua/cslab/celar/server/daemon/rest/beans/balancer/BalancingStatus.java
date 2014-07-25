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
public class BalancingStatus {
    
    private boolean running;
    private long elapsedTime;

    public BalancingStatus() {
    }

    public BalancingStatus(boolean running, long elapsedTime) {
        this.running = running;
        this.elapsedTime = elapsedTime;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    
}
