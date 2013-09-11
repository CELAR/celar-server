/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entities;

import database.IDTable;
import database.Table;
import java.util.Map;

/**
 *
 * @author cmantas
 */
public abstract class DBIDEntity extends DBEntity {
	int id=0;
	IDTable table;
	
	public DBIDEntity(){
		super();
		id=0;
	}

	public DBIDEntity(IDTable table){
		this.table=table;
	} 

	public DBIDEntity(Map<String,String>fields, IDTable table){
		super(fields, table);
	}

	public DBIDEntity(int id,IDTable table){
		this.table=table;
		Map <String,String> fields=table.getById(id);
		fromMap(fields);
		modified=false;
	}

	public void delete(){
		this.table.delete(id);
		this.id=0;
		this.modified=true;
		
	}
        
        public int getId(){
            return this.id;
        }

}
