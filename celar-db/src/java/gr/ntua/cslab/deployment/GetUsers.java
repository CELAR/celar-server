/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.deployment;

import gr.ntua.cslab.JSONServlet;
import gr.ntua.cslab.database.entities.JSONTools;
import java.util.Map;;
import org.json.JSONObject;

/**
 *
 * @author Christos Mantas <cmantas@cslab.ece.ntua.gr>
 */
public class GetUsers extends JSONServlet {

    @Override
    public byte getType() {return JSON_TYPE;}

    @Override
    public Iterable<String> requestJSONParameters() {return new java.util.LinkedList();}

    @Override
    public Iterable<String> requestStringParameters() {return new java.util.LinkedList();}

    @Override
    public void processRequest(Map<String, JSONObject> inputJSONParameters, Map<String, String> inputStringParameters) {
        print(JSONTools.exportAllUsers());
    }

    @Override
    public String getServletInfo() {
        return "Exportl all known users to a json object";
    }


}
