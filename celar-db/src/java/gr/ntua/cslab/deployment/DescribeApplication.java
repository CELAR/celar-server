package gr.ntua.cslab.deployment;

import gr.ntua.cslab.JSONServlet;
import gr.ntua.cslab.database.entities.Application;
import static gr.ntua.cslab.database.entities.JSONTools.parseApplicationDescription;
import java.util.Map;
import java.util.Arrays;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class DescribeApplication extends JSONServlet {

   

    @Override
    public byte getType() {return INT_TYPE;}

    @Override
    public Iterable<String> requestJSONParameters() {
        String[] params={"application"};
        return Arrays.asList(params);
    }

    @Override
    public Iterable<String> requestStringParameters() {return new java.util.LinkedList();}

    @Override
    public void processRequest(Map<String, JSONObject> inputJSONParameters, Map<String, String> inputStringParameters) {
                Application app=parseApplicationDescription(inputJSONParameters.get("application"),true);
                //return the app id of the new application
                print(app.getId());
    }

    @Override
    public String getServletInfo() {return "inserts an application in the DB based on its description";}

}
