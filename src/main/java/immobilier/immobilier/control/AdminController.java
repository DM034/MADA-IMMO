package immobilier.immobilier.control;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import immobilier.immobilier.model.Biens;
import immobilier.immobilier.model.ChiffreAdmin;
import immobilier.immobilier.model.Conn;
import immobilier.immobilier.model.Connector;
import immobilier.immobilier.model.Csv_biens;
import immobilier.immobilier.model.Csv_commission;
import immobilier.immobilier.model.Csv_location;
import immobilier.immobilier.model.Location;
import immobilier.immobilier.model.Utilisateur;
import immobilier.immobilier.model.V_detail_location;
import immobilier.immobilier.model.V_liste_locations;
import immobilier.immobilier.tools.Tools;


@Controller
@RequestMapping("/admin")

public class AdminController {
    
    @GetMapping("/detailLocation")
    public String detailLocation( Model model, String id) {
        try {
            V_detail_location loc = new V_detail_location();
            LinkedList<V_detail_location> locs = loc.getAllV_detail_locationsByIdLoc(id);
            model.addAttribute("locs", locs);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("page", "listeDetailLocation");
        return "index";
    }
    
    @GetMapping("/listeLocation")
    public String listLocation( Model model) {
        try {
            V_liste_locations loc = new V_liste_locations();
            LinkedList<V_liste_locations> locs = loc.getAllV_liste_locations();
            model.addAttribute("locs", locs);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("page", "listeLocation");
        return "index";
    }
    
    @GetMapping("/nouvelleLocation")
    public String newLocation(String client, String biens, Date date, Double duree, Model model) {
        try {
            Location loc = new Location(biens, client, duree, date);
            loc.createLocation();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "redirect:/admin/location?message="+e.getMessage();
        }
        return "redirect:/admin/";
    }

    @GetMapping("/location")
    public String location(Model model, @RequestParam(defaultValue = "") String message) {
        try {
            LinkedList<Utilisateur> ut = new Utilisateur().getAllClient();
            LinkedList<Biens> bs = new Biens().getAllBiens();
            model.addAttribute("client", ut);
            model.addAttribute("message", message);
            model.addAttribute("biens", bs);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAttribute("page", "location");
        return "index";
    }

    @GetMapping("/voirDashboard")
    public String dash(Model model, String date1, String date2) {
        try {
            Connection con = Conn.con();
            ChiffreAdmin ca = new ChiffreAdmin();

            LinkedList<ChiffreAdmin> cas = ca.getChiffreAdminDouble1erMoisEntre2Dates(date1, date2, con);
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
            model.addAttribute("totCa", Tools.formatThousand(totCa));
            model.addAttribute("totCo", Tools.formatThousand(totCo));
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        model.addAttribute("page", "dashboardAdmin");
        return "index";
    }

    @PostMapping("/saveDonnees")
    public String saveTravaux(MultipartFile biens, MultipartFile commission, MultipartFile location) {
        Connection connection = null;
        try {
            connection = Connector.connect();
            new Csv_biens().saveData(biens, connection);
            new Csv_commission().saveData(commission, connection);
            new Csv_location().saveData(location, connection);
            connection.commit();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Connector.Rollback(connection);
        } finally {
            Connector.CloseConnection(connection);
        }

        return "redirect:/admin/";
    }

    @GetMapping("/importDonnees")
    public String actImportD(Model model) {
        model.addAttribute("page", "importDonnees");
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

            listSql.add("loyer_mensuel");
            listSql.add("paiement");
            listSql.add("photo");
            listSql.add("location");
            listSql.add("commission");
            listSql.add("biens");
            listSql.add("type_biens");
            listSql.add("utilisateur");
            listSql.add("csv_biens");
            listSql.add("csv_location");
            listSql.add("csv_commission");

            for (String string : listSql) {
                String sql = "DELETE FROM " + string;
                System.out.println(sql);
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