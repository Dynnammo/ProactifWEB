
import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.service.ServiceEmploye;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class cloturerIntervention {
    public boolean execute (HttpServletRequest request, Intervention intervention) throws ParseException{
        System.out.println("je passe");
        String probleme=request.getParameter("realisation");
      
      String commentaire=request.getParameter("commentaire");
      System.out.println("commentaire: "+commentaire);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date date=formatter.parse(request.getParameter("date")); 
      System.out.println("probleme: "+probleme);
      if("Sans probleme".equals(probleme)){
          ServiceEmploye.cloturerIntervention(intervention, commentaire, false, date);
          return true;
      }else if("Avec probleme".equals(probleme)){
          ServiceEmploye.cloturerIntervention(intervention, commentaire, true, date);
          return true;
      }else{
          return false;
      }
    }
}