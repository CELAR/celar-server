/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.testing;

import gr.ntua.cslab.database.ApplicationTable;
import gr.ntua.cslab.database.ComponentDependencyTable;
import gr.ntua.cslab.database.ComponentTable;
import gr.ntua.cslab.database.DecisionsTable;
import gr.ntua.cslab.database.DeploymentTable;
import gr.ntua.cslab.database.MetricValueTable;
import gr.ntua.cslab.database.MetricsTable;
import gr.ntua.cslab.database.ModuleDependencyTable;
import gr.ntua.cslab.database.ModuleTable;
import gr.ntua.cslab.database.ProvidedResourceTable;
import gr.ntua.cslab.database.ResizingActionsTable;
import gr.ntua.cslab.database.ResourcesTable;
import gr.ntua.cslab.database.SpecsTable;
import gr.ntua.cslab.database.UserTable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author cmantas
 */
public class TablesTester {

	public static void main(String args[]){
			
		//init table objects
		UserTable usertable = new UserTable(true);
		ModuleTable moduleTable=new ModuleTable(true);
		ApplicationTable appTable=new ApplicationTable(true);
		ModuleDependencyTable modDep=new ModuleDependencyTable(true);
		ProvidedResourceTable provResTable=new ProvidedResourceTable(true);
		SpecsTable specsTable=new SpecsTable(true);
		ComponentTable compTable=new ComponentTable(true);
		ComponentDependencyTable compDep=new ComponentDependencyTable(true);
		DeploymentTable deplTable=new DeploymentTable(true);
		ResourcesTable resTable=new ResourcesTable(true);
		ResizingActionsTable raTable=new ResizingActionsTable(true);
		DecisionsTable desTable=new DecisionsTable(true);
		MetricsTable metricsTable=new MetricsTable(true);
		MetricValueTable mvTable=new MetricValueTable(true);
		

		// insert data in the tables
		System.out.println("===================  INSERT  =====================");
		//create a user
		int userId=usertable.insertUser("christaras" );
		System.out.print("Inserted chris. exists: ");
		System.out.println(usertable.exists(userId));
		System.out.println("chris is="+userId);
		//create an application	
		int appId=appTable.insertApplication("chris's app", userId);
		System.out.println("inserted app with id:"+appId);
		System.out.println("app with id "+appId+" exists: "+appTable.exists(appId));
		//create a module
		int moduleId=moduleTable.insertModule(appId,"chris's apache" );
		System.out.println("inserted module chris's apache"+moduleId);
		System.out.println("module with id "+appId+" exists: "+moduleTable.exists(moduleId));
		//create a second module
		int module2Id=moduleTable.insertModule(appId,"chris's hbase" );
		System.out.println("inserted module chris's hbase"+moduleId);
		//create a module dependency
		modDep.insertModuleDependency(moduleId, module2Id, "needs");
		//lookup the dependency
		List moduleDependencies=modDep.findDependencies(moduleId);
		System.out.println("chris's apache dependencies: "+moduleDependencies);
		//create a provided resource
		int providedResourceId=provResTable.insertProvidedResource("vm", "dummy_vm");
		System.out.println("provided resource with id "+providedResourceId
			+" inserted exists: "+provResTable.exists(providedResourceId));
		int specId=specsTable.insertSpecs(providedResourceId, "tiny vm");
		System.out.println("spec with id="+specId+" inserted. exists: "
			+specsTable.exists(specId));
		//create a component for an existing module with an existing provider resource
		int compId=compTable.insertComponent("seed node", module2Id, providedResourceId);
		System.out.println("Component with id="+compId
			+" inserted. exists="+compTable.exists(compId));
		//create a component dependency and look it up
		compDep.insertModuleDependency(compId, compId, "self");
		List componentDependencies=compDep.findDependencies(compId);
		System.out.println("chris's component dependencies: "+componentDependencies);
		//create a deployment and check it exists
		int deplId=deplTable.insertDeployment(
			appId, new Timestamp(System.currentTimeMillis()), null);
		System.out.println("deployment with id="+deplId
			+" inserted. exists="+deplTable.exists(deplId));
		//create a resource and check it exists
		int resourceId=resTable.insertResource(deplId, compId, 
			providedResourceId, new Timestamp(System.currentTimeMillis()), null);
		System.out.println("resource with id "+resourceId+" inserted"
			+" exists: "+resTable.exists(resourceId));
		//create a resizing action acn check it exists
		int raId=raTable.insertResizingAction(moduleId, compId, "add_vm");
		System.out.println("resizing action with id "+raId+" inserted"
			+" exists: "+raTable.exists(raId));
		//create a decision 
		Timestamp ts=new Timestamp(System.currentTimeMillis());
		System.out.println("decision created="+desTable.insertDecision(ts, raId, 2));
		//create a metric for a previously created component
		int metricId=metricsTable.insertMetric(compId, ts);
		System.out.println("metric with id "+metricId+" inserted"
			+" exists: "+metricsTable.exists(metricId));
		//create a metric value for a previously created metric value, component, resource
		int mvId=mvTable.insertMetricValue(metricId, resourceId, 
			new Timestamp(System.currentTimeMillis()));
		System.out.println("metric value with id "+mvId+" inserted"
			+" exists: "+mvTable.exists(mvId));
		
		
		System.out.println(appTable.getById(appId));
	

		
		//delete the things you inserted
		System.out.println("===================  DELETE  =====================");
		mvTable.delete(mvId);
		System.out.println("Deleted metric value with id="+mvId+" exists="
			+mvTable.exists(mvId));
		metricsTable.delete(metricId);
		System.out.println("Deleted metric with id="+metricId+" exists="
			+metricsTable.exists(metricId));
		System.out.println("Deleted decision. rows affected: "+desTable.deleteDecision(ts));
		raTable.delete(raId);
		System.out.println("deleted resizing action with id="+raId
			+" exists="+raTable.exists(raId));
		resTable.delete(resourceId);
		System.out.println("deleted resource with id="+resourceId
			+" exists="+resTable.exists(resourceId));
		deplTable.delete(deplId);
		System.out.println("deleted deployment with id "
			+deplId+" exists:"+deplTable.exists(deplId));
		compDep.deleteDependency(compId, compId);
		componentDependencies=compDep.findDependencies(compId);
		System.out.println("chris's component dependencies after delete: "
			+componentDependencies);
		compTable.delete(compId);
		System.out.println("deleted component. exists: "+compTable.exists(compId));
		specsTable.delete(specId);
		System.out.println("deleted sppec. exists:"+specsTable.exists(specId));
		provResTable.delete(providedResourceId);
		System.out.println("deleted prov resouce. exists: "+provResTable.exists(providedResourceId));	
		modDep.deleteDependency(moduleId, module2Id);
		moduleDependencies=modDep.findDependencies(moduleId);
		System.out.println("chris's apache dependencies after delete: "+moduleDependencies);
		moduleTable.delete(moduleId);
		System.out.println("deleted chris's hbase. exists: "+moduleTable.exists(moduleId));
		moduleTable.delete(module2Id);
		System.out.println("deleted chris's apache. exists: "+moduleTable.exists(module2Id));
		appTable.delete(appId);
		System.out.println("deleted chris's app. exists: "+appTable.exists(appId));
		usertable.delete(userId);
		System.out.println("deleted chris. exists:"+usertable.exists(userId));
		
		//close connections
		usertable.closeConnection();
		moduleTable.closeConnection();
		appTable.closeConnection();
		modDep.closeConnection();
		provResTable.closeConnection();
		compTable.closeConnection();
		compDep.closeConnection();
		deplTable.closeConnection();
		resTable.closeConnection();
		raTable.closeConnection();
		desTable.closeConnection();
		metricsTable.closeConnection();
		mvTable.closeConnection();
		
	}
	
}
