
import fr.insalyon.dasi.proactif.dao.InterventionDAO;
import fr.insalyon.dasi.proactif.modele.Animal;
import fr.insalyon.dasi.proactif.modele.Employe;
import fr.insalyon.dasi.proactif.modele.Incident;
import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.service.ServiceEmploye;
import java.util.List;
import java.util.Objects;
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

public class getProchaineIntervention {
    public Intervention execute(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        Employe employe = (Employe)session.getAttribute("employe");
        
        System.out.println("emp = " + employe);
        List<Intervention> liste = ServiceEmploye.consulterInterventionJour();
        //System.out.println("liste = " + liste);
        for (Intervention intervention : liste) {
            
  
            if(Objects.equals(intervention.getItvEmploye().getId(), employe.getId())){
                System.out.println("Je passe dans le premier if, je reconnais l'employe");
                if(intervention.getEtat()==0){
                    System.out.println("Je renvoie la prochaine itv");
                    return intervention;
                }
            }
    }       
        return null;
    
}
}  


