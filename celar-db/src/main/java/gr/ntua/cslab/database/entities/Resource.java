package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.ResourcesTable;
import gr.ntua.cslab.database.Tables;
import java.sql.Timestamp;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
public class Resource extends DBIDEntity {
	
	int deploymentId, componentId, providedResourceId;
	Timestamp startTime, endTime;

	/**
	 * Creates an unstored resource from a type, submitted and the resource table
	 * @param deployment the deployment this resizing action is for
	 * @param component the component this resizing action applies to
	 * @param table a resource table
	 */
	public Resource(Deployment deployment, Component component, ProvidedResource pr,
		Timestamp start_time, Timestamp end_time) 
		throws NotInDBaseException {
		super(Tables.resTable);
		//check if the app is not strored in the database (for consistency reasons)
		if(deployment.id==0||deployment.modified)
			throw new NotInDBaseException("the deployment must be stored "
				+ "in Database before the resource is created");
		if(component.id==0||component.modified)
			throw new NotInDBaseException("the component must be stored "
				+ "in Database before the resource is created");		
		if(pr.id==0||pr.modified)
			throw new NotInDBaseException("the provided resource must be stored "
				+ "in Database before the resource is created");
		this.deploymentId=deployment.id;
		this.componentId=component.id;
		this.providedResourceId=pr.id;
		this.startTime=start_time;
		this.endTime=end_time;
	}

	
	
	/**
	 * Creates an previously stored resource directly from  the database
	 * @param id
	 */
	public Resource(int id){
		super(id, Tables.resTable);
	}
	
        
        /**
         * creates an unstored Resource from a json object
         * @param jo
         * @throws NotInDBaseException 
         */
        public Resource(JSONObject jo) throws NotInDBaseException{
            super(jo, Tables.resTable);
        }


	/**
	 * Stores the resource in the database and retrieves the id it was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		ResourcesTable t=(ResourcesTable) table;
		this.id=t.insertResource(deploymentId, componentId,
				providedResourceId, startTime, endTime);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "Resource id:"+id+" deploymentid:"+deploymentId
			+" componentID:"+componentId+" pr.Res.Id:"+providedResourceId
			+" start:"+startTime+" end: "+endTime;
	}


	@Override
	protected void fromMap(Map<String, String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.deploymentId=Integer.parseInt(fields.get("DEPLOYMENT_id"));
		this.componentId=Integer.parseInt(fields.get("COMPONENT_id"));
		this.providedResourceId=Integer.parseInt(fields.get("PROVIDED_RESOURCE_id"));
		this.startTime= Timestamp.valueOf(fields.get("start_time"));
		String end=fields.get("end_time");
		this.endTime=end==null?null:Timestamp.valueOf(end);
		
	}

    @Override
    public JSONObject toJSONObject() {
        JSONObject json= new JSONObject();
        json.put("id", id);
        json.put("DEPLOYMENT_id", deploymentId);
        json.put("COMPONENT_id", componentId);
        json.put("PROVIDED_RESOURCE_id", providedResourceId);
        json.put("start_time", startTime.toString());
        if(endTime!=null) json.put("end_time",endTime.toString());        
        return json;
    }

    @Override
    void fromJSON(JSONObject jo)  {
        this.deploymentId=jo.getInt("DEPLOYMENT_id");
        this.componentId=jo.getInt("COMPONENT_id");
        this.providedResourceId=jo.getInt("PROVIDED_RESOURCE_id");
        String startTimeString=jo.getString("start_time");
        this.startTime=Timestamp.valueOf(startTimeString);
        if(jo.has("end_time")){
            String endTimeString=jo.getString("end_time");
            this.endTime=Timestamp.valueOf(endTimeString);
        }
        else  this.endTime=null;
    }


	
}
