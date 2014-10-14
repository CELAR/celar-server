/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.ntua.cslab.db_entities;

import java.util.Map;

/**
 *
 * @author cmantas
 */
public class Probe extends DBEntity{
    String name, details;
    
   public Probe(String name, String details){
        this.name=name;
        this.details=details;
    }
   
    public Probe(Map<String, String> inmap){
        super(inmap);
    }

    @Override
    public String getTableName() {
       return "Probes";
    }






}
