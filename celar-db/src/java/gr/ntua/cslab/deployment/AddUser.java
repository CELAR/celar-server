package gr.ntua.cslab.deployment;

import gr.ntua.cslab.database.entities.User;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class AddUser extends gr.ntua.cslab.JSONServlet {

  

    @Override
    public Iterable<String> requestJSONParameters() {
        String[] params ={"user"};
        return Arrays.asList(params);
    }

    @Override
    public Iterable<String> requestStringParameters() {return new java.util.LinkedList();}

    @Override
    public void processRequest(Map<String, JSONObject> inputJSONParameters, Map<String, String> inputStringParameters) {
         JSONObject userj=inputJSONParameters.get("user").getJSONObject("user");
         User user=new User(userj);
         user.store();
         print(user.getId());
    }

    @Override
    public byte getType(){return INT_TYPE;}

    @Override
    public String getServletInfo() {
        return "A Servlet that parses a user from a JSON representation";
    }

}
