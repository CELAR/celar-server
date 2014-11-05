package gr.ntua.cslab.database;

import gr.ntua.cslab.celar.server.beans.Application;
import static gr.ntua.cslab.celar.server.beans.Application.makeStringID;
import gr.ntua.cslab.celar.server.beans.Component;
import gr.ntua.cslab.celar.server.beans.ComponentDependency;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.IDEntity;
import gr.ntua.cslab.celar.server.beans.IDEntityFactory;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.Module;
import gr.ntua.cslab.celar.server.beans.ModuleDependency;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import gr.ntua.cslab.celar.server.beans.Resource;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.beans.structured.ComponentInfo;
import gr.ntua.cslab.celar.server.beans.structured.ModuleInfo;
import gr.ntua.cslab.database.DBTools.Constrain;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentResources;
import static gr.ntua.cslab.database.EntityGetters.getMetricValueByMetric;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 * Abstract DataBase Entity
 *
 * @author cmantas
 */
public final class EntityTools {

    /**
     * True if the current state of the entity is not stored in the DB
     */
    private static final Logger LOG = Logger.getLogger(EntityTools.class);

    /**
     * Default constructor is private.
     */
    private EntityTools() {}

    /**
     * the static initializer
     */
    static{
        gr.ntua.cslab.celar.server.beans.IDEntity.setFactory(new DBFactory());
    }

    /**
     * Stores the entity in the appropriate Database Table
     *
     * @param entity
     * @throws DBException in case of an error
     */
    public static void store(ReflectiveEntity entity) throws DBException {
        DBTools.insertData(TableTools.getTableName(entity.getClass()), entity.getFieldMap());
    }
    
    /**
     * Stores the entity in the appropriate Database Table
     *
     * @param app
     * @throws DBException in case of an error
     */
    public static void store(Application app) throws DBException {
        int appId = app.getUnique_Id();
        if(appId==0){
             String prevMax = DBTools.maxValue(TableTools.getTableName(app.getClass()), "unique_id");
             int uniqueId = prevMax!=null? Integer.parseInt(prevMax)+1:1;
             app.setUnique_Id(uniqueId);
             
        }
        DBTools.insertData(TableTools.getTableName(app.getClass()), app.getFieldMap());
    }

    /**
     * Converts the Entity to a JSON representation
     *  <b>Note: </b> will recursively use the overridable "toMap" method
     * 
     * @param e
     * @return a JSON representation of the entity
     */
    public static JSONObject toJSONObject(ReflectiveEntity e) {
        JSONObject jo = new JSONObject();
        Map<String, String> m = e.getFieldMap();
        for (Map.Entry<String, String> entry : m.entrySet()) {
            jo.put(entry.getKey(), entry.getValue());
        }
        return jo;
    }
    
        
    public static <T extends ReflectiveEntity> void fromFieldMap(ReflectiveEntity e, Map<String, String> fieldMap) {
        if (fieldMap==null) return;
        Class current = e.getClass();
//        while (current != ReflectiveEntity.class && current!=null) {
            for (Field f : current.getFields()) {
                try {
                    String name = f.getName();
                    String value = fieldMap.get(name);
                    if (value==null) value = fieldMap.get(name.toLowerCase());
                    Class type = f.getType();
                    //System.out.println("setting "+name+" ("+type.getName()+"): "+value);
                    if (value==null){
                        LOG.debug("Null value for: "+name);
                        continue;
                    }
                    if(type==String.class)
                        f.set(e, value);
                    else if(type==int.class)
                        f.set(e, Integer.parseInt(value));
                    else if(type==MyTimestamp.class)
                        f.set(e, new MyTimestamp(Timestamp.valueOf(value).getTime()));
                    else
                        throw new Exception("Unhandled type: "+type);
                } catch (Exception ex) {
                    LOG.error(ex);
                }
            }
            current = current.getSuperclass();
//        }
    }
    

