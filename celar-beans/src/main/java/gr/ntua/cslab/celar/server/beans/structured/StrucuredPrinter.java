

package gr.ntua.cslab.celar.server.beans.structured;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author cmantas
 */
public final class StrucuredPrinter {
    
    public StrucuredPrinter() {
    }

    
    public static final String print(Structured re, int indent) {
        indent = indent>0?--indent:0;
        String sindent = indent>0?"|":"";
        sindent  += (new String(new char[indent]).replace("\0"," |"));
        String  i = sindent+ (indent>0?" ":"");
        String s = i+re.getClass().getSimpleName()+"\n";
        
        sindent  =indent>0? "| "+sindent+"--":"|--";
        indent = indent+1;
        List<Field> lists = new java.util.LinkedList();
        try {
            for (Field f : re.getClass().getFields()) {

                String name = f.getName();
                Object value = f.get(re);
                if (f.get(re) == null) {
                    continue;
                }
                if (f.get(re) instanceof List) {
                    lists.add(f);
                    continue;
                }
                s += sindent+name+":"+value.toString()+"\n";

            }

            for (Field f : lists) {
                String name = f.getName();
                s+=sindent+ name+":\n";
                if(f.get(re)==null) continue;
                for(Object li: (List)f.get(re)){
                    if (li instanceof Structured) {                        
                      s+=((Structured)li).toStructuredString(indent+1);
                  }
                    else{
                        s+=sindent+name+":"+li.toString();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }

}
