/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author cmantas
 */
public interface DBIdentifiable {

	/**
	 * Checks if the user with the specified id exists
	 * @param Id
	 * @return 
	 */
	public boolean exists(int Id);
	
	
	/**
	 * Deletes a user from the user table.
	 * @param userId
	 * @return
	 */
	public boolean delete(int appId);
}
