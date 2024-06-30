package immobilier.immobilier.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import immobilier.immobilier.model.ChiffreAdmin;
import immobilier.immobilier.model.Conn;
import immobilier.immobilier.model.Connector;


@Controller
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/voirDashboard")
    public String dash(Model model, String date1, String date2) {
        try {
            Connection con = Conn.con();
            ChiffreAdmin ca = new ChiffreAdmin();
            LinkedList<ChiffreAdmin> cas = ca.getChiffreAdminEntre2Dates(date1, date2, con);
            Double totCa = 0.;
            Double totCo = 0.;
            LinkedList<String> labels = new LinkedList<>();
            LinkedList<Double> data = new LinkedList<>();
            for (ChiffreAdmin ch : cas) {
                labels.add(ch.getDate());
                data.add(ch.getCommissions());
                totCa = totCa + ch.getChiffre_affaires();
                totCo = totCo + ch.getCommissions();
            }
            model.addAttribute("cas", cas);
            model.addAttribute("labels", labels);
            model.addAttribute("data", data);
            model.addAttribute("totCa", totCa);
            model.addAttribute("totCo", totCo);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        model.addAttribute("page", "dashboardAdmin");
        return "index";
    }

    @GetMapping("/")
    public String actB(Model model) {
        model.addAttribute("page", "voirDashboardAdmin");
        return "index";
    }

    @GetMapping("/reinitialisation")
    public String reinitialisation(Model model) {
        model.addAttribute("page", "reinitialisation");
        return "index";
    }

    @GetMapping("/reinit")
    public String reinit(Model model) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            LinkedList<String> listSql = new LinkedList<>();

            listSql.add("penalite");

            for (String string : listSql) {
                String sql = "DELETE FROM " + string;
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                Connector.CloseStatement(statement);
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Connector.Rollback(connection);
        } finally {

            Connector.CloseConnection(connection);
        }
        return "redirect:/admin/reinitialisation?message=success";
    }

}