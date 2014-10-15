package gr.ntua.cslab.celar.server.beans;

/**
 *
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
