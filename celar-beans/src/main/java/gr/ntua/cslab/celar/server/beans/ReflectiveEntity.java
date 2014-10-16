package gr.ntua.cslab.celar.server.beans;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * This is the father of all the celar entities.
 * Its functionality is that it can produce a mapping of {field --> value} for
 * all the public fields in a descendant class. It can also use that map to print
 * entity and check for equality
 * @author cmantas
 */
public abstract class ReflectiveEntity {
    
    /**
     * Default constructor
     */
    public ReflectiveEntity(){}
    
    /**
     * The copy constructor
     * @param re Where to copy from
     */
    public ReflectiveEntity(ReflectiveEntity re) {
        mirror(re);
    }
    
   protected void mirror(ReflectiveEntity re){
       //copy all fields 
       Class current = re.getClass();
       while(!current.equals(ReflectiveEntity.class)){
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
    
    /**
     * Marshals the entity
     * @param out
     * @throws JAXBException 
     */
    public void marshal(OutputStream out) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(this.getClass());
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this, out);
    }
    
    /**
     * Unmarshals the entity
     * @param in
     * @throws JAXBException 
     */
    public void unmarshal(InputStream in) throws JAXBException{
         JAXBContext jc = JAXBContext.newInstance(this.getClass());
         Unmarshaller um = jc.createUnmarshaller();        
         ReflectiveEntity e = (ReflectiveEntity)um.unmarshal(in);
         this.mirror(e);
    }
    
    

}
