/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.celar.slipstreamClient;
import static gr.ntua.cslab.celar.slipstreamClient.SSXMLParser.parse;

import java.util.Map;


/**
 *
 * @author cmantas
 */
public class ParsingTest {
    
    
    
    public static void main(String args[]) throws Exception{
        
    String username , password, slipstreamHost, connectorName, deploymentId;

    username  = "celar";
    password  = "celar2015";
    slipstreamHost  = "https://83.212.102.166";
    connectorName  = "okeanos";
    deploymentId="e7054d0e-cfc0-4372-b9d6-076f56f62501";

    SlipStreamSSService ssService  = new SlipStreamSSService(username, password, slipstreamHost, connectorName);
    
    Map<String,String> test  = ssService.getAllRuntimeParams(deploymentId);
    
    System.out.println(test);
    
        //System.out.println(ssService.getDeploymentState(deploymentId));
    
    
    }
}
