package gr.ntua.cslab.services.deployment;

import gr.ntua.cslab.bash.Command;
import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Deploy
 */
@WebServlet("/Deploy")
public class Deploy extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Deploy() {
        super();
        this.desc = new HTMLDescription();
        this.desc.setName("Deploy application");
        this.desc.setRetValue("true/false");
        this.desc.setType("GET");
        this.desc.setDescription("Call used to deploy an application.<br/>"
        		+ "This call will physically run into CELAR Server. The expected arguments are:"
        		+ "<ul>"
        		+ "<del>"
        		+ "<li>Deployment Configuration</li>"
        		+ "<li>Application id (identifies the application)</li></del>"
        		+ "<li>cluster (value cassandra)</li>"
        		+ "</ul>");
        this.desc.addParameter("Application structure", "TOSCA (xml)");
        this.desc.setExample("How can you deploy a cassandra cluster:<br/>"
        		+ "Call this uri from your client with the GET argument ?cluster=cassandra.<br/>"
        		+ "After that a new deployment will be initiated on LAL with 1 ycsb client, 1 seednode and 2 cassandra nodes.<br/>"
        		+ "This call returns a JSON message responding to the STDOUT and STDERR of the executed slipstream command. (it will change eventually)<br/>"
        		+ "http://83.212.117.112/celar-orchestrator/deployment/deploy/?cluster=cassandra");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("info")!=null){
			response.getOutputStream().println(this.desc.toHTML());
			return;
		} 
		if(request.getParameter("cluster").equals("cassandra")){
			Command com = new Command("ss-execute -u sixsq -p siXsQsiXsQ --endpoint https://83.212.124.172 Cassandra/cassandra");
			com.waitFor();
			response.getOutputStream().println("{ \"deploy\": "+com.getOutputsAsJSONString()+"}");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
