package gr.ntua.cslab.deployment;

import gr.ntua.cslab.JSONServlet;
import gr.ntua.cslab.db_entities.DBException;
import gr.ntua.cslab.db_entities.User;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class AddUser extends JSONServlet {

    @Override
    public Iterable<String> requestJSONParameters() {
        String[] params ={"user"};
        return Arrays.asList(params);
    }

    @Override
    public Iterable<String> requestStringParameters() {return new java.util.LinkedList();}

    @Override
    public void processRequest(Map<String, JSONObject> inputJSONParameters,
            Map<String, String> inputStringParameters) throws DBException{
        
         logger.info("adding a user");
         JSONObject userj=inputJSONParameters.get("user").getJSONObject("user");
         User user=new User(userj);
         user.store();
         print(user.toJSONObject());
         //print("dummy result");
    }

    @Override
    public byte getType(){return INT_TYPE;}

    @Override
    public String getServletInfo() {
        return "A Servlet that parses a user from a JSON representation";
    }

}
