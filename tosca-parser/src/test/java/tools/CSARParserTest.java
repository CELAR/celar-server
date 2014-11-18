package tools;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.util.Map.Entry;

/**
 *
 * @author cmantas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CSARParserTest {
    
    public CSARParserTest() {
    }

    

    public static void main(String args[]){
        
        try {
            //create a Parser instance
  Parser tc  = new CSARParser("DataPlaySubmission.csary");
            

            
            //application name and version
            System.out.println("Application: "+tc.getAppName()+" v"+tc.getAppVersion());
            
            //iterate through modules
            for(String module: tc.getModules()){
                System.out.println("\t"+module);
                
                //module dependecies
                System.out.println("\t\tdepends on: "+tc.getModuleDependencies(module));
                
                //iterate through components
                for(String component: tc.getModuleComponents(module)){
                    System.out.println("\t\t"+component);
                    
                    //component dependencies
                    System.out.println("\t\t\tdepends on: "+tc.getComponentDependencies(component));
                    
                    //component properties
                    for(Entry prop: tc.getComponentProperties(component).entrySet()){
                        if(!prop.getKey().toString().toLowerCase().contains("script"))
                            System.out.println("\t\t\t"+prop.getKey()+": "+prop.getValue());
                        else
                            System.out.println("\t\t\t"+prop.getKey()+": "+"[ some string script ");
                    }
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("failed the showcase");
        }        
    }
    

    
}
