package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.ApplicationTable;
import gr.ntua.cslab.database.Tables;
import java.sql.Timestamp;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Application extends DBIDEntity{
	
	String description;
	Timestamp submitted;
	int userId;

	/**
	 * Creates an unstored application from a description, submitted and the application table
	 * @param name
	 * @param table 
	 */
	public Application(String description, Timestamp submitted, User user) throws NotInDBaseException {
		super(Tables.appTable);
		this.description=description;
		this.submitted=submitted;
		if(user.id==0||user.modified)
			throw new NotInDBaseException("the User must be stored "
				+ "in Database before the application is created");
		this.userId=user.id;
	}

	
	
	/**
	 * Creates an previously stored application directly from  the database
	 * @param id
	 * @param table 
	 */
	public Application(int id){
		super(id, Tables.appTable);
	}
        
        
         /**
         * creates an unstored Application from a json object
         * @param jo
         */
        public Application(JSONObject jo){
            super(jo, Tables.appTable);
        }
	


	/**
	 * Stores the application in the database and retrieves the id he was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {

		ApplicationTable t=(ApplicationTable) table;
		this.id=t.insertApplication(description,submitted, userId);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "Application: "+description+", id:"+id+" submitted:"+submitted+", userId:"+userId;
	}

	


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.description=fields.get("description");
		this.submitted=Timestamp.valueOf(fields.get("submitted"));
		this.userId=Integer.parseInt(fields.get("USER_id"));
	}

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("description", description);
        json.put("submitted", submitted.toString());    
        json.put("USER_id", ""+userId);        
        return json;
    }

   
        @Override
        void fromJSON(JSONObject jo)  {
        String appDescription = (String) jo.get("description");
        int appUserId = Integer.parseInt((String) jo.get("USER_id"));
        String appSubmittedString = (String) jo.get("submitted");
        Timestamp appSubmittedTs;
        if (appSubmittedString.equalsIgnoreCase("now")) {
            appSubmittedTs = new Timestamp(System.currentTimeMillis());
        } else {
            appSubmittedTs = Timestamp.valueOf(appSubmittedString);
        }
        this.description=appDescription;
        this.submitted=appSubmittedTs;
        this.userId=appUserId;
    }


	
}
