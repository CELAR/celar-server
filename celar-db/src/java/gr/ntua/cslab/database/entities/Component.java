package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.ComponentTable;
import gr.ntua.cslab.database.Tables;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Component extends DBIDEntity {

    int moduleId, providedResourceId;
    String description;

    /**
     * Creates an unstored component from a module, provided resource and the
     * component table
     *
     * @param module the module this component belongs to
     * @param description this component's description
     * @param pr the provided resource this made from physically
     * @throws gr.ntua.cslab.database.entities.NotInDBaseException
     */
    public Component(Module module, String description, ProvidedResource pr) throws NotInDBaseException {
        super(Tables.componentTable);
        //check if the app is not strored in the database (for consistency reasons)
        if (module.id == 0 || module.modified) {
            throw new NotInDBaseException("the module must be stored "
                    + "in Database before the component is created");
        }
        if (pr.id == 0 || pr.modified) {
            throw new NotInDBaseException("the provided resource must be stored "
                    + "in Database before the component is created");
        }
        this.moduleId = module.id;
        this.providedResourceId = pr.id;
        this.description=description;
    }

    /**
     * Creates an previously stored component directly from the database
     *
     * @param id
     * @param table
     */
    public Component(int id) {
        super(id, Tables.componentTable);
    }

    /**
     * creates an unstored Component from a json object
     *
     * @param jo
     * @throws NotInDBaseException
     */
    public Component(JSONObject jo) throws NotInDBaseException {
        super(jo, Tables.componentTable);
    }

    /**
     * Stores the component in the database and retrieves the id he was assigned
     *
     * @return true if successful false if not
     */
    @Override
    public boolean store() {
        ComponentTable t = (ComponentTable) table;
        this.id = t.insertComponent(description,moduleId, providedResourceId);
        if (id != 0) {
            this.modified = false;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Component, id:" + id +" desription: "+description+
                " ModuleId:" + moduleId + " prov.ResourceId:" + providedResourceId;
    }

    @Override
    protected void fromMap(Map<String, String> fields) {
        this.id = Integer.parseInt(fields.get("id"));
        this.moduleId = Integer.parseInt(fields.get("MODULE_id"));
        this.providedResourceId = Integer.parseInt(fields.get("PROVIDED_RESOURCE_id"));
        this.description =fields.get("description");
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id", "" + id);
        json.put("MODULE_id", moduleId);
        json.put("PROVIDED_RESOURCE_id", providedResourceId);
        json.put("description", description);
        return json;
    }

    @Override
    void fromJSON(JSONObject jo) {
        this.moduleId = jo.getInt("MODULE_id");
        this.providedResourceId = jo.getInt("PROVIDED_RESOURCE_id");
        this.description=jo.getString("description");
    }
    
    static Component getByDescription(String description) throws NotInDBaseException {
        return Tables.componentTable.getComponentByDescription(description);
    }
}
