/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.model.LatLng;
import fr.insalyon.dasi.proactif.dao.JpaUtil;
import fr.insalyon.dasi.proactif.modele.Animal;
import fr.insalyon.dasi.proactif.modele.Incident;
import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.modele.Livraison;
import fr.insalyon.dasi.proactif.service.GeoTest;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

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
        HttpSession session = request.getSession(true);
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
           String param=request.getParameter("action");
           switch (param){
           
               case "creerClient":
                    InscriptionAction a= new InscriptionAction();
                    boolean inscriptionValide=a.execute(request);
                    printInscription(out, inscriptionValide);
                    break;
                
               case "connexion":
                   ConnexionAction ca=new ConnexionAction();
                   int connexionValide=ca.execute(request);
                   printConnexion(out,connexionValide);
                   
                   break;
                   
               case "getProchaineInt":
                   //System.out.println("------------------------");
                           
                       getProchaineIntervention gPI= new getProchaineIntervention();
                       Intervention i=gPI.execute(request);
                       printProchaineIntervention(out,i);
                       break;
                       
                   
               case "setMap":
                     getToutesLesInt gTI= new getToutesLesInt();
                     List <Intervention> liste=gTI.execute(request);
                     //System.out.println("La list de gTI: "+liste);
                     
                     printSetMap(out,liste);
                     
                     break;
                     
               case "getEnCours":
                    getIntEnCours gEC= new getIntEnCours();
                    List <Intervention> listeEC=gEC.execute(request);
                    printSetMap(out,listeEC);
                    break;
                    
               case "getSoiMeme":
                   getSesPropresInts gSPI= new getSesPropresInts();
                   List <Intervention> listeSM=gSPI.execute(request);
                   //System.out.println("Liste soi-même: "+listeSM);
                   printSetMap(out,listeSM);
                   break;
               case "getAutres":
                   getIntDesAutres gIDA=new getIntDesAutres();
                   List <Intervention> listeAu=gIDA.execute(request);
                   printSetMap(out,listeAu);
                   break;
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
    
    public static void printConnexion (PrintWriter out, int connexionValide){
         Gson gson= new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject jsonCo= new JsonObject();
        
        jsonCo.addProperty("type", connexionValide);
        JsonObject container= new JsonObject();
        container.add("connexion",jsonCo);
        out.println(gson.toJson(container));          

        
    }
    
    public static void printProchaineIntervention (PrintWriter out, Intervention intervention){
         Gson gson= new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject jsonCo= new JsonObject();
        
        Date today= intervention.getDateDebut();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        
        String date=df.format(today);
        jsonCo.addProperty("date",date);
        jsonCo.addProperty("id", intervention.getItvClient().getId());
        jsonCo.addProperty("nom", intervention.getItvClient().getNom());
        jsonCo.addProperty("prenom", intervention.getItvClient().getPrenom());
        jsonCo.addProperty("adresse",intervention.getItvClient().getAdresse());
        LatLng origine= GeoTest.getLatLng(intervention.getItvEmploye().getAdresse());
        LatLng destination=GeoTest.getLatLng(intervention.getItvClient().getAdresse());;
         if(intervention instanceof Animal){
                  jsonCo.addProperty("animal",((Animal) intervention).getTypeAnimal());
                  jsonCo.addProperty("type","animal");
                  
              }else if(intervention instanceof Incident){
                  jsonCo.addProperty("type","incident");
                 
              }else{
                 jsonCo.addProperty("objet",((Livraison) intervention).getObjet());
                 jsonCo.addProperty("entreprise",((Livraison) intervention).getEntreprise());
                 jsonCo.addProperty("type","livraison");
                 

              }
         
         double distance= GeoTest.getFlightDistanceInKm(origine, destination);
         jsonCo.addProperty("distance",(int)distance);
        JsonObject container= new JsonObject();
        container.add("container",jsonCo);
        out.println(gson.toJson(container));          

        
    }
    
    public static void printSetMap (PrintWriter out, List <Intervention> liste){
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonListe = new JsonArray();
        LatLng origine;
        LatLng destination;
        /*LatLng test=GeoTest.getLatLng(liste.get(0).getItvClient().getAdresse());
        String t=test.toString();
        System.out.println("Je vais afficher LatLng");
        System.out.println("LatLng: "+t);*/
            for(Intervention intervention : liste){
               //origine= GeoTest.getLatLng(intervention.getItvEmploye().getAdresse()); 
               destination=GeoTest.getLatLng(intervention.getItvClient().getAdresse());
               
               //String o=origine.toString();
               String d=destination.toString();
               String Lat=d.substring(0, d.indexOf(','));
               String Lng=d.substring(d.indexOf(',')+1, d.length());
               //System.out.println("Lat : "+Lat);
               //System.out.println("Lng :"+Lng);
               
               JsonObject jsonIntervention  = new JsonObject();
               jsonIntervention.addProperty("lat",Lat);
               jsonIntervention.addProperty("lng",Lng);
               Date today= intervention.getDateDebut();
               DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
               Date fin=intervention.getDateFin();
               String dateDeb=df.format(today);
               String dateFin=df.format(fin);
               jsonIntervention.addProperty("dateDeb",dateDeb);
               jsonIntervention.addProperty("dateFin",dateFin);
               //jsonIntervention.addProperty("idE", intervention.ge);
               jsonIntervention.addProperty("idC", intervention.getItvClient().getId());
               jsonIntervention.addProperty("nom", intervention.getItvClient().getNom());
               jsonIntervention.addProperty("prenom", intervention.getItvClient().getPrenom());
               jsonIntervention.addProperty("adresse",intervention.getItvClient().getAdresse());
               jsonIntervention.addProperty("employe",intervention.getItvEmploye().getNom()+" "+intervention.getItvEmploye().getPrenom()+" "+intervention.getItvEmploye().getId());
                
               if(intervention instanceof Animal){
                  jsonIntervention.addProperty("animal",((Animal) intervention).getTypeAnimal());
                  jsonIntervention.addProperty("type","animal");
                  
              }else if(intervention instanceof Incident){
                  jsonIntervention.addProperty("type","incident");
                 
              }else{
                 jsonIntervention.addProperty("objet",((Livraison) intervention).getObjet());
                 jsonIntervention.addProperty("entreprise",((Livraison) intervention).getEntreprise());
                 jsonIntervention.addProperty("type","livraison");
                 

              }
                
               jsonListe.add(jsonIntervention);
               
            }
        //System.out.println(jsonListe);    
        JsonObject container = new JsonObject();
        container.add("interventions", jsonListe);
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
