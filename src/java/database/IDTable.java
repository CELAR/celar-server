package database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This abstract class is used to provide methods to its subclasses. Any class needed to provide 
 * a database interface must extends this class.
 * 
 * @author Giannis Giannakopoulos
 *
 */
public abstract class IDTable extends Table implements DBIdentifiable{

	public IDTable(){super();}
	
	/**
	 *Creates a table with an open connection if the parameter is set to true
	 * @param onnect
	 */
	public IDTable(boolean connect){
		super(connect);	
	}

	/**
	 * Checks if the entry with the specified id exists 
	 * @param Id
	 * @return 
	 */
	@Override
	public boolean exists(int Id){
            try {
                        ResultSet set=executeQuery(this.selectSQL("id='"+Id+"'"));
                        boolean result=set.next();
                        set.close();
                        return result;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
	}
	
	
	/**
	 * Deletes a row from the user table.
	 * @param id
	 * @return
	 */
	@Override
	public boolean delete(int id){
		try {
			if(!this.exists(id))
				return false;
			Statement statement = this.connection.createStatement();
			statement.execute(this.deleteSQL("id","'"+id+"'"));
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the next available (max+1) value that can be used as an id
	 * for the table.
	 * @return 
	 */
	protected int getNextId(){
		String maxVal=this.maxValue("id");
		if(maxVal==null)
			return 1;
		else
			return 1+ Integer.parseInt(maxVal);
			
	}
	
	/**
	 * Retrieves the entry stored under the specified id
	 * @param id
	 * @return a Map of ColumnName->Value for this entry
	 */
	public Map<String, String> getById(int id) {
		try {
			Map<String, String> result = new java.util.TreeMap();
			Statement statement = this.connection.createStatement();
			ResultSet rs = statement.executeQuery(this.selectSQL("id=" + id));
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			rs.next();
			for (int i = 1; i < columnCount + 1; i++) {
				String name = rsmd.getColumnName(i);
				String value = decode(rs.getString(name));
				result.put(name, value);
			}

			return result;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	
	
}