
package gr.ntua.cslab.celar.server.beans;

import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author cmantas
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MyTimestamp extends Timestamp{

    public String skata = "skataaaaa";
    
    public MyTimestamp(){
        super(0);
    }
    
    public MyTimestamp(Long nanos){
        super(nanos);
    }
    
 
    public long getLongTime(){
        return super.getTime();
    }
    
    public void setLongTime(long time){
        super.setTime(time);
    }
}
