package gr.ntua.cslab.celar.server.beans;


/**
 * A DBEntity that has a unique, auto-increment integer "id" as its primary key
 * @author cmantas
 */
public abstract class IDEntity  extends ReflectiveEntity{

    /**
     * a unique, auto-increment integer primary key "id" 
     */
    public int id;
    
    protected static IDEntityFactory factory = null;
    
     public IDEntity(){
         this.id=0;
     }
     
    /**
     * Creates a DBIDEntity given only its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
     public IDEntity(int id) throws Exception{           
            if(factory!=null){
                factory.create(this, id);
            }
            else{
                throw new Exception("You cannot create an entity by its id if you don't implement an 'IDEntityFactory'");
            }
     }
     
  
   /**
    * Gets the "id" of this IDEntity
    * @return 
    */
    public int getId(){
        return this.id;
    }


    
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Sets the entity factory (static) that creates the entities by their id
     * @param f 
     */
    public static void setFactory(IDEntityFactory f){factory=f;};
    
}
