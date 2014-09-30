/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.DBTools.Constrain;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * Abstract DataBase Entity
 *
 * @author cmantas
 */
public abstract class DBEntity {

    protected boolean modified = true;
    /**
     * True if the current state of the entity is not stored in the DB
     */
    private static Logger LOG = Logger.getLogger(DBEntity.class);

    /**
     * Default constructor.
     */
    public DBEntity() {
        modified = true;
    }

    /**
     * Creates an entity from the fields of the database entry. Calls then
     * fromMap method which is implemented in the subclasses
     *
     * @param fields
     */
    DBEntity(Map<String, String> fields) {
        fromMap(fields);
        modified = false;
    }

    /**
     * Creates an entity from its JSON representation
     * <b>Note: </b> will recursively use the overridable "fromMap" method
     *
     * @param jo
     */
    public DBEntity(JSONObject jo) {
        modified = true;
        fromJSON(jo);
    }

    /**
     * Reads the entity-specific parameters from the Database fields and stores
     * them in the appropriate class variables;
     *
     * @param fields
     */
    abstract protected void fromMap(Map<String, String> fields);

    /**
     * Creates an entity-specific mapping of fields-->values for this DBEntity
     * instance;
     *
     * @return mapping of fields-->values
     */
    abstract protected Map<String, String> toMap();

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    abstract public String getTableName();

    public boolean isModified() {
        return modified;
    }

    /**
     * Stores the entity in the appropriate Database Table
     *
     * @throws gr.ntua.cslab.db_entities.DBException in case of an error
     */
    public void store() throws DBException {
        DBTools.insertData(this.getTableName(), this.toMap());
    }

    /**
     * Converts the Entity to a JSON representation
     *  <b>Note: </b> will recursively use the overridable "toMap" method
     * 
     * @return a JSON representation of this entity
     */
    public JSONObject toJSONObject() {
        JSONObject jo = new JSONObject();
        Map<String, String> m = this.toMap();
        for (Map.Entry<String, String> entry : m.entrySet()) {
            jo.put(entry.getKey(), entry.getValue());
        }
        return jo;
    }

    /**
     * Retrieves this Entity's fields from a JSON representation
     *  <b>Note: </b> will recursively use the overridable "fromMap" method
     * 
     * @return a JSON representation of this entity
     */
    final void fromJSON(JSONObject jo) {
        Map<String, String> m = new java.util.TreeMap();
        for (Object i : jo.keySet()) {
            String key = (String) i;
            Object value = jo.get(key);
            if (value instanceof String) {
                m.put(key, (String) value);
            }
        }
        fromMap(m);
    }

    /**
     * Return a string  representation of style: EntityName(field1:value1, field2:value2 ...)
     * @return 
     */
    @Override
    public String toString() {
        String s = "";
        s += this.getClass().getSimpleName() + "(";
        Map<String, String> m = this.toMap();
        boolean once = true;
        for (Map.Entry<String, String> entry : m.entrySet()) {
            if (once) {
                once = false;
            } else {
                s += ", ";
            }
            s += entry.getKey() + ":" + entry.getValue();
        }
        s += ")";
        return s;
    }

    /**
     * Checks all fields of the two entities to see if they are equal
     *
     * @param e the entity that will be checked against "this" instance
     * @return true if they are equal
     */
    public boolean equals(DBEntity e) {
        JSONObject jo = new JSONObject();
        Map<String, String> me = this.toMap();
        Map<String, String> other = e.toMap();
        for (Map.Entry<String, String> entry : me.entrySet()) {
            String key = entry.getKey();
            String my_value = entry.getValue();
            if (!other.containsKey(key)) {
                return false;
            }
            if (!other.get(key).equals(my_value)) {
                return false;
            }
        }
        return true;
    }

    
    private static <T extends DBEntity> List<T> getByConstrains(Class myClass, List<Constrain> lc, boolean or) {
        List<T> results = new java.util.LinkedList();
        try {
            T dummy = (T) myClass.newInstance();
            List<Map<String, String>> mapsFromDB;
            if (lc != null && !lc.isEmpty()) {
                mapsFromDB = DBTools.doSelect(dummy.getTableName(), lc, or);
            } else {
                mapsFromDB = DBTools.doSelect(dummy.getTableName(), "TRUE");
            }

            for (Map m : mapsFromDB) {
                T e = (T) myClass.newInstance();
                e.fromMap(m);
                e.modified = false;
                results.add(e);
            }
        } catch (DBException ex) {
            System.err.println(ex);
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            System.err.println("Instatiation exception, have you defined a default constructor for " + myClass.getSimpleName() + " ?");
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return results;
    }
    
    
    private static <T extends DBEntity> List<T> getByField(Class myClass, String field, String value){
        List<Constrain> cl = new LinkedList();
        if(field!=null)  cl.add(new Constrain(field, value));
        return getByConstrains(myClass, cl, false);
    }

    
    /***
     * Gets a list of Entities that satisfy the given Constrains (either with OR or AND joining)
     * @param <T> the generic type of Entities that the method returns
     * @param constrainList a list  of constrains
     * @param or whether the constrains will be joined with OR or AND
     * @return 
     */
    public <T extends DBEntity> List<T> getByConstrains(List<Constrain> constrainList, boolean or) {
        return getByConstrains(this.getClass(), constrainList, or);
    }
    
    /**
     * Gets a list of Entities that have the given value under the given fields
     * @param <T> the generic type of Entities that the method returns
     * @param field the column name that will be searched for the given value
     * @param value the desired value
     * @return a List of Entities
     */
    public <T extends DBEntity> List<T> getByField(String field, String value) {
        return getByField(this.getClass(), field, value);
    }

    /**
     * Gets all the Entities in the table
     * @param <T> the generic type of Entities that the method returns
     * @return a List of Entities
     */
    public <T extends DBEntity> List<T> getAll() {
        return getByField(this.getClass(), null, null);
    }

    /**
     * Deletes this entity from the table based on all its fields;
     * @throws DBException 
     */
    public void delete() throws DBException {
        DBTools.doDelete(this.getTableName(), this.toMap());
    }

}
