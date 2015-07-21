package gr.ntua.cslab.database;

import gr.ntua.cslab.celar.server.beans.*;
import gr.ntua.cslab.celar.server.beans.structured.ProvidedResourceInfo;
import gr.ntua.cslab.database.DBTools.Constrain;
import static gr.ntua.cslab.database.EntityTools.joiner;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author cmantas
 */
public class EntitySearchers {
    
    private static final Logger LOG = Logger.getLogger(EntitySearchers.class.getName());

    public static List<Application> searchApplication(long submittedStart, long submittedEnd, String description, int userid, String moduleName, String componentDescription, String providedResourceId) throws Exception {
        List<DBTools.Constrain> constrains = new LinkedList();
        constrains.add(new DBTools.Constrain("submitted", ">=", "" + new MyTimestamp(submittedStart)));
        if (submittedEnd > 0) {
            constrains.add(new DBTools.Constrain("submitted", "<=", "" + new MyTimestamp(submittedEnd)));
        }
        if (description != null) {
            constrains.add(new DBTools.Constrain("Application.description", description));
        }
        if (userid != 0) {
            constrains.add(new DBTools.Constrain("user_id", "" + userid));
        }
        if (moduleName != null) {
            constrains.add(new DBTools.Constrain("Module.name", moduleName));
        }
        if (componentDescription != null) {
            constrains.add(new DBTools.Constrain("Component.description", componentDescription));
        }
        LOG.info("Searching for constrains: " + constrains);
        List<Class> classes = new LinkedList();
        classes.add(Application.class);
        classes.add(Module.class);
        classes.add(Component.class);
        List<List<ReflectiveEntity>> lines = joiner(classes, constrains);
        List<Application> rv = new LinkedList();
        for (List<ReflectiveEntity> line : lines) {
            rv.add((Application) line.get(0));
        }
        return rv;
    }

    public static List<Decision> searchDecisions(Deployment depl, long start, long end, String actionType, int componentId, int moduleId) throws Exception {
        List<Decision> rv = new LinkedList();
        List<DBTools.Constrain> constrains = new LinkedList();
        if (start >= 0) {
            constrains.add(new DBTools.Constrain("timestamp", ">=", "" + new MyTimestamp(start)));
        }
        if (end > 0) {
            constrains.add(new DBTools.Constrain("timestamp", "<=", "" + new MyTimestamp(end)));
        }
        if (!actionType.equals("")) {
            constrains.add(new DBTools.Constrain("lower(RESIZING_ACTION.type)", actionType.toLowerCase()));
        }
        if (componentId > 0) {
            constrains.add(new DBTools.Constrain("COMPONENT.id", "" + componentId));
        }
        if (moduleId > 0) {
            constrains.add(new DBTools.Constrain("MODULE.id", "" + moduleId));
        }
        List<Class> classes = new LinkedList();
        classes.add(Module.class);
        classes.add(Component.class);
        classes.add(ResizingAction.class);
        classes.add(Decision.class);
        List<List<ReflectiveEntity>> lines = joiner(classes, constrains);
        for (List<ReflectiveEntity> line : lines) {
            Decision d = (Decision) line.get(3);
            if (d.deployment_id.equals(depl.id)) {
                rv.add(d);
            }
        }
        return rv;
    }

    public static List<MetricValue> searchMetricValues(Deployment dep, Metric m, Timestamp start, Timestamp finish) throws Exception {
        List<Constrain> lc = new LinkedList();
        if (start != null) {
            lc.add(new DBTools.Constrain("METRIC_VALUES.timestamp", ">= ", "" + start));
        }
        if (finish != null) {
            lc.add(new DBTools.Constrain("METRIC_VALUES.timestamp", "<=", "" + finish));
        }
        if (dep != null) {
            lc.add(new DBTools.Constrain("RESOURCES.DEPLOYMENT_id", dep.id));
        }
        List<Class> classes = new LinkedList();
        classes.add(Resource.class);
        classes.add(MetricValue.class);
        List<MetricValue> rv = new LinkedList();
        List<List<ReflectiveEntity>> lines = joiner(classes, lc);
        for (List<ReflectiveEntity> line : lines) {
            rv.add((MetricValue) line.get(1));
        }
        return rv;
    }
    
        static List<List<ReflectiveEntity>> searchResourceSpecsTANGLED(String resourceTypeName )throws Exception {
            List<List<ReflectiveEntity>> rv = new LinkedList();
            List<DBTools.Constrain> constrains = new LinkedList();
            constrains.add(new DBTools.Constrain("RESOURCE_TYPE.type", "" + resourceTypeName));
            
                    
            List<Class> classes = new LinkedList();
            classes.add(ResourceType.class);
            classes.add(ProvidedResource.class);
            classes.add(Spec.class);
            List<List<ReflectiveEntity>> lines = joiner(classes, constrains);
            for (List<ReflectiveEntity> line : lines) rv.add(line);
            return rv;
    }   
    
        public static List<ProvidedResourceInfo> searchProvidedResourceSpecs(String resourceTypeName ) throws Exception{
            List<ProvidedResourceInfo> rv = new LinkedList();
            
            //get the tangled rows (ResourceType, ProvidedResource, Spec)
            List<List<ReflectiveEntity>> tangled = searchResourceSpecsTANGLED(resourceTypeName);
            
            ProvidedResource current = null, check;
            ProvidedResourceInfo currentInfo = null;
            
            for(List<ReflectiveEntity> entities: tangled){
                //the provided resource to check against current
                check = (ProvidedResource) entities.get(1);
                
                //check if there is a new resource
                if(current==null || !current.equals(check)){
                    current = check; 
                    currentInfo=new  ProvidedResourceInfo(current);
                    if(current!=null) rv.add(currentInfo);
                }
                
                currentInfo.specs.add((Spec) entities.get(2) );
                
            }
            
            
            return rv;
        }
}
