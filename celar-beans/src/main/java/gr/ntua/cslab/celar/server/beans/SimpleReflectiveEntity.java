package gr.ntua.cslab.celar.server.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * This is the father of all the celar entities.
 * Its functionality is that it can produce a mapping of {field --> value} for
 * all the public fields in a descendant class. It can also use that map to print
 * entity and check for equality
 * @author cmantas
 */
@XmlAccessorType(XmlAccessType.FIELD)

public abstract class SimpleReflectiveEntity {
    
    /**
     * Default constructor
     */
    public SimpleReflectiveEntity(){}
    
    /**
     * The copy constructor
     * @param re Where to copy from
     */
    public SimpleReflectiveEntity(SimpleReflectiveEntity re) {
        mirror(re);
    }
    
   protected void mirror(SimpleReflectiveEntity re){
       //copy all fields 
       Class current = re.getClass();
       while(!current.equals(SimpleReflectiveEntity.class)){
        for (Field f : current.getDeclaredFields()) {
                try {
                    f.set(this, f.get(re));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        current = current.getSuperclass();
       }
   }
    
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
                    rv.put(name, value);
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
    public boolean equals(SimpleReflectiveEntity e) {
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
    
    
    
    public final String toString(boolean structured){
        return toString(0);
    }
    
    
    String toString( int indent) {
        indent = indent>0?--indent:0;
        String sindent = indent>0?"|":"";
        sindent  += (new String(new char[indent]).replace("\0"," |"));
        String  i = sindent+ (indent>0?" ":"");
        String s = i+"/*"+this.getClass().getSimpleName()+"\n";
        
        sindent  =indent>0? "| "+sindent+"--":"|--";
        indent = indent+1;
        List<Field> lists = new java.util.LinkedList();
        try {
            for (Field f : this.getClass().getFields()) {

                String name = f.getName();
                Object value = f.get(this);
                if (f.get(this) == null) {
                    continue;
                }
                if (value instanceof List) {
                    lists.add(f);
                    continue;
                }
                if (value instanceof SimpleReflectiveEntity) s += ((SimpleReflectiveEntity) value).toString(indent+1);
                else s += sindent+name+":"+value.toString()+"\n";

            }
            //print data in lists
            for (Field f : lists) {
                String name = f.getName();
                s+=sindent+ name+":\n";
                if(f.get(this)==null) continue;
                for(Object li: (List)f.get(this)){
                    if (li instanceof SimpleReflectiveEntity) {                        
                      s+=((SimpleReflectiveEntity)li).toString(indent+1);
                  }
                    else{
                        s+=sindent+" |"+li.toString();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }
    
        /**
     * Unmarshals the entity
     * @param <E>
     * @param clazz
     * @param in
     * @return the unmarshalled object
     * @throws JAXBException 
     */
    public static SimpleReflectiveEntity unmarshalGeneric (Class clazz, InputStream in) throws JAXBException{
         JAXBContext jc = JAXBContext.newInstance(clazz);
         Unmarshaller um = jc.createUnmarshaller();        
         SimpleReflectiveEntity rv = (SimpleReflectiveEntity)um.unmarshal(in);
         return rv;
    }
    
    public static SimpleReflectiveEntity unmarshalGeneric (Class clazz, String s) throws JAXBException{
        ByteArrayInputStream bis = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        return unmarshalGeneric(clazz, bis);
    }

}
