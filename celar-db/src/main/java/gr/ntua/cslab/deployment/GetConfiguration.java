/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.deployment;

import gr.ntua.cslab.database.entities.Application;
import gr.ntua.cslab.testing.EntitiesTester;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static gr.ntua.cslab.database.entities.JSONTools.exportApplicationConfiguration;
import static gr.ntua.cslab.database.entities.JSONTools.findDeploymentApp;
import java.sql.Timestamp;

/**
 *
 * @author cmantas
 */
@WebServlet(name = "GetConfiguration", urlPatterns = {"/deployment/getConfiguration"})
public class GetConfiguration extends HttpServlet {
    
        static int indent=3;
      

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("text;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {

			String deploymentId=request.getParameter("DeploymentId");
			String timestamp=request.getParameter("timestamp");
                        
                        //sanity check input parameters
                        int depId=deploymentId==null?1:Integer.parseInt(deploymentId);
                        timestamp=timestamp==null?"now":timestamp;
                        
                        Timestamp ts=timestamp=="now"?new Timestamp(System.currentTimeMillis()):Timestamp.valueOf(timestamp);
                        
                        Application parent=findDeploymentApp(depId);
                        String result=exportApplicationConfiguration(parent, ts).toString(3);
                        //write the result
                        out.print(result);
			
			
		} finally {			
			out.close();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
