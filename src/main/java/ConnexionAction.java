
import fr.insalyon.dasi.proactif.modele.Client;
import fr.insalyon.dasi.proactif.modele.Employe;
import fr.insalyon.dasi.proactif.service.ServiceClient;
import fr.insalyon.dasi.proactif.service.ServiceEmploye;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PAPE leo
 */
public class ConnexionAction {
    
    //renvoie 0: echec co, 1: client, 2 employé
    public int execute (HttpServletRequest request){
         String mail=request.getParameter("mail");
         String mdp=request.getParameter("mdp");
         HttpSession session = request.getSession();
         Long id=Long.valueOf(mdp);
          
         Client c=ServiceClient.connexionClient(mail, id);
         if(c!=null){
             session.setAttribute("client",c);
             return 1;
         }else{
          Employe e=ServiceEmploye.connexionEmploye(mail, id); // j'ai passé la méthode en publique elle était privée avant
          
          if(e!=null){
              System.out.println("Employé connecté: "+e);
            session.setAttribute("employe",e);
            return 2;
         }
        }
         return 0;
    }
}
