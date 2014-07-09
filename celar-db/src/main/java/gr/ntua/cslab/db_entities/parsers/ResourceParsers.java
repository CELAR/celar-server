/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities.parsers;

import gr.ntua.cslab.db_entities.DBException;
import gr.ntua.cslab.db_entities.ProvidedResource;
import gr.ntua.cslab.db_entities.ResourceType;
import gr.ntua.cslab.db_entities.Spec;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class ResourceParsers {
        public static JSONArray exportProvidedResourceSpecs(ProvidedResource pr) throws DBException{
        List<Spec> specs=Spec.getByProvidedResource(pr);
        JSONArray specsJson=new JSONArray();
        for(Spec s:specs){
            specsJson.put(s.toJSONObject());
        }
        return specsJson;
    }
    
    
    public static JSONObject exportProvidedResource(ProvidedResource pr) throws DBException{
        JSONObject prj=pr.toJSONObject();
        prj.put("specs", exportProvidedResourceSpecs(pr));
        return prj;
    }
    

        public static JSONObject exportProvidedResourcesByType(ResourceType rt) throws DBException{
        JSONObject result=new JSONObject();
        JSONArray prs=new JSONArray();
        List<ProvidedResource> resources=ProvidedResource.getByType(rt);
        for(ProvidedResource pr:resources){
            prs.put(exportProvidedResource(pr));
        }
        result.put("provided_resources", prs);
        return result;
    }
    
    public static JSONObject exportProvidedResourcesByType(String type) throws DBException{
        ResourceType rt = ResourceType.getByName(type);
        return exportProvidedResourcesByType(rt);
    }
}
