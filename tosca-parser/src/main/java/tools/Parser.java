package tools;

import java.util.List;
import java.util.Map;

/**
 *
 * @author cmantas
 */
public interface Parser {
    
    
    /***
     * Returns the name of the Application
     * @return name (String)
     */
    public String getAppName();
    
    /***
     * Returns the version of the Application
     * @return version (String)
     */
    public String getAppVersion();
    
    
    /***
     * Returns the names of the modules in the Application
     * @return a list of names
     */
    public List<String> getModules();
    
    /***
     * Given the name of a module, it returns the names of the modules it depends on
     * @param module
     * @return a list of names
     */
    public List<String> getModuleDependencies(String module);
    
    /***
     * Given the name of the module, it returns the names of the components this module consists of
     * @param module
     * @return 
     */
    public List<String> getModuleComponents(String module);
    
    /***
     * Given the name of a component it returns the names of the components it depends on
     * @param component
     * @return a list of names
     */
    public List<String> getComponentDependencies(String component);
    
    /***
     * Given the name of a component it returns a mapping of <br/>
     * <b> property--> value </b><br/>
     * with the various properties known of this components 
     * @param component
     * @return a map of properties
     */
    public Map<String, String> getComponentProperties(String component);

    /**
     * Returns the contents of the TOSCA file, packaged in the CSAR archive
     * @return 
     */
    public String getToscaContents();
    
    
    
}
