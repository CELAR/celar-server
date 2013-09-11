/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entities;

import database.Table;
import java.util.Map;

/**
 *Abstract DataBase Entity
 * @author cmantas
 */
public abstract class DBEntity {
	
	Table table; /*The Handler of the database where this entity will be stored*/

	protected boolean modified=true; /** True if the current state of the 
					   * entity is not stored in the DB
	 				   */
	/**
	 * Default constructor.
	 * by default the 
	 */
	public DBEntity(){
		modified=true;
	}

	/**
	 * Creates an entity from the fields of the database entry.
	 * Calls then fromMap method which is implemented in the subclasses
	 * @param fields
	 * @param table
	 */
	public DBEntity(Map <String,String> fields, Table table){
		this.table=table;
		fromMap(fields);
		modified=false;
	}



	/**
	 *Reads the entity-specific parameters from the Database fields and stores
	 * them in the appropriate class variables;
	 * @param fields
	 */
	abstract protected void fromMap(Map <String,String> fields);

	public boolean isModified(){
		return modified;
	}

	

	/**
	 * Stores the entity in the appropriate Database Table
	 * @return
	 */
	abstract public boolean store();
        

	
}
