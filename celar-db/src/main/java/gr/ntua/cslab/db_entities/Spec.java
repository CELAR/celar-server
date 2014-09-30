/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import gr.ntua.cslab.db_entities.DBTools.Constrain;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cmantas
 */
public class Spec extends DBIDEntity {

    int providedResourceId;
    String property, value;

    /**
     * Default Constructor
     */
    public Spec() {
        super();
    }

    /**
     * Creates a Spec entity based on its ProvidedResource father and 
     * the property - value pair for this Spec
     * @param pr
     * @param property
     * @param value 
     */
    public Spec(ProvidedResource pr, String property, String value) {
        this.providedResourceId = pr.id;
        this.property = property;
        this.value = value;
    }

    Spec(Map<String, String> inmap) {
        super(inmap);
    }

    /**
     * Creates a Spec entity given its id
     * (retrieves the rest of the fields from the DB)
     * @param id
     * @throws DBException in case no Entity is found with the given ID in this table
     */
    public Spec(int id) throws DBException {
        super(id);
    }
    

    @Override
    protected void fromMap(Map<String, String> fields) {
        this.id = Integer.parseInt(fields.get("id"));
        this.providedResourceId = Integer.parseInt(fields.get("PROVIDED_RESOURCE_id"));
        this.property = fields.get("property");
        this.value = fields.get("value");
    }

    @Override
    protected Map<String, String> toMap() {
        Map<String, String> map = new java.util.TreeMap();
        map.put("id", ""+id);
        map.put("PROVIDED_RESOURCE_id",""+providedResourceId);
        map.put("property", ""+property);
        map.put("value", ""+value);
        return map;
    }

    /**
     * Returns the name of the table that this Entity is saved to
     * @return 
     */
    @Override
    public String getTableName() {
        return "SPECS";
    }

    /**
     * Returns a list of all the Specs whose father is the given ProvidedResouce
     * @param providedResource
     * @return
     * @throws DBException 
     */
    public static List<Spec> getByProvidedResource(ProvidedResource providedResource) throws DBException {
        List<Spec> rv = new java.util.LinkedList();
        Spec dummy = new Spec();

        for(Object e: dummy.getByField("PROVIDED_RESOURCE_id", ""+providedResource.getId())){
            rv.add((Spec) e);
        }
        return rv;
    }
    
    
    private static List intersect(List A, List B) {
        List rtnList = new java.util.LinkedList<>();
        for (Object dto : A) {
            if (B.contains(dto)) {
                rtnList.add(dto);
            }
        }
        return rtnList;
    }

    
    public static List<Integer> getProvidedResourceIDsByProperty(String property, String value){
        List<Constrain> lc= new java.util.LinkedList();
        Constrain prop = new Constrain("property", property);
        lc.add(prop);
        Constrain val = new Constrain("value", value);
        lc.add(val);
        Spec dummy = new Spec();
        List<Integer> rv = new java.util.LinkedList();
        for(Object e: dummy.getByConstrains(lc, false)){
            rv.add(((Spec) e).providedResourceId);
        }
        return rv;
    }
    
    public static List<Integer> getProvidedResourceIDsByFlavor(int cores, int ram, int disk){
       List<Integer> byCores =  getProvidedResourceIDsByProperty("cores", ""+cores);
        System.out.println(byCores);
       List<Integer> byRam =  getProvidedResourceIDsByProperty("ram", ""+ram);
        System.out.println(byRam);
       List<Integer> byDisk =  getProvidedResourceIDsByProperty("disk", ""+disk);
        System.out.println(byDisk);
       List<Integer> rv = intersect(byCores, byRam);
        System.out.println(rv);
       rv = intersect(rv, byDisk);
        System.out.println(rv);
       return rv;
    }
    
}
