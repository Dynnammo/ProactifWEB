
import fr.insalyon.dasi.proactif.modele.Animal;
import fr.insalyon.dasi.proactif.modele.Client;
import fr.insalyon.dasi.proactif.modele.Incident;
import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.modele.Livraison;
import fr.insalyon.dasi.proactif.service.ServiceClient;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author baptiste
 */
class interventionAction {
    boolean execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Client c = (Client) session.getAttribute("client");
        
        String typeIntervention = request.getParameter("typeIntervention");
        String typeAnimal= request.getParameter("typeAnimal");
        String nomEntreprise = request.getParameter("nomEntreprise");
        String typeObjet = request.getParameter("typeObjet");
        String description = request.getParameter("description");
        
        Intervention i = null;
        switch (typeIntervention){
            case "incident":
                i = new Incident(null, c, new Date(), null, "Aucun commentaire", 0, description);  
                break;
            case "animal":
                i = new Animal(null, c, new Date(), null, "Aucun commentaire", 0, description, typeAnimal);
                break;
            case "livraison":
                i = new Livraison(null, c, new Date(), null, "Aucun commentaire", 0, description, typeObjet, nomEntreprise);
        }
        System.out.println("Intervention " + i.toString());
        
        boolean interventionAcceptee = ServiceClient.creerIntervention(i);
        System.out.println("etat de l'intervention"+interventionAcceptee);
        return interventionAcceptee;
    }
    
}
