/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ntua.cslab.deployment;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static gr.ntua.cslab.database.entities.JSONTools.parseJson;
import static gr.ntua.cslab.database.entities.JSONTools.parseUsers;
import static gr.ntua.cslab.database.entities.JSONTools.parseProvidedResources;
import static gr.ntua.cslab.database.entities.JSONTools.parseApplicationConfiguration;
import org.json.JSONObject;

/**
 *
 * @author pro
 */
public class PutConfiguration extends HttpServlet {

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
        response.setContentType("text");
        PrintWriter out = response.getWriter();
        try {
            

            //parse and store users configuration if any
            JSONObject users=parseJson(request.getParameter("userConfiguration"), out);
            if(users!=null){
                parseUsers(users,true);
                out.println("users parsed");
            }else out.println("no users parsed (no input)");
            
            //parse and store provided resources if any
            JSONObject resourcesConf=parseJson(request.getParameter("resourcesConfiguration"), out);
            if(resourcesConf!=null){
                parseProvidedResources(resourcesConf,true);
                out.println("provided resources parsed");
            }else out.println("no provided resources parsed (no input)");
            
            //parse and store application configuration if any
            String inputConf=request.getParameter("applicationConfiguration");
            if(inputConf!=null&&!inputConf.trim().equals("")){
                parseApplicationConfiguration(inputConf,true);
                out.println("application configuration parsed");
            }else out.println("application configuration not parsed (no input)");
            
           
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
