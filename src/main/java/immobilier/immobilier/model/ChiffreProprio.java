package immobilier.immobilier.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class ChiffreProprio {
    String idProprio;
    String proprio;
    String idBiens;
    String biens;
    String region;
    String type;
    String idLocation;
    String date;
    Double ca;
    Double co;
    Double gains;

    public LinkedList<ChiffreProprio> getChiffreProprioEntre2Dates(String idProprio, String date1, String date2, Connection con)
            throws Exception {
        LinkedList<ChiffreProprio> result = new LinkedList<ChiffreProprio>();
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
            +"        COALESCE( "
            +"            (SELECT la.montant  "
            +"             FROM loyer_mensuel la "
            +"             WHERE la.idBiens = b.id "
            +"               AND la.date_changement <= l.date_debut "
            +"             ORDER BY la.date_changement DESC "
            +"             LIMIT 1), "
            +"            b.loyer "
            +"        ) AS montant_mensuel, "
            +"        c.valeur AS commission_pourcentage "
            +"    FROM mois_complets mc "
            +"    CROSS JOIN location l "
            +"    JOIN biens b ON l.idBiens = b.id "
            +"    JOIN type_biens tb ON b.idType = tb.id "
            +"    JOIN commission c ON tb.id = c.idType "
            +"    JOIN utilisateur u ON b.idProprio = u.id "
            +"    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut) "
            +"      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
            +"      AND u.profil = 'proprio' "
            +") "
            +"SELECT  "
            +"    lm.utilisateur_id as idProprio, "
            +"    lm.utilisateur_nom as proprio, "
            +"    lm.bien_id as idBiens, "
            +"    lm.biens as biens, "
            +"    lm.bien_adresse as region, "
            +"    lm.bien_type as type, "
            +"    lm.location_id as idLocation, "
            +"    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date, "
            +"    lm.montant_mensuel AS ca, "
            +"    lm.montant_mensuel * (lm.commission_pourcentage::float / 100) AS co, "
            +"    lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100) AS gains "
            +"FROM locations_mensuelles lm "
            +"WHERE lm.utilisateur_id = '"+idProprio+"' AND lm.mois BETWEEN '"+date1+"' AND '"+date2+"' "
            +"ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois";
            System.out.println(sql);
            ResultSet rSet = stat.executeQuery(sql);
            while (rSet.next()) {
                ChiffreProprio vrest = new ChiffreProprio(
                        rSet.getString(1),
                        rSet.getString(2),
                        rSet.getString(3),
                        rSet.getString(4),
                        rSet.getString(5),
                        rSet.getString(6),
                        rSet.getString(7),
                        rSet.getString(8),
                        rSet.getDouble(9),
                        rSet.getDouble(10),
                        rSet.getDouble(11));
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

    public ChiffreProprio() {
    }

    public ChiffreProprio(String idProprio, String proprio, String idBiens, String biens, String region, String type, String idLocation, String date, Double ca, Double co, Double gains) {
        this.idProprio = idProprio;
        this.proprio = proprio;
        this.idBiens = idBiens;
        this.biens = biens;
        this.region = region;
        this.type = type;
        this.idLocation = idLocation;
        this.date = date;
        this.ca = ca;
        this.co = co;
        this.gains = gains;
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

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdLocation() {
        return this.idLocation;
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getCa() {
        return this.ca;
    }

    public void setCa(Double ca) {
        this.ca = ca;
    }

    public Double getCo() {
        return this.co;
    }

    public void setCo(Double co) {
        this.co = co;
    }

    public Double getGains() {
        return this.gains;
    }

    public void setGains(Double gains) {
        this.gains = gains;
    }

}
