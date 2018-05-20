

import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.service.ServiceEmploye;
import java.util.List;
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
public class getToutesLesInt {
    public List<Intervention> execute(HttpServletRequest request){
        
       
       
        List<Intervention> liste = ServiceEmploye.consulterInterventionJour();
        //System.out.println("Je récupère la liste");
        return liste;
    }
}
