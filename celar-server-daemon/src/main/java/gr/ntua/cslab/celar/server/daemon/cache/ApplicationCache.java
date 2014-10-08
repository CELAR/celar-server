package gr.ntua.cslab.celar.server.daemon.cache;

import gr.ntua.cslab.celar.server.daemon.rest.beans.application.ApplicationInfo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Giannis Giannakopoulos
 */
public class ApplicationCache {
    
    private static List<ApplicationInfo> applications;
    
    
    public static void allocateCache() {
        ApplicationCache.applications = new LinkedList<>();
    }
    /**
     * Insert a new application description into the Application cachel=.
     * @param application 
     */
    public static void insertApplication(ApplicationInfo application) {
        ApplicationCache.applications.add(application);
    }
    
    
    /**
     * Search an application based on its id.
     * @param id
     * @return 
     */
    public static ApplicationInfo getApplicationById(String id) {
        for(ApplicationInfo a : applications) {
            if(a.getId().equals(id))
                return a;
        }
        return null;
    }
    
}