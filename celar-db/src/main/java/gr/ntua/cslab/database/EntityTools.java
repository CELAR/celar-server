package gr.ntua.cslab.database;

import gr.ntua.cslab.celar.server.beans.Application;
import static gr.ntua.cslab.celar.server.beans.Application.makeStringID;
import gr.ntua.cslab.celar.server.beans.IDEntity;
import gr.ntua.cslab.celar.server.beans.IDEntityFactory;
import gr.ntua.cslab.celar.server.beans.MyTimestamp;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import gr.ntua.cslab.database.DBTools.Constrain;
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
        Class current = e.getClass();
//        while (current != ReflectiveEntity.class && current!=null) {
            for (Field f : current.getFields()) {
                try {
                    String name = f.getName();
                    String value = fieldMap.get(name.toLowerCase());
                    Class type = f.getType();
                    //System.out.println("setting "+name+" ("+type.getName()+"): "+value);
                    if (value==null) continue;
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

    
    public static void store(IDEntity ie) throws DBException{
        Map m = ie.getFieldMap();
        ie.setId( DBTools.insertIDData(TableTools.getTableName(ie.getClass()), m));
    }
  

}
