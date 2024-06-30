package immobilier.immobilier.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class ChiffreAdmin {
    String date;
    Double chiffre_affaires;
    Double commissions;
    Double gains;

    public LinkedList<ChiffreAdmin> getChiffreAdminEntre2Dates(String date1, String date2, Connection con)
            throws Exception {
        LinkedList<ChiffreAdmin> result = new LinkedList<ChiffreAdmin>();
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
                    + "    SELECT DATE_TRUNC('month', MIN(l.date_debut)) AS mois "
                    + "    FROM location l "
                    + "    UNION ALL "
                    + "    SELECT mois + INTERVAL '1 month' "
                    + "    FROM mois_complets "
                    + "    WHERE mois < DATE_TRUNC('month', (SELECT MAX(l.date_debut + (l.duree || ' months')::INTERVAL) FROM location l)) "
                    + "), locations_mensuelles AS ( "
                    + "    SELECT  "
                    + "        mc.mois, "
                    + "        l.id AS location_id, "
                    + "        COALESCE( "
                    + "            (SELECT la.montant  "
                    + "             FROM loyer_mensuel la "
                    + "             WHERE la.idBiens = b.id "
                    + "               AND la.date_changement <= l.date_debut "
                    + "             ORDER BY la.date_changement DESC "
                    + "             LIMIT 1), "
                    + "            b.loyer "
                    + "        ) AS montant_mensuel, "
                    + "        c.valeur AS commission_pourcentage "
                    + "    FROM mois_complets mc "
                    + "    CROSS JOIN location l "
                    + "    JOIN biens b ON l.idBiens = b.id "
                    + "    JOIN type_biens tb ON b.idType = tb.id "
                    + "    JOIN commission c ON tb.id = c.idType "
                    + "    WHERE mc.mois >= DATE_TRUNC('month', l.date_debut) "
                    + "      AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
                    + ") "
                    + "SELECT  "
                    + "    TO_CHAR(dates.mois, 'YYYY-MM-DD') AS mois, "
                    + "    COALESCE(SUM(lm.montant_mensuel), 0) AS chiffre_affaires, "
                    + "    COALESCE(SUM(lm.montant_mensuel * (lm.commission_pourcentage::float / 100)), 0) AS commissions, "
                    + "    COALESCE(SUM(lm.montant_mensuel * (1 - lm.commission_pourcentage::float / 100)), 0) AS gains "
                    + "FROM ( "
                    + "    SELECT GENERATE_SERIES('" + date1 + "'::date, '" + date2 + "'::date, '1 month'::interval) AS mois "
                    + ") dates "
                    + "LEFT JOIN locations_mensuelles lm ON dates.mois = lm.mois "
                    + "GROUP BY dates.mois "
                    + "ORDER BY dates.mois";
            System.out.println(sql);
            ResultSet rSet = stat.executeQuery(sql);
            while (rSet.next()) {
                ChiffreAdmin vrest = new ChiffreAdmin(
                        rSet.getString(1),
                        rSet.getDouble(2),
                        rSet.getDouble(3),
                        rSet.getDouble(4));
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

    public ChiffreAdmin() {
    }

    public ChiffreAdmin(String date, Double chiffre_affaires, Double commissions, Double gains) {
        this.date = date;
        this.chiffre_affaires = chiffre_affaires;
        this.commissions = commissions;
        this.gains = gains;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getChiffre_affaires() {
        return this.chiffre_affaires;
    }

    public void setChiffre_affaires(Double chiffre_affaires) {
        this.chiffre_affaires = chiffre_affaires;
    }

    public Double getCommissions() {
        return this.commissions;
    }

    public void setCommissions(Double commissions) {
        this.commissions = commissions;
    }

    public Double getGains() {
        return this.gains;
    }

    public void setGains(Double gains) {
        this.gains = gains;
    }

}
