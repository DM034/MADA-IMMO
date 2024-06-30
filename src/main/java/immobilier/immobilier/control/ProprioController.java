package immobilier.immobilier.control;

import java.sql.Connection;
import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import immobilier.immobilier.model.ChiffreAdmin;
import immobilier.immobilier.model.ChiffreProprio;
import immobilier.immobilier.model.Conn;
import immobilier.immobilier.model.Login;
import immobilier.immobilier.model.Utilisateur;
import immobilier.immobilier.model.V_biens_lib;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/proprio")
public class ProprioController {

    @GetMapping("/voirDashboard")
    public String voirDash(HttpServletRequest request, Model model, String date1, String date2) {
        try {
            Connection con = Conn.con();
            Login log = (Login) request.getSession().getAttribute("user");
            String username = log.getUsername();
            String idUser = new Utilisateur().getIdUtilisateurByUsername(username).getId();
            ChiffreProprio cp = new ChiffreProprio();
            LinkedList<ChiffreProprio> cps = cp.getChiffreProprioEntre2Dates(idUser, date1, date2, con);
            Double totCa = 0.;
            Double gains = 0.;
            for (ChiffreProprio ch : cps) {
                totCa = totCa + ch.getCa();
                gains = gains + ch.getGains();
            }
            model.addAttribute("cps", cps);
            model.addAttribute("totCa", totCa);
            model.addAttribute("gains", gains);
        } catch (Exception e) {
            // TODO: handle exception
        }
        model.addAttribute("page", "dashboardProprio");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dash(Model model) {
        model.addAttribute("page", "voirDashboardPropr");
        return "index";
    }

    @GetMapping("/")
    public String actB(HttpServletRequest request, Model model) {
        try {
            Login log = (Login) request.getSession().getAttribute("user");
            String username = log.getUsername();
            String idUser = new Utilisateur().getIdUtilisateurByUsername(username).getId();
            V_biens_lib vb = new V_biens_lib();
            LinkedList<V_biens_lib> vbl = vb.getBiensByPropr(idUser);
            model.addAttribute("vbl", vbl);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("page", "listeBiensPropr");
        return "index";
    }
}