package database;


import database.entities.Dependency;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class ComponentDependencyTable extends Table {
	
	public ComponentDependencyTable() {super();}
	
	
	public ComponentDependencyTable(boolean connect) {super(connect);}


	@Override
	protected String getTableName() {
		return "COMPONENT__DEPENDENCY";
	}
	
	
	/**
	 * Inserts a component Dependency  with the specified data. 
	 * It returns true if everything went smoothly, else it returns false.
	 * @param COMPONENT_from_id the component the dependency is from (dependant) 
	 * @param COMPONENT_to_id the component the dependency is to (required)
	 * @param type the type of the componentDependency
	 */
	public boolean insertModuleDependency(int COMPONENT_from_id, int COMPONENT_to_id, String type){
		Map<String, String> data = new java.util.TreeMap<String, String>();
		data.put("COMPONENT_from_id", Integer.toString(COMPONENT_from_id));
		data.put("COMPONENT_to_id",Integer.toString(COMPONENT_to_id));
		data.put("type",type);
		return	this.insertData(data);
	}

	
	/**
	 * Finds the components the specified component depends on
	 * @param COMPONENT_from_id
	 * @return 
	 */
	public List<Dependency> findDependencies(int COMPONENT_from_id){
		List<Dependency> dependencies=new java.util.LinkedList();
		
		Statement statement;
		String query=this.selectSQL("COMPONENT_from_id="+COMPONENT_from_id);
		try {
			statement = this.connection.createStatement();
			ResultSet set=statement.executeQuery(query);
			while(set.next()){
			String type=set.getString("type");
			int to=Integer.parseInt(set.getString("COMPONENT_to_id"));
			dependencies.add(
				new Dependency(COMPONENT_from_id, to, type));
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
	 * Deletes a the dependencies from one component to another 
	 * @param COMPONENT_from_id
	 * @param COMPONENT_to_id
	 * @return # of rows affected 
	 */
	public int deleteDependency(int COMPONENT_from_id, int COMPONENT_to_id){
		String query= "DELETE FROM "+this.getTableName()+""
			+" WHERE COMPONENT_from_id="+COMPONENT_from_id
			+" AND COMPONENT_to_id="+COMPONENT_to_id;
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