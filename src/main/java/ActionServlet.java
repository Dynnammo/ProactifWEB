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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            throws ServletException, IOException, ParseException {
        
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        try (PrintWriter out = response.getWriter()) {
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
                    System.out.println("dans l'action"+listeEC);
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
               case "consulterInterventions":
                   getIntClient gIC = new getIntClient();
                   List<Intervention> listeIntervention = gIC.execute(request);
                   System.out.println("listeIntervention dans ActionServlet principal "+ listeIntervention);
                   printConsulterIntervention(out,listeIntervention);
                   break;
               case "demanderIntervention":
                   interventionAction iA = new interventionAction();
                   boolean interventionAcceptee= iA.execute(request);
                   printDemanderIntervention(out,interventionAcceptee);
                   break;
                case "deconnexion":
                    session = request.getSession(false);
                               case "cloturerItv":
                   getProchaineIntervention gPI2= new getProchaineIntervention();
                   Intervention intervention=gPI2.execute(request);
                    System.out.println("je passe dans l'action servlet");
                   cloturerIntervention cla= new cloturerIntervention();
                   boolean cloturationValide= cla.execute(request, intervention);
                   System.out.println(cloturationValide);
                   printCloturerIntervention(out,cloturationValide);
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
        System.out.println("Je suis dans print");
            for(Intervention intervention : liste){
               destination=GeoTest.getLatLng(intervention.getItvClient().getAdresse());
               
               String d=destination.toString();
               String Lat=d.substring(0, d.indexOf(','));
               String Lng=d.substring(d.indexOf(',')+1, d.length());
                System.out.println("creation du jsonIntervention");
               JsonObject jsonIntervention  = new JsonObject();
               jsonIntervention.addProperty("lat",Lat);
               jsonIntervention.addProperty("lng",Lng);
                System.out.println("remplissage gps");
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date today= intervention.getDateDebut();
                String dateDeb=df.format(today);
                jsonIntervention.addProperty("dateDeb",dateDeb);
               try{
                    Date fin=intervention.getDateFin();
                    String dateFin=df.format(fin);
                    jsonIntervention.addProperty("dateFin",dateFin);
               } catch(Exception e) {
                   System.err.println("Echec du parsing de date fin");
               }


                System.out.println("remplissage date");
               jsonIntervention.addProperty("idE", intervention.getItvEmploye().getId());
               jsonIntervention.addProperty("idC", intervention.getItvClient().getId());
               jsonIntervention.addProperty("nom", intervention.getItvClient().getNom());
               jsonIntervention.addProperty("prenom", intervention.getItvClient().getPrenom());
               jsonIntervention.addProperty("adresse",intervention.getItvClient().getAdresse());
               jsonIntervention.addProperty("employe",intervention.getItvEmploye().getNom()+" "+intervention.getItvEmploye().getPrenom()+" "+intervention.getItvEmploye().getId());
                System.out.println("remplissage infos");
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
    
    public static void printConsulterIntervention(PrintWriter out, List <Intervention> liste){
        /*
        On renvoie un objet de la forme {"container": [{...},{...},...]} où chaque
        {...} est un JsonObject de la forme: {"date":...,"type":...,"typeAnimal":...,...}
        SACHANT que TOUTES LES INTERVENTIONS ONT LES MÊMES ATTRIBUTS QUI SONT VIDES s'ils ne 
        servent pas ie. si c'est un incident, les attr. animal, nomEntreprise et type Objet sont des
        strings vides
        
        Les statuts sont à modifier
        */
        //Intervention i = null;
        //i = new Incident(null, null, new Date(), null, "Faux incident, je suis resté chez moi", 0, "Description 1");
        //liste.add(i);
        System.out.println("listeIntervention dans print" + liste);
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        
        JsonArray jsonInterventions= new JsonArray();
        for (Intervention intervention : liste) {
            JsonObject jsonIntervention = new JsonObject();
            
            jsonIntervention.addProperty("date",intervention.getDateDebut().toString());
            
            if (intervention instanceof Animal){
                jsonIntervention.addProperty("type","Animal");
                jsonIntervention.addProperty("typeAnimal",((Animal) intervention).getTypeAnimal());
            }
            else if (intervention instanceof Livraison){
                jsonIntervention.addProperty("type","Livraison");
                jsonIntervention.addProperty("entreprise",((Livraison) intervention).getEntreprise());
                jsonIntervention.addProperty("typeObjet",((Livraison) intervention).getObjet());
            }
            else{
                jsonIntervention.addProperty("type", "Incident");
            }
            
            
            Date d = intervention.getDateDebut();
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int year = cal.get(Calendar.YEAR);
            jsonIntervention.addProperty("annee", year);
            
            
            int statut = intervention.getEtat();
            //A COMPLETER
            switch(statut){
                case (0):
                    jsonIntervention.addProperty("statut", "En cours");
                    break;
                case (1):
                    jsonIntervention.addProperty("statut", "Cloturée");
                    break;
                case (2):
                    jsonIntervention.addProperty("statut", "Problème");
                    break;
            }
            jsonIntervention.addProperty("commentaire", intervention.getCommentaire());
            jsonIntervention.addProperty("description", intervention.getDescription());
            
            jsonInterventions.add(jsonIntervention);
            System.out.println("Inntervention jsonnée "+jsonIntervention);
        }
        JsonObject container= new JsonObject();
        container.add("container",jsonInterventions);
        out.println(gson.toJson(container));
    }
    
    private void printDemanderIntervention(PrintWriter out, boolean interventionAcceptee) {
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject jsonInterventionAcceptee= new JsonObject();
        jsonInterventionAcceptee.addProperty("interventionAcceptee", interventionAcceptee);
        
        JsonObject container= new JsonObject();
        container.add("container",jsonInterventionAcceptee);
        out.println(gson.toJson(container));
    }
    
    public static void printCloturerIntervention(PrintWriter out, boolean cloturationValide){
         Gson gson= new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject jsonCo= new JsonObject();
        
        jsonCo.addProperty("type", cloturationValide);
        JsonObject container= new JsonObject();
        container.add("cloturation",jsonCo);
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
