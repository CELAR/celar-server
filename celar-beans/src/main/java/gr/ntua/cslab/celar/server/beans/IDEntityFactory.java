package gr.ntua.cslab.celar.server.beans;

/**
 * Implementations of this interface should be able to create an entity from its Id by
 * loading the rest of the fields from a persistent store
 * @author cmantas
 */
public interface IDEntityFactory {
    
    /**
     * Fills the entity's fields with info retrieved from a given ID
     * @param c
     * @param id 
     * @throws java.lang.Exception if no entity is found with said id
     */
    abstract public void create(IDEntity c, int id) throws Exception;
}
