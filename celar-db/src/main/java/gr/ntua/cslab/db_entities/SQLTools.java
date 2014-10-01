/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.db_entities;

import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Helper class containing static String method creating SQL queries
 *
 * @author cmantas
 */
public class SQLTools {

    /**
     * encodes the given text for entry in the database
     *
     * @param text
     * @return
     */
    static protected String encode(String text) {
        return StringEscapeUtils.escapeXml(text);
    }

    /**
     * decodes text retrieved from the DB in its previous state
     *
     * @param text
     * @return
     */
    static protected String decode(String text) {
        return StringEscapeUtils.unescapeXml(text);
    }

    /**
     * This method returns a SQL string inserting a new tuple into the database.
     * The tuple is modeled as a {@link Map} (set of key-values). The key
     * corresponds to the table field and the values are the actual tuple
     * values.
     * <br/><br/>
     * <b>Attention</b>: This method expects that the values are already
     * formatted and compatible to SQL (e.g., strings need ' characters and
     * integers do not).
     *
     * @param tuppples
     * @return SQL string for the insert function
     */
    static String insertSQL(String tableName, Map<String, String> tupples) {
        String values = "";
        String keys = "";

        //generate SQL keys SET
        for (Map.Entry<String, String> e : tupples.entrySet()) {
            keys += "\"" + e.getKey() + "\",";
        }
        keys = keys.substring(0, keys.length() - 1);
        keys = "(" + keys + ")";

        //generate SQL VALUES set
        for (Map.Entry<String, String> e : tupples.entrySet()) {
            String value = e.getValue();
            //escape special values
            if (value.equals("null")) {
                value="NULL";
            } 
            if (value.equals("NULL")) {
                /*do nothing*/;
            } else if (value.equals("NOW()")) {
                /*do nothing*/;
            } else if (value.equals("DEFAULT")) {
                /*do nothing*/;
            } else {
                value = "'" + encode(value) + "'";
            }

            values += value + ",";
        }
        values = values.substring(0, values.length() - 1);
        values = "(" + values + ")";
        String sql = "INSERT INTO \"" + tableName + "\" " + keys + " VALUES " + values + ";";
        return sql;
    }

    /**
     * Returns an sql string doing a SELECT over the table. This is a naive
     * implementation, as the parameter must be a valid sql "WHERE" statement.
     *
     * @param tableName
     * @param whereStatement
     * @return
     */
    public static String selectSQL(String tableName, String whereStatement) {
        return "SELECT * FROM \"" + tableName + "\" WHERE " + whereStatement + ";";
    }

    /**
     * Returns an SQL statement used to delete an entry from the table.
     *
     * @param tableName
     * @param tableField
     * @param tableValue
     * @return
     */
    protected static String deleteSQL(String tableName, String tableField, String tableValue) {
        String sql = "DELETE FROM \"" + tableName + "\" WHERE " + tableField + "=" + tableValue;
//		System.out.println(sql);
        return sql;
    }

    /**
     * Returns an SQL statement used to delete an entry from the users table.
     *
     * @param tableName
     * @param tupples
     * @return
     */
    protected static String deleteSQL(String tableName, Map<String, String> tupples) {
        String values = "";
        String keys = "";
        //generate SQL keys SET
        for (Map.Entry<String, String> e : tupples.entrySet()) {
            keys += "\"" + e.getKey() + "\",";
        }
        keys = keys.substring(0, keys.length() - 1);
        keys = "(" + keys + ")";

        //generate SQL VALUES set
        for (Map.Entry<String, String> e : tupples.entrySet()) {
            String value = e.getValue();
            value = "'" + encode(value) + "'";
            values += value + ",";
        }
        values = values.substring(0, values.length() - 1);
        values = "(" + values + ")";
        String sql = "DELETE from \"" + tableName + "\"  WHERE " + keys + " = " + values + ";";
        return sql;
    }

}
