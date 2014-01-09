package gr.ntua.cslab.iaas;
import gr.ntua.cslab.JSONServlet;
import static gr.ntua.cslab.database.entities.JSONTools.exportProvidedResources;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;


/**
 *
 * @author cmantas
 */
public class Resources extends JSONServlet {

    @Override
    public byte getType() {return JSON_TYPE; }

    @Override
    public Iterable<String> requestJSONParameters() {return null;}

    @Override
    public Iterable<String> requestStringParameters() {
        List<String> params=new java.util.LinkedList();
        params.add("type");
        return params;
    }

    @Override
    public void processRequest(Map<String, JSONObject> inputJSONParameters, Map<String, String> inputStringParameters) {
        String type=inputStringParameters.get("type");
        print(exportProvidedResources(type));
    }

    @Override
    public String getServletInfo() {
        return "returns the provided resources of the specified type";
    }


}
