package database;

import static database.DBConnectable.DB_NAME;
import static database.DBConnectable.DRIVER;
import static database.DBConnectable.USER;
import static database.Table.DEBUG;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringEscapeUtils;


/**
 * This abstract class is used to provide methods to its subclasses. Any class needed to provide 
 * a database interface must extends this class.
 * 
 * @author Giannis Giannakopoulos
 *
 */
public abstract class Table implements DBConnectable{

	public static boolean DEBUG=true;
	protected Connection connection;

	public Table(){super();}
	
	/**
	 *Creates a table with an open connection if the parameter is set to true
	 * @param openConnectioconnectn
	 */
	public Table(boolean connect){
		if(connect)this.openConnection();
	}

	/**
	 * Opens the connection with the database.
	 * 
	 * <b>Note:</b> SLOW procedure, use with care!
	 */
	@Override
	public void openConnection() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (DEBUG) {
			System.out.println("Driver registered");
		}
		try {
			this.connection = DriverManager
				.getConnection("jdbc:mysql://localhost/"
				+DB_NAME+"?"
				+ "user="+USER);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (DEBUG) {
			System.out.println("Connection created");
		}
	}
	
	/**
	 * Closes the connection with the database.
	 */
	@Override
	public void closeConnection(){
		try {
			this.connection.close();
			if(DEBUG)	System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method implemented by any sublaclass. This method will return the name of the table
	 * as a String.
	 * @return
	 */
	protected abstract String getTableName();
	
	/**
	 * This method returns a SQL string inserting a new tuple into the database. The tuple
	 * is modeled as a {@link Map} (set of key-values). The key corresponds to the table field and the values are the
	 * actual tuple values.
	 * <br/><br/>
	 * <b>Attention</b>: This method expects that the values are already formatted and compatible to SQL (e.g., strings
	 * need ' characters and integers do not).  
	 * 
	 * @param object
	 * @return
	 */
	protected String insertSQL(Map<String, String> tupples){
		String values="";
		String keys="";
		
		//generate SQL keys SET
		for(Entry<String, String> e:tupples.entrySet())
			keys+=e.getKey()+",";
		keys=keys.substring(0, keys.length()-1);
		keys="("+keys+")";

		//generate SQL VALUES set
		for(Entry<String, String> e:tupples.entrySet()){
			String value=e.getValue();
			//escape special values
			if(value.equals("NULL"))
				/*do nothing*/;
			else if (value.equals("NOW()"))
				/*do nothing*/;
			else
				value="'"+encode(value)+"'";
			
			values+=value+",";
		}
		values=values.substring(0,values.length()-1);
		values="("+values+")";
		String sql = "INSERT INTO "+this.getTableName()+" "+keys+" VALUES "+values+";";
		return sql;
	}
	
	/**
	 * Returns an SQL statement used to delete an entry  from the users table.
	 * @param tableField
	 * @param tableValue
	 * @return
	 */
	protected String deleteSQL(String tableField, String tableValue) {
		String sql = "DELETE FROM "+this.getTableName()+" WHERE "+tableField+"="+tableValue;
//		System.out.println(sql);
		return sql;
	}
	
	/**
	 * Returns an sql string doing a SELECT over the table. 
	 * This is a naive implementation, as the parameter
	 * must be a valid sql "WHERE" statement. 
	 * @param whereStatement
	 * @return
	 */
	protected String selectSQL(String whereStatement){
		return "SELECT * FROM "+this.getTableName()+" WHERE "+whereStatement;
	}
	
	/**
	 * Returns the max value of a specified field, null if empty
	 * @param field
	 * @return 
	 */
	protected String maxValue(String field){
		Statement statement;
		String query="SELECT MAX("+field+") FROM "+ this.getTableName()+";";
		try {
			statement = this.connection.createStatement();
			ResultSet set=statement.executeQuery(query);
			set.next();
			String result=set.getString("MAX("+field+")");	
			set.close();
			statement.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}


	/**
	 * Method used to insert previously formatted to the database.
	 * The data (modeled as a Map) must already be formatted in  a manner of
	 * Field_Name->Value
	 * The method returns true if the insertion is done, else false.
	 * @param data a map of fields->values for this particular table
	 * @return true if insertion is successful
	 * 
	 */
	public boolean insertData(Map<String, String> data){
		try {
			Statement statement=this.connection.createStatement();
			statement.execute(this.insertSQL(data));
			statement.close();
			return true;
		} catch (SQLException e) {
			if(Table.DEBUG) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * encodes the given text for entry in the database 
	 * @param text
	 */
	static protected String encode(String text){
		return StringEscapeUtils.escapeXml(text);	
	}
	/**
	 * decodes text retrieved from the DB in its previous state
	 * @param text
	 */
	static protected String decode(String text){
		return StringEscapeUtils.unescapeXml(text);
	}

}