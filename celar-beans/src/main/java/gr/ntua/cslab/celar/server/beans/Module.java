package gr.ntua.cslab.celar.server.beans;

/**
 * Represents a 'Module' entity as it is stored in celarDB
 * @author cmantas
 */
public class Module extends IDEntity{
    public String name,application_Id;
    
    /**
     * Default constructor
     */
    public Module(){
        super();
    }
    
    /**
     * Copy constructor
     * @param m 
     */
   public Module(Module m) {
        super(m);
    }
    
    /**
     * Creates a Module entity from a given name and its father "Application"
     * @param name the name of the module
     * @param app the Application instance whose child is this Module
     */
    public Module(String name, Application app){
        this.name=name;
        this.name=name;
        this.application_Id=app.getId();
    }
    
    /**
     * Creates a Module entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws Exception 
     */
    public Module(int id) throws Exception{
        super(id);
    }
    
    /**
     * Returns the name of this Module
     * @return 
     */
    public String getName(){ return this.name;}
}
