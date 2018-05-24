
import fr.insalyon.dasi.proactif.modele.Client;
import fr.insalyon.dasi.proactif.modele.Intervention;
import fr.insalyon.dasi.proactif.service.ServiceClient;
import java.util.List;
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
class getIntClient {

    List<Intervention> execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Client c = (Client) session.getAttribute("client");
        List<Intervention> lI = ServiceClient.consulterHistorique(c);
        return lI;
    }
}
