package gr.ntua.cslab.descriptor;

import java.util.HashMap;

/**
 * This class provides an interface used to provide an application description.
 * This class can be extended in order to provide the details in a specific form.
 * @author giannis
 *
 */
public class ServiceDescriptor {

	protected String name;
	protected HashMap<String, String> parameters;
	protected String type; 		//GET, POST
	protected String description;
	protected String example;
	protected String retValue;
	
	public String getRetValue() {
		return retValue;
	}
	public void setRetValue(String retValue) {
		this.retValue = retValue;
	}
	public ServiceDescriptor() {
		parameters = new HashMap<String, String>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	
	public void addParameter(String name, String type){
		this.parameters.put(name, type);
	}
}
