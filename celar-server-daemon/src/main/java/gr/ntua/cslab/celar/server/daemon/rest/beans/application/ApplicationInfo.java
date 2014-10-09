/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.celar.server.daemon.rest.beans.application;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Giannis Giannakopoulos
 */

@XmlRootElement(name = "application")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInfo {
    private String id;
    private long submitted;
    private String description;
    private String version;
    
    @XmlTransient    
    private String slipstreamName;
    @XmlTransient
    private String csarFilePath;
    
    
    public ApplicationInfo() {
    }

    public ApplicationInfo(String id, long submitted, String description, String version) {
        this.id = id;
        this.submitted = submitted;
        this.description = description;
        this.version = version;
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSubmitted() {
        return submitted;
    }

    public void setSubmitted(long submitted) {
        this.submitted = submitted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
