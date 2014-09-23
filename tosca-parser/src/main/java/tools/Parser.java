/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tools;

import java.util.List;
import java.util.Map;

/**
 *
 * @author cmantas
 */
public interface Parser {
    
    
    public List<String> getModules();
    
    public List<String> getModuleComponents(String module);
    
    public Map<String, String> getComponentProperties(String component);
    
    public List<String> getModuleDependencies(String module);
    
    public List<String> getComponentDependencies(String component);
    
}
