package database.entities;

import database.IDTable;
import database.UserTable;
import java.util.Map;
import database.Tables;

/**
 *
 * @author cmantas
 */
public class User extends DBIDEntity{
	
	String name;

	/**
	 * Creates an unstored user from a name and the user table
	 * @param name
	 * @param table 
	 */
	public User(String name) {
		super(Tables.usertable);
		this.name=name;
	}

	
	/**
	 * Creates a previously stored user as retrieved from the fields of the database
	 * @param fields
	 * @param table 
	 */
	public User(Map <String,String> fields){
		super(fields, Tables.usertable);
		this.name=fields.get("name");
	}
	
	/**
	 * Creates an previously stored user directly from  the database
	 * @param id
	 * @param table 
	 */
	public User(int id){
		super(id, Tables.usertable);
	}
	

	/**
	 * Stores the user in the database and retrieves the id he was assigned
	 * @return true if successful false if not 
	 */
	@Override
	public boolean store() {
		UserTable t=(UserTable) table;
		this.id=t.insertUser(name);
		if(id!=0){
			this.modified=false;
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return "User: "+name+" , id:"+id;
	}


	@Override
	protected void fromMap(Map <String,String> fields) {
		this.id=Integer.parseInt(fields.get("id"));
		this.name=fields.get("name");
	}




	
}
