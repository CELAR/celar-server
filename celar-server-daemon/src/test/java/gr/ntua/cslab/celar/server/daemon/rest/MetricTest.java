
package gr.ntua.cslab.celar.server.daemon.rest;

import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.*;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.beans.structured.REList;
import static gr.ntua.cslab.celar.server.daemon.rest.ApplicationTest.metric;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;



/**
 *
 * @author cmantas
 */
public class MetricTest extends DeploymentTest{
    static Metrics dummyM = new Metrics();
    
    public static void main(String args[]) throws Exception{
        create();
        
        String testFile = "app_info_test";
        
        //write app description to files
        System.out.println(app.id);
        ApplicationInfo ai = dummy.getApplicationInfo(app.id);
        FileOutputStream fos = new FileOutputStream(testFile);
        ai.marshal(fos);
        fos.close();
        
        //showcase
        InputStream fis = new FileInputStream(testFile);
       
        //read the application info from some inputstream
        ApplicationInfo appInfo = new ApplicationInfo();
        appInfo.unmarshal(fis);
        
        //this is the first component of the first module
        Component c = appInfo.modules.get(0).components.get(0);
        
        //a metric about this component (with a timestamp of now)
        Metric ametric = new Metric(component, "my metricsss");
        
        //send this metric to some output stream
//        ametric.marshal(System.out);
        
        fos = new FileOutputStream("metric_test");
        ametric.marshal(fos);
        fos.close();
        fis = new FileInputStream("metric_test");
        
        Metric m = dummyM.put(null, fis);
        System.out.println("Received metric:\n\t "+m);
        
        REList<MetricValue> mvl = Metrics.getValues(metric.id, 0 , System.currentTimeMillis());
        System.out.println("Received metric values:"+" \n\t");
        mvl.marshal(new FileOutputStream("metric_values.xml"));
        
        mvl = new REList();
        mvl.unmarshal(new FileInputStream("metric_values.xml"));
        System.out.println("READ mvl:\n"+mvl);
//        for (MetricValue mv: mvl.values){
//            mv.marshal(System.out);
//        }

        
        //remove the last metric
        System.out.println(dummyM.remove( m.id ));
        
        
        
        destroy();
        
    }
    

}
