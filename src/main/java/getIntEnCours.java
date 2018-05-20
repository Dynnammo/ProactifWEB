
import fr.insalyon.dasi.proactif.modele.Employe;
import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.service.ServiceEmploye;
import java.util.ArrayList;
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
public class getIntEnCours {
    
    public List <Intervention> execute(HttpServletRequest request){
        
        
       // System.out.println("emp = " + employe);
        List<Intervention> liste = ServiceEmploye.consulterInterventionJour();
        
        List<Intervention>listeEC=new ArrayList<Intervention> ();
        //System.out.println("liste = " + liste);
        for (Intervention intervention : liste) {
            
                
                int etat=(int)intervention.getEtat();
                System.out.println("L'intervention a pour état :"+etat);
                if(etat==0){
                    System.out.println("Je rajoute des bails dans listeEC");
                    listeEC.add(intervention);
                    
                   
                }
        
        }       
        return listeEC;
}
}
