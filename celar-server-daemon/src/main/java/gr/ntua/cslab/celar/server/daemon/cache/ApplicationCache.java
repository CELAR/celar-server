package gr.ntua.cslab.celar.server.daemon.cache;


import gr.ntua.cslab.celar.server.beans.structured.ApplicationInfo;
import gr.ntua.cslab.celar.server.daemon.rest.beans.ApplicationInfoList;
import gr.ntua.cslab.celar.server.daemon.rest.beans.ApplicationServerInfo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Giannis Giannakopoulos
 */
public class ApplicationCache {
    
    private static List<ApplicationServerInfo> applications;
    
    
    public static void allocateCache() {
        ApplicationCache.applications = new LinkedList<>();
    }
    /**
     * Insert a new application description into the Application cachel=.
     * @param application 
     */
    public static void insertApplication(ApplicationServerInfo application) {
        ApplicationCache.applications.add(application);
    }
    
    
    /**
     * Search an application based on its id.
     * @param id
     * @return 
     */
    public static ApplicationServerInfo getApplicationById(String id) {
        for(ApplicationServerInfo a : applications) {
            if(a.getId().equals(id))
                return a;
        }
        return null;
    }
    
//    public static ApplicationInfoList getApplications() {
//        return new ApplicationInfoList(applications);
//    }
}
