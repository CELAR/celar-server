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
        tableNames.put("Resource", "RESOURCES");
        
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
