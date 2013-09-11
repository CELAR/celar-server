/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author pro
 */
public class Tables {
    
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
    
}
