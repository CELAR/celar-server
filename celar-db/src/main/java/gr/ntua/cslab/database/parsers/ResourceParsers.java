/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database.parsers;

import gr.ntua.cslab.celar.server.beans.ProvidedResource;
import gr.ntua.cslab.celar.server.beans.ResourceType;
import gr.ntua.cslab.celar.server.beans.Spec;
import gr.ntua.cslab.database.DBException;
import static gr.ntua.cslab.database.EntityGetters.getProvidedResourceByTypeName;
import static gr.ntua.cslab.database.EntityGetters.getResourceTypeByName;
import static gr.ntua.cslab.database.EntityGetters.getSpecsByProvidedResource;
import static gr.ntua.cslab.database.EntityTools.toJSONObject;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class ResourceParsers {
        public static JSONArray exportProvidedResourceSpecs(ProvidedResource pr) throws DBException{
        List<Spec> specs=getSpecsByProvidedResource(pr);
        JSONArray specsJson=new JSONArray();
        for(Spec s:specs){
            specsJson.put(toJSONObject(s));
        }
        return specsJson;
    }
    
    
    public static JSONObject exportProvidedResource(ProvidedResource pr) throws DBException{
        JSONObject prj=toJSONObject(pr);
        prj.put("specs", exportProvidedResourceSpecs(pr));
        return prj;
    }
    

        public static JSONObject exportProvidedResourcesByType(ResourceType rt) throws DBException{
        JSONObject result=new JSONObject();
        JSONArray prs=new JSONArray();
        List<ProvidedResource> resources=getProvidedResourceByTypeName(rt.type);
        for(ProvidedResource pr:resources){
            prs.put(exportProvidedResource(pr));
        }
        result.put("provided_resources", prs);
        return result;
    }
    
    public static JSONObject exportProvidedResourcesByType(String type) throws DBException{
        ResourceType rt = getResourceTypeByName(type);
        return exportProvidedResourcesByType(rt);
    }
}
