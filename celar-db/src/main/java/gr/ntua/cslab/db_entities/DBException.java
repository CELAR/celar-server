/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import java.sql.SQLException;

/**
 *
 * @author cmantas
 */
public class DBException extends SQLException {
    
    /**
     * The byte type of the exception
     */
    byte type = 0;
    String sqlCause;

    //define types
    public final static byte FOREIGN_KEY = 1;
    public final static byte NO_SUCH_ENTRY = 2;

    
    /**
     * Creates a DBException from an SQLexception
     * @param father the SQLException that will be wrapped in a DBException
     * @param sqlCause the SQL string that caused the exception
     */
    DBException(SQLException father, String sqlCause) {
        super(father);
        this.sqlCause = sqlCause;
        if (is_foreign_key()) {
            type = FOREIGN_KEY;
        }
    }

    /**
     * Creates an DBException from byte type and a string with the details
     * @param type
     * @param detail 
     */
    public DBException(byte type, String detail) {
        super("Detail: " + detail);
        this.type = type;

    }

    
    private boolean is_foreign_key() {
        String msg = this.getMessage();
        if (msg.indexOf("foreign key constraint") != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * gets the details string for this exception
     * @return 
     */
    public String getDetail() {
        String msg = this.getMessage();
        int index = msg.indexOf("Detail:");
        index += 6;
        return msg.substring(index);
    }

    /**
     * Gets the type of the exception as a string
     * @return 
     */
    public String getType() {
        switch (type) {
            case FOREIGN_KEY:
                return "Foreign Key Violation";
            case NO_SUCH_ENTRY:
                return "Entry not in the DB";
            default:
                return "SQL Error";
        }
    }

    @Override
    public String toString() {
        return "Type: " + this.getType() + "\n\tDetails: " + this.getDetail() + "\n\tCause: " + this.sqlCause;
    }

}
