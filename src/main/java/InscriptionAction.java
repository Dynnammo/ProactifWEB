
import fr.insalyon.dasi.proactif.modele.Client;
import fr.insalyon.dasi.proactif.service.ServiceClient;
import javax.servlet.http.HttpServletRequest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PAPE leo
 */

public class InscriptionAction {
    
    public boolean execute(HttpServletRequest request){
        
        String nom;
        nom = (String) request.getParameter("nom");
        String prenom=request.getParameter("prenom");
        String genre=request.getParameter("genre");
        String mail=request.getParameter("mail");
        String tel=request.getParameter("tel");
        String date= request.getParameter("date");
        System.out.println(date);
        System.err.println(genre);
        String ville= request.getParameter("ville");
        String cp=request.getParameter("cp");
        String ad=request.getParameter("ad");
        
        String adresse= ad+", "+ville;
        boolean insc = false;
        Client c= new Client(genre, nom, prenom, date, adresse, "0", tel, mail);
        if (ServiceClient.trouverClientParMail(mail)!=null){
            return insc;
        }
        else if (c.getDateNaissance()==null){
            return insc;
        }
        else {
            insc=ServiceClient.creerClient(c);
            //System.out.println("creation client");
        }
        return insc;
    }
}
