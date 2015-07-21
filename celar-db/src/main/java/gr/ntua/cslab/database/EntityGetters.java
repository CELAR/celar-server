package gr.ntua.cslab.database;

import gr.ntua.cslab.celar.server.beans.*;
import gr.ntua.cslab.database.DBTools.Constrain;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author cmantas
 */

public class EntityGetters {
    private static final Logger LOG = Logger.getLogger(EntityGetters.class.getName());
    private static final DBFactory factory = new DBFactory();
    
    
    
    
        /**
     * Returns a list of Components that have the given Module as their father
     * @param module the father of the given Components
     * @return
     * @throws DBException 
     */
    public static List<Component> getModuleComponents(Module module) throws DBException {
        return EntityTools.<Component>getByField(Component.class,"MODULE_id", "" + module.getId());
    }
    
    
    /**
     * Returns a list of all the metrics associated with the given component 
     * @param c
     * @return 
     */    
    public static List<Metric> getComponentMetrics(Component c){
        return EntityTools.<Metric>getByField(Metric.class, "COMPONENT_id", "" + c.getId());
    }
    
    public static List<MetricValue> getMetricValueByMetric(Metric m){
        return EntityTools.<MetricValue>getByField(MetricValue.class, "METRICS_id", ""+m.getId());
    }
    
    
    /**
     * Returns a list of all the metric values associated with the given Metric between the given timestamps
     * @param m
     * @param start
     * @param end
     * @return 
     */
    public static List<MetricValue> getMetricValueByMetric(Metric m, Timestamp start, Timestamp end){
        LOG.error("Not implemented timestamp constrains for metric values");
        return EntityTools.<MetricValue>getByField(MetricValue.class, "METRICS_id", ""+m.getId());
    }
    
    
    /**
     * Returns a List of all the ProvidedResources of the given type
     * 
     * @param typeName the name of the ResourceType father of all the ProvidedResources returned
     * @return 
     * @throws gr.ntua.cslab.database.DBException 
    */
    public static List<ProvidedResource> getProvidedResourceByTypeName(String typeName) throws DBException{
        ResourceType rt = getResourceTypeByName(typeName);
        return EntityTools.getByField(
                        ProvidedResource.class, "RESOURCE_TYPE_id", ""+rt.getId());
    }
    
        /**
     * Returns a list of all the modules whose father is the given Application
     * @param app
     * @return a List of Modules
     * @throws DBException in case of an error
     */
    public static List<Module> getModulesByApplication(Application app) throws DBException {
        List<Module> rv = new java.util.LinkedList();

        for(Object e: EntityTools.getByField(Module.class, "APPLICATION_id", ""+app.getId())){
            rv.add((Module) e);
        }
        return rv;
    }
    
        /**
     * gets the user with the specified name (assumed unique)
     * 
     * @param name
     * @return a List of Users
     * @throws gr.ntua.cslab.database.DBException
    */
    public static User getUserByName(String name) throws DBException{
        List<User> list = EntityTools.getByField(User.class, "name", name);
        if(list.isEmpty()){
           // throw new DBException(DBException.NO_SUCH_ENTRY, "No Users found with name: "+name);
            return null;
        }
        return list.get(0);

    }
    
    /**
     * gets all User entities
     * @return a List of Users
     */
    public static List<User> getAllUsers(){
        return EntityTools.<User>getAll(User.class);
    }
    
        /**
     * Returns a list of all the Specs whose father is the given ProvidedResouce
     * @param providedResource
     * @return
     * @throws DBException 
     */
    public static List<Spec> getSpecsByProvidedResource(ProvidedResource providedResource) throws DBException {
        List<Spec> rv = new java.util.LinkedList();
        for(Object e: EntityTools.getByField(Spec.class,"PROVIDED_RESOURCE_id", ""+providedResource.getId())){
            rv.add((Spec) e);
        }
        return rv;
    }
    
    
    private static List intersect(List A, List B) {
        List rtnList = new java.util.LinkedList<>();
        for (Object dto : A) {
            if (B.contains(dto)) {
                rtnList.add(dto);
            }
        }
        return rtnList;
    }

    
    public static List<Integer> getProvidedResourceIDsByProperty(String property, String value){
        List<DBTools.Constrain> lc= new java.util.LinkedList();
        Constrain prop = new DBTools.Constrain("property", property);
        lc.add(prop);
        Constrain val = new DBTools.Constrain("value", value);
        lc.add(val);
        List<Integer> rv = new java.util.LinkedList();
        for(Object e: EntityTools.getByConstrains(Spec.class, lc, false)){
            rv.add(((Spec) e).provided_Resource_Id);
        }
        return rv;
    }
    
    static List<Integer> getProvidedResourceIDsByFlavor(int cores, int ram, int disk){
       List<Integer> byCores =  getProvidedResourceIDsByProperty("cores", ""+cores);
       List<Integer> byRam =  getProvidedResourceIDsByProperty("ram", ""+ram);
       List<Integer> byDisk =  getProvidedResourceIDsByProperty("disk", ""+disk);
       List<Integer> rv = intersect(byCores, byRam);
       rv = intersect(rv, byDisk);
       return rv;
    }
    
