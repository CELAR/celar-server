/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pro
 */
public class Tables  extends DBConnectable{
    
                
        	//init table objects
		public static UserTable usertable = new UserTable(true);
		public static ModuleTable moduleTable=new ModuleTable(true);
		public static ApplicationTable appTable=new ApplicationTable(true);
		public static ModuleDependencyTable modDep=new ModuleDependencyTable(true);
		public static ProvidedResourceTable provResTable=new ProvidedResourceTable(true);
		public static SpecsTable specsTable=new SpecsTable(true);
		public static ComponentTable componentTable=new ComponentTable(true);
		public static ComponentDependencyTable compDep=new ComponentDependencyTable(true);
		public static DeploymentTable deplTable=new DeploymentTable(true);
		public static ResourcesTable resTable=new ResourcesTable(true);
		public static ResizingActionsTable raTable=new ResizingActionsTable(true);
		public static DecisionsTable desTable=new DecisionsTable(true);
		public static MetricsTable metricsTable=new MetricsTable(true);
		public static MetricValueTable mvTable=new MetricValueTable(true);

    public static void closeConnections() {
        	//close connections
		Tables.usertable.closeConnection();
		Tables.moduleTable.closeConnection();
		Tables.appTable.closeConnection();
		Tables.modDep.closeConnection();
		Tables.provResTable.closeConnection();
		Tables.componentTable.closeConnection();
		Tables.compDep.closeConnection();
		Tables.deplTable.closeConnection();
		Tables.resTable.closeConnection();
		Tables.raTable.closeConnection();
		Tables.desTable.closeConnection();
		Tables.metricsTable.closeConnection();
		Tables.mvTable.closeConnection();
    }

    public static void clearDB() {
        try {
            
            Class.forName(DRIVER);

            Connection con = DriverManager
                    .getConnection("jdbc:mysql://localhost/"
                    +DB_NAME+"?"
                    + "user="+USER);

            File file = new File("scripts/clear_celar_db.sql");

            Statement statement;
            statement = con.createStatement();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.isEmpty() )continue;
                statement.executeUpdate(line);
            }
            con.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }           catch (ClassNotFoundException ex) {
                        Logger.getLogger(Tables.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }

}
