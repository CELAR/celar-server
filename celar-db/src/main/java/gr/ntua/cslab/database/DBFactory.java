/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database;

import gr.ntua.cslab.celar.server.beans.IDEntity;
import gr.ntua.cslab.celar.server.beans.IDEntityFactory;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import java.util.Map;

/**
 *
 * @author cmantas
 */
public class DBFactory implements IDEntityFactory{
    
    @Override
    public  void create(IDEntity c, int id) throws Exception{        
        createById(c, id); 
    }
    
    
    public void createById(ReflectiveEntity c, Object id) throws Exception{
        Map fields = DBTools.doSelectByID(TableTools.getTableName(c.getClass()), id);
        if (fields==null)
            throw new Exception("There is no "+c.getClass().getSimpleName()+" with an id="+id);
        EntityTools.fromFieldMap(c, fields);
    }
}