    /**
     * Returns a List of all the provided (flavor) resources with the given that flavor parameters
     * @param cores
     * @param ram
     * @param disk
     * @return
     * @throws DBException 
     */
    public static List<ProvidedResource> getProvidedResourceByFlavorInfo(int cores, int ram, int disk) throws Exception{
        List<ProvidedResource> rv = new java.util.LinkedList();
        for (Integer id: getProvidedResourceIDsByFlavor(cores, ram, disk)){
            rv.add(new ProvidedResource(id));
        }
        return rv;
    }
    
            /**
     * Gets the Resources
     * 
     * @param c the Comp
     * @return 
     * @throws gr.ntua.cslab.database.DBException 
    */
    public static List<Resource> getResourcesByComponent(Component c) throws DBException{
        List<Resource> list = EntityTools.getByField(Resource.class, "COMPONENT_id", ""+c.getId());
        return list;

    }
    
    /** Gets the Resources by a component and deployment
     * 
     * @param c the Comp
     * @param dep
     * @return 
     * @throws gr.ntua.cslab.database.DBException 
    */
    public static List<Resource> getResources(Component c, Deployment dep) throws DBException{
        List<Constrain> cl= new java.util.LinkedList();
        if(c!=null) cl.add(new Constrain("COMPONENT_id", ""+c.getId()));
        
        cl.add(new Constrain("DEPLOYMENT_id", ""+dep.getId()));
        List<Resource> list = EntityTools.getByConstrains(Resource.class, cl, false);
        return list;

    }
    
    /** Gets the Resources by a component and deployment
     * 
     * @param dep the deployment whose resources we are getting
     * @return 
     * @throws gr.ntua.cslab.database.DBException 
    */
    public static List<Resource> getDeploymentResources(Deployment dep) throws DBException{
        List<Constrain> cl= new java.util.LinkedList();
        cl.add(new Constrain("DEPLOYMENT_id", ""+dep.getId()));
        List<Resource> list = EntityTools.getByConstrains(Resource.class, cl, false);
        return list;
    }
    
    /** Gets all the Metrics of a given deployment
     * 
     * @param comp the component whose metrics we are getting
     * @return 
     * @throws gr.ntua.cslab.database.DBException 
    */
    public static List<Metric> getMetrics(Component comp) throws DBException, Exception{
        return EntityTools.getByField(Metric.class, "COMPONENT_id", ""+comp.id);
    }
    
        /**
     * Gets the resource type with the specified String type (assumed unique)
     * 
     * @param type
     * @return 
     * @throws gr.ntua.cslab.database.DBException 
    */
    public static ResourceType getResourceTypeByName(String type) throws DBException{
        List<ResourceType> list = EntityTools.getByField(ResourceType.class, "type", type);
        if(list.isEmpty()){
            throw new DBException(DBException.NO_SUCH_ENTRY, "No resources found of type: "+type);
        }
        return list.get(0);

    }
    
    public static Deployment getDeploymentById(String id) throws Exception{
        Deployment rv = new Deployment();
        factory.createById(rv, id);
        return rv;
    }
    
    public static Application getApplicationById(String id) throws Exception{
        Application rv = new Application();
        factory.createById(rv, id);
        return rv;
    }
    
    public static List<ResizingAction> getResizingActions(String type, Component c){
        List<Constrain> con= new LinkedList();
        if(type!=null) con.add(new Constrain("lower(type)",type.toLowerCase()));
        if(c!=null) con.add(new Constrain("COMPONENT_ID", ""+c.id));
        List<ResizingAction> list = EntityTools.getByConstrains(ResizingAction.class, con, false);
        return list;
    }
    
    public static List<ComponentDependency> getDependencies(Component c){
        return EntityTools.getByField(ComponentDependency.class, "COMPONENT_from_id", ""+c.id);
    }
    
    public static List<ModuleDependency> getDependencies(Module m){
        return EntityTools.getByField(ModuleDependency.class, "MODULE_from_id", ""+m.id);
    }
            
    
    public static List<Deployment> getDeployments( Timestamp start, Timestamp finish, Application app){
        List<Constrain> lc = new LinkedList();
        if (start != null)  lc.add(new DBTools.Constrain("DEPLOYMENT.start_time", ">= ", "" + start));
        if (finish != null) lc.add(new DBTools.Constrain("DEPLOYMENT.end_time", " IS NULL OR DEPLOYMENT.end_time <=", "" + finish));
        if (app != null)    lc.add(new DBTools.Constrain("DEPLOYMENT.APPLICATION_id", app.id));
        LOG.info(lc.toString());
        return EntityTools.getByConstrains(Deployment.class, lc, false);
    }
    
    public static DeploymentState getDeploymentState(String deploymentId){
        
        String query = "select * FROM DEPLOYMENT_STATE WHERE DEPLOYMENT_id='"+deploymentId+"' order by timestamp FETCH FIRST 1 ROWS ONLY;";
        List<DeploymentState> l = EntityTools.getByQuery(DeploymentState.class, query);
        if(l.isEmpty()) return null;
        else return l.get(0);
    }

}
