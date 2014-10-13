/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import java.lang.reflect.Field;
import java.util.Map;
import org.json.JSONObject;

/**
 * A DBEntity that has a unique, auto-increment integer "id" as its primary key
 * @author cmantas
 */
public abstract class DBIDEntity extends DBEntity {

    /**
     * a unique, auto-increment integer primary key "id" 
     */
    int id;
    
     public DBIDEntity(){
         super();
         this.id=0;
     }
     
    /**
     * Creates a DBIDEntity given only its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
     public DBIDEntity(int id) throws DBException{
         Map fields = DBTools.doSelectByID(this.getTableName(), id);
         this.fromMap(fields);
     }
     
    /**
     * Creates a DBIDEntity given its JSON representation
     * <b>Note</b>pass-through between the child and father constructors
     * @param jo 
     */
    public DBIDEntity(JSONObject jo){
        super(jo);
    }
    
   
    /**
     * Creates a DBIDEntity given a mapping representation of fields-->values
     * <b>Note:</b> pass-through between the child and father constructors
     * @param jo 
     */
    DBIDEntity(Map<String, String> fields) {
        super(fields);
    }
    
   /**
    * Gets the "id" of this DBIDEntity
    * @return 
    */
    public int getId(){
        return this.id;
    }

    /**
     * Stores the DBIDEntity in the appropriate Database Table
     * <b>Note: </b> Retrieves the id the DBMS will give to the tuple
     *
     * @throws gr.ntua.cslab.db_entities.DBException if there is a failure
     * executing the query storing this entry
     */
    public void store() throws DBException {
        System.out.println(this.getFieldMap());
        Map m = this.toMap();
        this.id = DBTools.insertIDData(this.getTableName(), m);
        
    }
    
    /**
     * Deletes the entity from the appropriate table based on (only) its ID;
     * @throws gr.ntua.cslab.db_entities.DBException if there is a failure
     * executing the query deleting this entry
     */
    public void delete() throws DBException{
        DBTools.doDeleteID(this.getTableName(), id);
    }
    
}
