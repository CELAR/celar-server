package gr.ntua.cslab.database;

import gr.ntua.cslab.celar.server.beans.Application;
import gr.ntua.cslab.celar.server.beans.Deployment;
import gr.ntua.cslab.celar.server.beans.IDEntity;
import gr.ntua.cslab.celar.server.beans.Metric;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import gr.ntua.cslab.celar.server.beans.Resource;
import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.beans.structured.ComponentInfo;
import gr.ntua.cslab.celar.server.beans.structured.ModuleInfo;
import gr.ntua.cslab.database.DBTools.Constrain;
import static gr.ntua.cslab.database.DBTools.doSelect;
import static gr.ntua.cslab.database.DBTools.whereStatement;
import static gr.ntua.cslab.database.EntityGetters.getDeploymentResources;
import static gr.ntua.cslab.database.EntityGetters.getMetricValueByMetric;
import static gr.ntua.cslab.database.TableTools.getTableName;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public static List<String> fieldList(Class c) {
        List<String> rv = new LinkedList();
        Class current = c;
        for (Field f : current.getFields()) {
            rv.add(f.getName());
            //current = current.getSuperclass();
        }
        return rv;
    }
    
   
        
    public static <T extends ReflectiveEntity> void fromFieldMap(ReflectiveEntity e, Map<String, String> fieldMap) {
        if (fieldMap==null) return;
        Class current = e.getClass();
//        while (current != ReflectiveEntity.class && current!=null) {
            for (Field f : current.getFields()) {
                try {
                    String fieldName = f.getName();
                    String value = fieldMap.get(fieldName);
                    if (value==null) value = fieldMap.get(fieldName.toLowerCase());
                    Class type = f.getType();
                    //System.out.println("setting "+name+" ("+type.getName()+"): "+value);
                    if (value==null){
                        LOG.debug("Null value for: "+fieldName);
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
            mapsFromDB = DBTools.doSelectAll(TableTools.getTableName(dummy.getClass()), lc, or);
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
            for(ComponentInfo c: m.components){
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
    
    
    static String fatherEntityIdName (Class father, Class child){
        String fatherName = getTableName(father);
        for (Field f: child.getFields()){
            String fieldName = f.getName();
            if(fieldName.toLowerCase().contains(fatherName.toLowerCase()) && fieldName.toLowerCase().contains("id"))
                return fieldName;
        }
        return null;
    }
    
    
    static String aliasFields(List<Class> classes){
        String rv = "";
        for(Class c: classes){
            List<String> fields = fieldList(c);
            for(String f: fields){
                String tablename = TableTools.getTableName(c);
                rv += tablename+"."+f+ " AS "+tablename+"__"+f+" ,";
            }
        }
         rv = rv.substring(0, rv.length()-2);
        return rv;
    }
    
    
    static Map<String, Map<String,String>> deAliasFields(List<Class> classes, Map<String, String> fromDb){
        Map<String, Map<String,String>> rv = new TreeMap();
        for(Class c: classes){
            String tablename = TableTools.getTableName(c);
            Map map = new TreeMap();
            rv.put(c.getSimpleName(), map);
            //find fiedls
            List<String> fields = new LinkedList();
            for(String s: fromDb.keySet()){
                if (s.toLowerCase().startsWith(tablename.toLowerCase())){
                    fields.add(s);
                }
            }
            for (String s: fields){
                    String t =  fromDb.remove(s);
                    int i = c.getSimpleName().length()+3;
                    s = s.substring(i, s.length());
                    map.put(s,t);
            }
            
        }
        return rv;
    }
    
    
    static String joinedTableName(List<Class> classes){
        if (classes.isEmpty()) return null;
        String joinName ="",tableName="", prevName;
        Class prevClass, thisClass;
        thisClass = classes.get(0);
        tableName = TableTools.getTableName(thisClass);
        joinName += tableName;
        for (int i = 1; i < classes.size(); i++) {
            prevName = tableName;
            prevClass = thisClass;
            thisClass = classes.get(i);
            tableName = TableTools.getTableName(thisClass);
            joinName += " INNER JOIN " +tableName;  
            joinName += " ON ("+
                    prevName+".id="+
                    tableName+"."+fatherEntityIdName(prevClass,thisClass)+
                    ")";
        }
        return joinName;
        
    }
    
    static List<ReflectiveEntity> disJoin(Map<String,String> joinedFields,  List<Class > classes) throws Exception{
       List<ReflectiveEntity> rv = new LinkedList();
       Map<String, Map<String,String>> deAliased = deAliasFields(classes, joinedFields);
       for(Class c: classes){
           ReflectiveEntity re =(ReflectiveEntity) c.newInstance();
           fromFieldMap(re, deAliased.get(c.getSimpleName()));
           rv.add(re);
       }
       return rv;
        
    }
    
    static List<List<ReflectiveEntity>> joiner(List<Class> classList, List<Constrain> constrains) throws DBException, Exception{
 
        List<List<ReflectiveEntity>> rv = new LinkedList();
        List<Map<String, String>> mapsFromDB;
        
        String joinedTableName = joinedTableName(classList);
        String aliasedFields = aliasFields(classList);
        String where = whereStatement(constrains, false);
        mapsFromDB = doSelect(aliasedFields, joinedTableName, where);
        for(Map<String, String> m: mapsFromDB){
            rv.add(disJoin(m, classList));
        }
        return rv;
    }
  

}
