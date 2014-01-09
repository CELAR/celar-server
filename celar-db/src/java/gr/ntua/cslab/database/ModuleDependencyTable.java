/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database;


import gr.ntua.cslab.database.entities.Dependency;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class ModuleDependencyTable extends Table {
	
	public ModuleDependencyTable() {super();}
	
	
	public ModuleDependencyTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "MODULE_DEPENDENCY";
	}
	
	
	/**
	 * Inserts a moduleDependency  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param MODULE_from_id the module the dependency is from (dependant) 
	 * @param MODULE_to_id the module the dependency is to (requirement)
	 * @param type the type of the moduleDependency
	 */
	public boolean insertModuleDependency(int MODULE_from_id, int MODULE_to_id, String type){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("MODULE_from_id", Integer.toString(MODULE_from_id));
		data.put("MODULE_to_id",Integer.toString(MODULE_to_id));
		data.put("type",type);
		return	this.insertData(data);
	}

	
	/**
	 * Finds the module the specified module depends on
	 * @param MODULE_from_id
	 * @return 
	 */
	public List<Dependency> findDependencies(int MODULE_from_id){
		List<Dependency> dependencies=new java.util.LinkedList();
		
		Statement statement;
		String query=this.selectSQL("MODULE_from_id="+MODULE_from_id);
		try {
			statement = this.connection.createStatement();
			ResultSet set=statement.executeQuery(query);
			while(set.next()){
			String type=set.getString("type");
			int to=Integer.parseInt(set.getString("MODULE_to_id"));
			Map<String,String> m=new java.util.TreeMap();
                        m.put("MODULE_from_id", ""+MODULE_from_id);
                        m.put("MODULE_to_id", ""+to);
                        m.put("type", type);
			dependencies.add(
				new Dependency(m, Dependency.MODULE)
                                );
			}
			set.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return dependencies;
		
	}
	
	/**
	 * Deletes a the dependencies from one module to another 
	 * @param MODULE_from_id
	 * @param MODULE_to_id
	 * @return # of rows affected 
	 */
	public int deleteDependency(int MODULE_from_id, int MODULE_to_id){
		String query= "DELETE FROM "+this.getTableName()+""
			+" WHERE MODULE_from_id="+MODULE_from_id
			+" AND MODULE_to_id="+MODULE_to_id;
		Statement statement;
		int result=0;
		try {
			statement = this.connection.createStatement();
			result=statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}