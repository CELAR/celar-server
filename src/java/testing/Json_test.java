/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pro
 */
public class Json_test {

   public static void main(String[] args) 
   {
      JSONObject obj = new JSONObject();
      JSONObject obj2 = new JSONObject();
      
      obj2.put("foo","foo_value" );
      
      obj.put("inner", obj2);
      obj.put("name", "foo");
      obj.put("num", new Integer(100));
      obj.put("balance", 1.45);
      obj.put("is_vip", true);
      String json_string=obj.toJSONString();
      
       System.out.println(json_string);
      
      JSONParser parser=new JSONParser();
       JSONObject outer,inner=null;
       try {
          outer= (JSONObject) parser.parse(json_string);
          inner= (JSONObject) parser.parse(outer.get("inner").toString());
       } catch (ParseException ex) {
           System.err.println("error parsing json");
       }
       
       System.out.println("inner foo value="+inner.get("foo"));
       
   }
}
    
    

