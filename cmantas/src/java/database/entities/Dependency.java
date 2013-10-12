/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entities;

/**
 *
 * @author cmantas
 */
public class Dependency {
	String type;
	int from_id, to_id;



	public Dependency( int from_id, int to_id, String type) {
		this.type = type;
		this.from_id = from_id;
		this.to_id = to_id;
	}

	@Override
	public String toString() {
		return  type + ":{" + from_id + "-->" + to_id + '}';
	}

	
	
}
