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
import immobilier.immobilier.model.Disponiblite;
import immobilier.immobilier.model.Login;
import immobilier.immobilier.model.Photo;
import immobilier.immobilier.model.Utilisateur;
import immobilier.immobilier.model.V_biens_lib;
import immobilier.immobilier.model.V_detail_biens_disponibilite;
import immobilier.immobilier.tools.Tools;
import javax.servlet.http.HttpServletRequest;

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
            LinkedList<ChiffreProprio> cps = cp.getChiffreProprioSansComm1erMoisEntre2Dates(idUser, date1, date2, con);
            Double totCa = 0.;
            Double gains = 0.;
            for (ChiffreProprio ch : cps) {
                totCa = totCa + ch.getCa();
                gains = gains + ch.getGains();
            }
            model.addAttribute("cps", cps);
            model.addAttribute("totCa", Tools.formatThousand(totCa));
            model.addAttribute("gains", Tools.formatThousand(gains));
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

    @GetMapping("/detail")
    public String detail(HttpServletRequest request, Model model, String id) {
        try {
            V_detail_biens_disponibilite vbl = new V_detail_biens_disponibilite().getBiensById(id);
            Photo pht = new Photo();
            LinkedList<Photo> phts = pht.getPhotoById(id);            
            model.addAttribute("vbl", vbl);
            model.addAttribute("phts", phts);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("page", "detailPropr");
        return "index";
    }

    @GetMapping("/")
    public String actB(HttpServletRequest request, Model model) {
        try {
            Login log = (Login) request.getSession().getAttribute("user");
            String username = log.getUsername();
            String idUser = new Utilisateur().getIdUtilisateurByUsername(username).getId();
            V_detail_biens_disponibilite vb = new V_detail_biens_disponibilite();
            LinkedList<V_detail_biens_disponibilite> vbl = vb.getBiensByPropr(idUser);
            model.addAttribute("vbl", vbl);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("page", "listeBiensPropr");
        return "index";
    }
}