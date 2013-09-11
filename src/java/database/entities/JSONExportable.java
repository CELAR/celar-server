package database.entities;

import org.json.JSONObject;

public interface JSONExportable {
    
    
    /**
     * Converts the Entity to a JSON representation
     * @return a String in JSON format
     */
    public JSONObject toJSONObject();
}
