/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.deployment;

import gr.ntua.cslab.JSONServlet;
import gr.ntua.cslab.database.entities.Application;
import javax.servlet.annotation.WebServlet;
import static gr.ntua.cslab.database.entities.JSONTools.exportApplicationConfiguration;
import static gr.ntua.cslab.database.entities.JSONTools.findDeploymentApp;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
@WebServlet(name = "GetConfiguration", urlPatterns = {"/deployment/getConfiguration"})
public class GetConfiguration extends JSONServlet {

    @Override
    public byte getType() {return JSON_TYPE;}

    @Override
    public Iterable<String> requestJSONParameters() {
        return new java.util.LinkedList();
    }

    @Override
    public Iterable<String> requestStringParameters() {
        String[] params ={"DeploymentId","timestamp" };
        return Arrays.asList(params);
    }

    @Override
    public void processRequest(Map<String, JSONObject> inputJSONParameters, Map<String, String> inputStringParameters) {
        String deploymentId = inputStringParameters.get("DeploymentId");
        String timestamp = inputStringParameters.get("timestamp");

        //sanity check input parameters
        int depId = Integer.parseInt(deploymentId);
        timestamp = timestamp == null ? "now" : timestamp;
        Timestamp ts = timestamp.equals("now")
                ? new Timestamp(System.currentTimeMillis())
                : Timestamp.valueOf(timestamp);
        Application parent = findDeploymentApp(depId);
        print( exportApplicationConfiguration(parent, ts) );

    }

    @Override
    public String getServletInfo() {
        String info=" gets the configuration of an application for the specified deployment id, timestamp";
        return info;
    }
    

}
