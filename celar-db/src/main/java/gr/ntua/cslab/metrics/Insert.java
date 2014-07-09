package gr.ntua.cslab.metrics;

import gr.ntua.cslab.JSONServlet;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Insert extends JSONServlet {

    @Override
    public byte getType() {return INT_TYPE;}

    @Override
    public Iterable<String> requestJSONParameters() {
        String params[]={"metric"};
        return Arrays.asList(params);
    }

    @Override
    public Iterable<String> requestStringParameters() {return new java.util.LinkedList();}

    @Override
    public void processRequest(Map<String, JSONObject> inputJSONParameters, Map<String, String> inputStringParameters) {
        
    }

    @Override
    public String getServletInfo() {
       return "Inserts a metric in the database and returns its id";
    }

}
