
package gr.ntua.cslab.celar.server.daemon.rest.beans;


import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmantas
 */
public class ApplicationServerInfo extends ApplicationInfo{

    @XmlTransient    
    private String slipstreamName;
    @XmlTransient
    private String csarFilePath;

    public ApplicationServerInfo(ApplicationInfo ai) {
        super(ai);
    }
    
    public String getSlipstreamName() {
        return slipstreamName;
    }

    public void setSlipstreamName(String slipstreamName) {
        this.slipstreamName = slipstreamName;
    }

    public String getCsarFilePath() {
        return csarFilePath;
    }

    public void setCsarFilePath(String csarFilePath) {
        this.csarFilePath = csarFilePath;
    }
}
