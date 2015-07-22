package miscTests;
import com.sixsq.slipstream.exceptions.ValidationException;
import static gr.ntua.cslab.celar.slipstreamClient.SSXMLParser.parse;
import gr.ntua.cslab.celar.server.beans.DeploymentState;
import gr.ntua.cslab.celar.slipstreamClient.SlipStreamSSService;
import static gr.ntua.cslab.database.EntityTools.store;

import java.util.Map;


/**
 *
 * @author cmantas
 */
public class ParsingTest {
    
    static SlipStreamSSService ssService;
    
    public static void initClient() throws ValidationException{
            String 
                    username  = "celar",
                    password  = "celar2015",
                    slipstreamHost  = "https://83.212.102.166",
                    connectorName  = "okeanos";
            
             ssService  = new SlipStreamSSService(username, password, slipstreamHost, connectorName);
    }
    
    
    public static void main(String args[]) throws Exception{
     
        initClient();
        String deploymentId="e7054d0e-cfc0-4372-b9d6-076f56f62501";
        Map<String,String> test  = ssService.getAllRuntimeParams(deploymentId);
        System.out.println(test);

        DeploymentState depState = new DeploymentState(test, deploymentId);
        //store(depState); //this will fail with 
                            //Key (deployment_id)=(e7054d0e-cfc0-4372-b9d6-076f56f62501) is not present in table "deployment
    }
}
