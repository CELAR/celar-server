package gr.ntua.cslab.celar.server.beans;

/**
 * Represents a 'User' entity as it is stored in celarDB
 * @author cmantas
 */
public class Probe extends ReflectiveEntity{
    public String name, details;
    
    public Probe(){
        super();
    }
    
   public Probe(String name, String details){
        this.name=name;
        this.details=details;
    }
   






}