    /**
     * Retrieves the Entity's fields from a JSON representation
     *  <b>Note: </b> will recursively use the overridable "fromMap" method
     * 
     * @return a JSON representation of the entity
     */
    final static void fromJSON(ReflectiveEntity e, JSONObject jo) {
        Map<String, String> m = new java.util.TreeMap();
        for (Object i : jo.keySet()) {
            String key = (String) i;
            Object value = jo.get(key);
            if (value instanceof String) {
                m.put(key, (String) value);
            }
        }
        fromFieldMap(e, m);
    }


    
    static <T extends ReflectiveEntity> List<T> getByConstrains(Class<? extends ReflectiveEntity> myClass, List<Constrain> lc, boolean or) {
        List<T> results = new java.util.LinkedList();
        try {
            T dummy = (T) myClass.newInstance();
            List<Map<String, String>> mapsFromDB;
            if (lc != null && !lc.isEmpty()) {
                mapsFromDB = DBTools.doSelect(TableTools.getTableName(dummy.getClass()), lc, or);
            } else {
                mapsFromDB = DBTools.doSelect(TableTools.getTableName(dummy.getClass()), "TRUE");
            }

            for (Map m : mapsFromDB) {
                T e = (T) myClass.newInstance();
                fromFieldMap(e, m);
                results.add(e);
            }
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        } 
        return results;
    }
    
    
    static <T extends ReflectiveEntity> List<T> getByField(Class myClass, String field, String value){
        List<Constrain> cl = new LinkedList();
        Constrain c = new Constrain(field, value);
        if(field!=null)  cl.add(c);
        return getByConstrains(myClass, cl, false);
    }

    

    /**
     * Gets all the Entities in the table
     * @param <T> the generic type of Entities that the method returns
     * @param c the class to be returned
     * @return a List of Entities
     */
    public static <T extends ReflectiveEntity> List<T> getAll(Class c) {
        return getByField(c.getClass(), null, null);
    }
    
    

    /**
     * Deletes the entity from the table based on all its fields;
     * @param entity
     * @throws DBException 
     */
    public static void delete(ReflectiveEntity entity) throws DBException {
        DBTools.doDelete(TableTools.getTableName(entity.getClass()), entity.getFieldMap());
    }
    
    /**
     * Deletes the entity from the table based on all its fields;
     * @param entity
     * @throws DBException 
     */
    public static void delete(IDEntity entity) throws DBException {
        if (entity.getId()==0){
            LOG.error("Tried to delete a(n) "+entity.getClass().getSimpleName()+" with an id of 0");
            return;
        }
        DBTools.doDeleteID(TableTools.getTableName(entity.getClass()), entity.getId());
    }

    
    public static void store(IDEntity ie) throws DBException{
        Map m = ie.getFieldMap();
        ie.setId( DBTools.insertIDData(TableTools.getTableName(ie.getClass()), m));
    }
    
    /**
     * Removes all deployments of the current application
     * @param a 
     */
    public static void removeDeployments(Application a){
        throw new UnsupportedOperationException("not implemented");
    }
    
    /**
     * Removes all the used resources and deletes the deployment
     * @param dep The 
     * @throws gr.ntua.cslab.database.DBException 
     */
    public static void removeDeployment(Deployment dep) throws DBException {
        if(dep.id==null){
            LOG.info("Skipping deletion of deployment with id=0");
            return;
        }
        for (Resource r : getDeploymentResources(dep)) {
            //for all metrics
             for (Metric m: EntityTools.<Metric>getByField(Metric.class, "COMPONENT_id", "" + r.component_Id)){
                 for(MetricValue mv: getMetricValueByMetric(m)){
                     delete(mv);
                 }
                 delete(m);
             }
            delete(r);
        }
        delete(dep);
        LOG.info("Removed Deployment( " + dep.id + ")");
    }
    
    /**
     * Remove any aspect of the application
     * @param ai
     */
    public static void removeApplication(ApplicationInfo ai) throws DBException{
        //for all modules
        for(ModuleInfo m: ai.getModules()){
            //for all components
            for(ComponentInfo c: m.getComponents()){
                for(Resource r: c.resources) delete(r);
                delete(c);
            }
            delete(m);
        }
//        //delete component dependencies
//        for(ComponentDependency cd: componentDependencies){
//            delete(cd);
//        }
//        //delete module dependencies
//        for(ModuleDependency md: moduleDependencies){
//            delete(md);
//        }
//        //delete components and modules
//        for (Module m : modules) {
//            for (Component c : moduleComponents.get(m)) {
//                delete(c);
//            }
//           delete(m);
//        }        
//        delete(app);
        
        LOG.info("Removed the Application: "+ai.getDescription());
    }
  

}
