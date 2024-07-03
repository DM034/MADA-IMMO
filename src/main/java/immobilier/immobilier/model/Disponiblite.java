package immobilier.immobilier.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class Disponiblite {
    String idProprio;
    String proprio;
    String idBiens;
    String biens;
    String regions;
    String type;
    String date_disponibilite;

    public LinkedList<Disponiblite> getDisponibiliteBienByIdBien(String idBiens, Connection con)
            throws Exception {
        LinkedList<Disponiblite> result = new LinkedList<Disponiblite>();
        boolean open = false;
        if (con == null) {
            try {
                con = Conn.con();
            } catch (Exception e) {
                e.printStackTrace();
            }
            open = true;
        }
        try {
            Statement stat = con.createStatement();
            String sql = "WITH RECURSIVE mois_complets AS ( "
            +"    SELECT DATE_TRUNC('month', MIN(l.date_debut)) AS mois "
            +"    FROM location l "
            +"    UNION ALL "
            +"    SELECT mois + INTERVAL '1 month' "
            +"    FROM mois_complets "
            +"    WHERE mois < DATE_TRUNC('month', (SELECT MAX(l.date_debut + (l.duree || ' months')::INTERVAL) FROM location l)) "
            +"), locations_mensuelles AS ( "
            +"    SELECT  "
            +"        mc.mois, "
            +"        l.id AS location_id, "
            +"        b.id AS bien_id, "
            +"        b.nom AS biens, "
            +"        b.region AS bien_adresse, "
            +"        tb.nom AS bien_type, "
            +"        u.id AS utilisateur_id, "
            +"        u.username AS utilisateur_nom, "
            +"        l.date_debut, "
            +"        l.date_debut + (l.duree || ' months')::INTERVAL AS date_fin, "
            +"        COALESCE( "
            +"            (SELECT la.montant  "
            +"             FROM loyer_mensuel la "
            +"             WHERE la.idBiens = b.id "
            +"               AND la.date_changement <= l.date_debut "
            +"             ORDER BY la.date_changement DESC "
            +"             LIMIT 1), "
            +"            b.loyer "
            +"        ) AS montant_mensuel, "
            +"        c.valeur AS commission_pourcentage, "
            +"        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS row_num "
            +"    FROM mois_complets mc "
            +"    CROSS JOIN location l "
            +"    JOIN biens b ON l.idBiens = b.id "
            +"    JOIN type_biens tb ON b.idType = tb.id "
            +"    JOIN commission c ON tb.id = c.idType "
            +"    JOIN utilisateur u ON b.idProprio = u.id "
            +"    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut) "
            +"      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
            +"      AND u.profil = 'proprio' "
            +"), derniere_location AS ( "
            +"    SELECT  "
            +"        bien_id, "
            +"        MAX(date_fin) AS derniere_date_fin "
            +"    FROM locations_mensuelles "
            +"    GROUP BY bien_id "
            +") "
            +"SELECT DISTINCT "
            +"    lm.utilisateur_id as idProprio, "
            +"    lm.utilisateur_nom as proprio, "
            +"    lm.bien_id as idBiens, "
            +"    lm.biens as biens, "
            +"    lm.bien_adresse as region, "
            +"    lm.bien_type as type, "
            +"    TO_CHAR(DATE_TRUNC('month', COALESCE(dl.derniere_date_fin + INTERVAL '1 day', CURRENT_DATE)), 'YYYY-MM-DD') AS date_disponibilite "
            +"FROM locations_mensuelles lm "
            +"LEFT JOIN derniere_location dl ON lm.bien_id = dl.bien_id "
            +"WHERE lm.bien_id = '"+idBiens+"' "
            +"ORDER BY lm.utilisateur_id, lm.bien_id";
            System.out.println(sql);
            ResultSet rSet = stat.executeQuery(sql);
            while (rSet.next()) {
                Disponiblite vrest = new Disponiblite(
                        rSet.getString(1),
                        rSet.getString(2),
                        rSet.getString(3),
                        rSet.getString(4),
                        rSet.getString(5),
                        rSet.getString(6),
                        rSet.getString(7));
                result.add(vrest);
            }
            if (open == true) {
                con.close();
            }

        } catch (Exception e) {
            if (open == true) {
                con.close();
            }
            e.getMessage();
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public LinkedList<Disponiblite> getDisponibiliteBineByIdPropr(String idPropr, Connection con)
            throws Exception {
        LinkedList<Disponiblite> result = new LinkedList<Disponiblite>();
        boolean open = false;
        if (con == null) {
            try {
                con = Conn.con();
            } catch (Exception e) {
                e.printStackTrace();
            }
            open = true;
        }
        try {
            Statement stat = con.createStatement();
            String sql = "WITH RECURSIVE mois_complets AS ( "
            +"    SELECT DATE_TRUNC('month', MIN(l.date_debut)) AS mois "
            +"    FROM location l "
            +"    UNION ALL "
            +"    SELECT mois + INTERVAL '1 month' "
            +"    FROM mois_complets "
            +"    WHERE mois < DATE_TRUNC('month', (SELECT MAX(l.date_debut + (l.duree || ' months')::INTERVAL) FROM location l)) "
            +"), locations_mensuelles AS ( "
            +"    SELECT  "
            +"        mc.mois, "
            +"        l.id AS location_id, "
            +"        b.id AS bien_id, "
            +"        b.nom AS biens, "
            +"        b.region AS bien_adresse, "
            +"        tb.nom AS bien_type, "
            +"        u.id AS utilisateur_id, "
            +"        u.username AS utilisateur_nom, "
            +"        l.date_debut, "
            +"        l.date_debut + (l.duree || ' months')::INTERVAL AS date_fin, "
            +"        COALESCE( "
            +"            (SELECT la.montant  "
            +"             FROM loyer_mensuel la "
            +"             WHERE la.idBiens = b.id "
            +"               AND la.date_changement <= l.date_debut "
            +"             ORDER BY la.date_changement DESC "
            +"             LIMIT 1), "
            +"            b.loyer "
            +"        ) AS montant_mensuel, "
            +"        c.valeur AS commission_pourcentage, "
            +"        ROW_NUMBER() OVER (PARTITION BY l.id ORDER BY mc.mois) AS row_num "
            +"    FROM mois_complets mc "
            +"    CROSS JOIN location l "
            +"    JOIN biens b ON l.idBiens = b.id "
            +"    JOIN type_biens tb ON b.idType = tb.id "
            +"    JOIN commission c ON tb.id = c.idType "
            +"    JOIN utilisateur u ON b.idProprio = u.id "
            +"    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut) "
            +"      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
            +"      AND u.profil = 'proprio' "
            +"), derniere_location AS ( "
            +"    SELECT  "
            +"        bien_id, "
            +"        MAX(date_fin) AS derniere_date_fin "
            +"    FROM locations_mensuelles "
            +"    GROUP BY bien_id "
            +") "
            +"SELECT DISTINCT "
            +"    lm.utilisateur_id as idProprio, "
            +"    lm.utilisateur_nom as proprio, "
            +"    lm.bien_id as idBiens, "
            +"    lm.biens as biens, "
            +"    lm.bien_adresse as region, "
            +"    lm.bien_type as type, "
            +"    TO_CHAR(DATE_TRUNC('month', COALESCE(dl.derniere_date_fin + INTERVAL '1 day', CURRENT_DATE)), 'YYYY-MM-DD') AS date_disponibilite "
            +"FROM locations_mensuelles lm "
            +"LEFT JOIN derniere_location dl ON lm.bien_id = dl.bien_id "
            +"WHERE lm.utilisateur_id = '"+idPropr+"' "
            +"ORDER BY lm.utilisateur_id, lm.bien_id";
            System.out.println(sql);
            ResultSet rSet = stat.executeQuery(sql);
            while (rSet.next()) {
                Disponiblite vrest = new Disponiblite(
                        rSet.getString(1),
                        rSet.getString(2),
                        rSet.getString(3),
                        rSet.getString(4),
                        rSet.getString(5),
                        rSet.getString(6),
                        rSet.getString(7));
                result.add(vrest);
            }
            if (open == true) {
                con.close();
            }

        } catch (Exception e) {
            if (open == true) {
                con.close();
            }
            e.getMessage();
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    public Disponiblite() {
    }

    public Disponiblite(String idProprio, String proprio, String idBiens, String biens, String regions, String type, String date_disponibilite) {
        this.idProprio = idProprio;
        this.proprio = proprio;
        this.idBiens = idBiens;
        this.biens = biens;
        this.regions = regions;
        this.type = type;
        this.date_disponibilite = date_disponibilite;
    }

    public String getIdProprio() {
        return this.idProprio;
    }

    public void setIdProprio(String idProprio) {
        this.idProprio = idProprio;
    }

    public String getProprio() {
        return this.proprio;
    }

    public void setProprio(String proprio) {
        this.proprio = proprio;
    }

    public String getIdBiens() {
        return this.idBiens;
    }

    public void setIdBiens(String idBiens) {
        this.idBiens = idBiens;
    }

    public String getBiens() {
        return this.biens;
    }

    public void setBiens(String biens) {
        this.biens = biens;
    }

    public String getRegions() {
        return this.regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_disponibilite() {
        return this.date_disponibilite;
    }

    public void setDate_disponibilite(String date_disponibilite) {
        this.date_disponibilite = date_disponibilite;
    }

}
