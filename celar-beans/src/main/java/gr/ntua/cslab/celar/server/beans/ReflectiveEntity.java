package gr.ntua.cslab.celar.server.beans;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This is the father of all the celar entities.
 * Its functionality is that it can produce a mapping of {field --> value} for
 * all the public fields in a descendant class. It can also use that map to print
 * entity and check for equality
 * @author cmantas
 */
public abstract class ReflectiveEntity {
    
    /**
     * Produces a mapping of {field --> value} for all (non-null) public fields of the class
     * @return a map of String-->String
     */
   public Map<String, String> getFieldMap() {
        Map<String, String> rv = new java.util.TreeMap();
            for (Field f : this.getClass().getFields()) {
                try {
                    String name = f.getName();
                    if(f.get(this)==null) continue;
                    String value = f.get(this).toString();
                    rv.put(name, value.toLowerCase());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        return rv;
    }
    

    /**
     * Return a string  representation of style: EntityName(field1:value1, field2:value2 ...)
     * @return 
     */
    @Override
    public String toString() {
        String s = "";
        s += this.getClass().getSimpleName() + "(";
        Map<String, String> m = this.getFieldMap();
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
    public boolean equals(ReflectiveEntity e) {
        Map<String, String> me = this.getFieldMap();
        Map<String, String> other = e.getFieldMap();
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

}
