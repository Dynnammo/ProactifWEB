/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.insalyon.dasi.proactif.dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author PAPE leo
 */
@WebServlet(urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           /* out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ActionServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ActionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");*/
               System.out.println("test");
           String param=request.getParameter("action");
           switch (param){
           
               case "creerClient":
                       System.out.println("test");
                    InscriptionAction a= new InscriptionAction();
                    boolean inscriptionValide=a.execute(request);
                    printInscription(out, inscriptionValide);
                    break;
                    
              /* case "connecter<client":
                   Conne
                   break;*/
           }
                   
                  
               
            
            
           
        }
    }

    public static void printInscription(PrintWriter out, boolean inscriptionValide){
        
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject jsonBool= new JsonObject();
        
        if(inscriptionValide==true){
            jsonBool.addProperty("validite", true);
            
        }else{
            jsonBool.addProperty("validite",false);
        }
        
        JsonObject container= new JsonObject();
        container.add("inscription",jsonBool);
        out.println(gson.toJson(container));
        
        
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
    @Override
    public void init(){
        
        JpaUtil.init();
    }
    
    /**
     *
     */
    @Override
    public void destroy(){
        JpaUtil.destroy();
    }

}