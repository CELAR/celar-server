/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database.entities;

import gr.ntua.cslab.database.IDTable;
import java.util.Map;
import org.json.JSONObject;

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
        
       public  DBIDEntity(JSONObject jo, IDTable table) {
           super(jo, table);
           this.table=table;
        }

	public void delete(){
		this.table.delete(id);
		this.id=0;
		this.modified=true;
		
	}
        
        public int getId(){
            return this.id;
        }
        
        public boolean exists(){
            return id==-1?false:true;
        }

}
