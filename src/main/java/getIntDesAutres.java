/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import fr.insalyon.dasi.proactif.modele.Employe;
import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.service.ServiceEmploye;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 *
 * @author PAPE leo
 */
public class getIntDesAutres {
    
     public List <Intervention> execute(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        //System.out.println("session");
        Employe employe = (Employe)session.getAttribute("employe");
        
       // System.out.println("emp = " + employe);
        List<Intervention> liste = ServiceEmploye.consulterInterventionJour();
        List<Intervention> listeAu=new ArrayList<Intervention> ();
        //System.out.println("liste = " + liste);
        for (Intervention intervention : liste) {
            
            if(!intervention.getItvEmploye().getId().equals(employe.getId())){
                
                    listeAu.add(intervention);
                    System.out.println("Je rajoute des bails dans listeAu");
                
            }
        
       
        
        
        
    }       
        return listeAu;
    
}
    
}
