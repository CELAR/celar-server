package gr.ntua.cslab.descriptor;

import java.util.Map.Entry;

public class HTMLDescription extends ServiceDescriptor {

	public String toHTML(){
		String params="<table>\n";
		if(!this.parameters.isEmpty()){
			params+="<tr><th>Name</th><th>Type</th></tr>";
			for(Entry<String, String> e:this.parameters.entrySet()){
				params+="<tr><td>"+e.getKey()+"</td><td>"+e.getValue()+"</td></tr>";
			};
			params+="</table>\n";
		} else {
			params="No parameters";
		}
		String buffer="";
		buffer+="<html>\n" +
				"<head>\n" +
				"<title>"+this.name+"</title>\n" +
				"<style>\ntable,th,td\n{\nborder:1px solid black;}\ntable\n{\nborder-collapse:collapse\n}\n</style>"+
				"</head>\n" +
				"<body>\n" +
				"<table>\n" +
				"<tr>\n" +
				"<th>Service name</th>\n" +
				"<td>" +this.name+"</td>\n"+
				"</tr>\n" +
				"<tr>\n" +
				"<th>Parameters</th>\n" +
				"<td>" +params+"</td>\n"+
				"</tr>\n" +
				"<tr>\n" +
				"<th>Return value</th>\n" +
				"<td>" +this.retValue+"</td>\n"+
				"</tr>\n" +
				"<tr>\n" +
				"<th>Call Type</th>\n" +
				"<td>" +this.type+"</td>\n"+
				"</tr>\n" +
				"<tr>\n" +
				"<th>Service description</th>\n" +
				"<td>" +this.description+"</td>\n"+
				"</tr>\n" +
				"<tr>\n" +
				"<th>Example</th>\n" +
				"<td>" +this.example+"</td>\n"+
				"</tr>\n" +
				"</table>\n" +
				"</body>\n" +
				"</html>\n";
		
		return buffer;
	}

}
