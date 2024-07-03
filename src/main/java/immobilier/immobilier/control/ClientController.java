package immobilier.immobilier.control;

import java.sql.Connection;
import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import immobilier.immobilier.model.Conn;
import immobilier.immobilier.model.Login;
import immobilier.immobilier.model.LoyerClient;
import immobilier.immobilier.model.Utilisateur;
import immobilier.immobilier.tools.Tools;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/client")
public class ClientController {


    @GetMapping("/loyer")
    public String loyer(HttpServletRequest request, Model model, String date1, String date2) {
        try {
            Connection con = Conn.con();
            Login log = (Login) request.getSession().getAttribute("user");
            String username = log.getUsername();
            String idUser = new Utilisateur().getIdUtilisateurByUsername(username).getId();
            LoyerClient l = new LoyerClient();
            LinkedList<LoyerClient> loy = l.getLoyerClientDouble1erMoisEntre2Dates(idUser, date1, date2, con);
            LinkedList<LoyerClient> ap = l.getLoyerClientDouble1erMoisAPayerEntre2Dates(idUser, date1, date2, con);
            LinkedList<LoyerClient> p = l.getLoyerClientDouble1erMoisPayeEntre2Dates(idUser, date1, date2, con);
            // LinkedList<LoyerClient> loy = l.getLoyerClientEntre2Dates(idUser, date1, date2, con);
            // LinkedList<LoyerClient> ap = l.getLoyerClientAPayerEntre2Dates(idUser, date1, date2, con);
            // LinkedList<LoyerClient> p = l.getLoyerClientPayeEntre2Dates(idUser, date1, date2, con);
            Double apayer = 0.;
            Double paye = 0.;

            for (LoyerClient apaClient : ap) {
                apayer = apayer + apaClient.getMontant();
            }
            for (LoyerClient paClient : p) {
                paye = paye + paClient.getMontant();
            }

            model.addAttribute("loy",loy);
            model.addAttribute("apayer", Tools.formatThousand(apayer));
            model.addAttribute("paye", Tools.formatThousand(paye));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("page", "dashboardClient");
        return "index";
    }

    @GetMapping("/")
    public String actB(Model model) {
        model.addAttribute("page", "voirLoyer");
        return "index";
    }
}