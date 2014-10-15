/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database;

import java.util.Map;

/**
 *
 * @author cmantas
 */
public final class TableTools {
    public static final Map<String,String> tableNames = new java.util.TreeMap();
    
    static{
        tableNames.put("User", "USERS");
        tableNames.put("ComponentDependency", "COMPONENT_DEPENDENCY");
        tableNames.put("MetricValue", "METRIC_VALUES");
        tableNames.put("ModuleDependency", "MODULE_DEPENDENCY");
        tableNames.put("ProvidedResource", "PROVIDED_RESOURCE");
        tableNames.put("ResourceType", "RESOURCE_TYPE");
        tableNames.put("Spec", "SPECS");
        tableNames.put("Metric", "METRICS");
        
    }
    
    public static String getTableName(Class c){
        String className = c.getSimpleName();
        String tableName = tableNames.get(className);        
        if (tableName == null){
            tableName = className;
        }        
        return tableName;
    }
    

}
