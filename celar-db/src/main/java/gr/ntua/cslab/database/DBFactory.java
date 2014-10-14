/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.database;

import fasolakia.IDEntity;
import fasolakia.IDEntityFactory;
import java.util.Map;

/**
 *
 * @author cmantas
 */
public class DBFactory implements IDEntityFactory{
    
    public void create(IDEntity c, int id){        
        Map fields = DBTools.doSelectByID(TableTools.getTableName(c.getClass()), id);
        EntityTools.fromFieldMap(c, fields); 
    }
}
