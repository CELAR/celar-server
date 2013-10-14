package gr.ntua.cslab.services.application;

import gr.ntua.cslab.descriptor.HTMLDescription;

import java.io.IOException;

import javax.servlet.ServletException;
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
        desc.addParameter("", "");
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
		// TODO Auto-generated method stub
	}

}
