/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database;

import static gr.ntua.cslab.database.Table.DEBUG;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cmantas
 */
public abstract class DBConnectable {
	final static  String USER="root";
	final static String PASSWORD="";
	final static String DB_NAME="celar_db";
	final static String DRIVER="com.mysql.jdbc.Driver";
        
        protected Connection connection;

	/**
	 * Opens the connection with the database.
	 * 
	 * <b>Note:</b> SLOW procedure, use with care!
	 */
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
			connection = DriverManager
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
	public void closeConnection(){
		try {
			connection.close();
			if(DEBUG)	System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
}
