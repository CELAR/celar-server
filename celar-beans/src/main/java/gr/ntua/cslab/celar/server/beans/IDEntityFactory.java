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
     */
    abstract public void create(IDEntity c, int id);
}
