/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities.parsers;

import java.util.Map;
import tools.CSARParser;
import tools.Parser;

/**
 *
 * @author cmantas
 */
public class ToscaParser {

    public static void parseCsar(String csarFilepath) throws Exception {

        Parser tc = new CSARParser(csarFilepath);

        //application name and version
        System.out.println("Application: " + tc.getAppName() + " v" + tc.getAppVersion());

        //iterate through modules
        for (String module : tc.getModules()) {
            System.out.println("\t" + module);

            //module dependecies
            System.out.println("\t\tdepends on: " + tc.getModuleDependencies(module));

            //iterate through components
            for (String component : tc.getModuleComponents(module)) {
                System.out.println("\t\t" + component);

                //component dependencies
                System.out.println("\t\t\tdepends on: " + tc.getComponentDependencies(component));

                //component properties
                for (Map.Entry prop : tc.getComponentProperties(component).entrySet()) {
                    System.out.println("\t\t\t" + prop.getKey() + ": " + prop.getValue());
                }
            }

        }

    }

    public static void main(String args[]) throws Exception {
        parseCsar("app_7.csar");
    }

}
