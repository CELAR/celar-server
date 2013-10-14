package gr.ntua.cslab.services.application;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Describe
 */
@WebServlet("/Describe")
public class Describe extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTMLDescription desc;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Describe() {
        super();
        desc = new HTMLDescription();
        desc.setName("Describe Application");
        desc.setRetValue("true/false");
        desc.setDescription("This call is used to describe an application. Specifically with this call you can:"
        		+ "<ul>"
        		+ "<li>Load a TOSCA file using description parameter (see params for more)</li>"
        		+ "</ul>");
        desc.addParameter("description", "TOSCA File containing the Application Structure");
        desc.setType("POST");
        desc.setExample("http://83.212.124.172:8080/celar-server-api/application/describe/<br/>"
        		+ "description=foo --POST argument--<br/>"
        		+ "foo here the file content to be uploaded.<br/>"
        		+ "<span style='color:red; font-weight:bold;'>WARNING: please URLEncode the file content BEFORE sending it</span>");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("info")!=null){
			response.getOutputStream().print(this.desc.toHTML());
		} else {
			response.getOutputStream().print("Please use post to send me a TOSCA.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		ServletOutputStream out = response.getOutputStream();
//		while(in.ready()){
//			out.print(in.readLine());
//		}
//		out.println(request.getQueryString());
		if(request.getParameter("description")!=null){
			PrintStream file = new PrintStream(new FileOutputStream("/tmp/tosca.xml"), true);
			file.println(request.getParameter("description"));
			file.close();
			response.getOutputStream().println("{\"status\" : \"saved\"}");
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
