/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deployment;

import database.Tables;
import database.entities.Application;
import database.entities.Component;
import database.entities.Module;
import database.entities.ProvidedResource;
import database.entities.Tester;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author cmantas
 */
@WebServlet(name = "GetConfiguration", urlPatterns = {"/deployment/getConfiguration"})
public class GetConfiguration extends HttpServlet {
    
    
        static String crawlApplicationModules(int AppId) {
        JSONObject result=new JSONObject();
        Application app=new Application(AppId);
        List<Module> modules = Tables.moduleTable.getAppModules(AppId);
        System.out.println("modules for facebook:" + modules);
        
        JSONArray modulesListJson = new JSONArray();
        //iterate modules and add to json list
        
        for (Module m : modules) {
            List<Component> components = Tables.componentTable.getModuleComponents(m.getId());
            
            JSONArray componentsListJson = new JSONArray();
            //iterate components and add to json list
            for (Component c : components) {
                componentsListJson.put(c.toJSONObject());
            }
            JSONObject moduleJson=m.toJSONObject();
            moduleJson.put("components", componentsListJson);
            modulesListJson.put(moduleJson);
        }
        
        JSONObject applicationJson=app.toJSONObject();
        applicationJson.put("modules", modulesListJson);
        
        result.put("application", applicationJson);
        return result.toString(2);
    }

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
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {

			String DeploymentId=request.getParameter("DeploymentId");
			String timestamp=request.getParameter("timestamp");
                        //ignore theese for now
                        Tester.createStructure();
                        String result=crawlApplicationModules(Tester.facebook.getId());

			
			/* TODO output your page here. You may use following sample code. */
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet GetConfiguration</title>");			
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Servlet GetConfiguration </h1>");
			out.println("deployment conf for deploymentID: "+DeploymentId+", at timestamp: "+timestamp);
                        out.println("<textarea>"+result+"</textarea>");
			out.println("</body>");
			out.println("</html>");
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
