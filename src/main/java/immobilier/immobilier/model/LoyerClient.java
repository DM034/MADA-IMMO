package immobilier.immobilier.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class LoyerClient {
    String idClient;
    String client;
    String idProprio;
    String proprio;
    String idBiens;
    String biens;
    String region;
    String type;
    String idLocation;
    String date;
    Double montant;
    String statut_paiement;

    public LinkedList<LoyerClient> getLoyerClientEntre2Dates(String idClient, String date1, String date2, Connection con)
            throws Exception {
        LinkedList<LoyerClient> result = new LinkedList<LoyerClient>();
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
            +"        u_proprio.id AS utilisateur_id, "
            +"        u_proprio.username AS utilisateur_nom, "
            +"        u_client.id AS client_id, "
            +"        u_client.username AS client_nom, "
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
            +"    JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut) "
            +"        AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
            +"    JOIN biens b ON l.idBiens = b.id "
            +"    JOIN type_biens tb ON b.idType = tb.id "
            +"    JOIN commission c ON tb.id = c.idType "
            +"    JOIN utilisateur u_proprio ON b.idProprio = u_proprio.id "
            +"    JOIN utilisateur u_client ON l.idClient = u_client.id "
            +"    WHERE u_client.profil = 'user' "
            +") "
            +"SELECT  "
            +"    lm.client_id as idClient, "
            +"    lm.client_nom as client, "
            +"    lm.utilisateur_id as idProprio, "
            +"    lm.utilisateur_nom as proprio, "
            +"    lm.bien_id as idBiens, "
            +"    lm.biens as biens, "
            +"    lm.bien_adresse as region, "
            +"    lm.bien_type as type, "
            +"    lm.location_id as idLocation, "
            +"    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date, "
            +"    lm.montant_mensuel AS montant, "
            +"    CASE "
            +"        WHEN lm.mois <= DATE_TRUNC('month', CURRENT_DATE) THEN 'Payé' "
            +"        ELSE 'À payer' "
            +"    END AS statut_paiement "
            +"FROM locations_mensuelles lm "
            +"WHERE lm.client_id = '"+idClient+"' "
            +"  AND lm.mois BETWEEN DATE_TRUNC('month', '"+date1+"'::DATE) AND DATE_TRUNC('month', '"+date2+"'::DATE) "
            +"ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois";
            System.out.println(sql);
            ResultSet rSet = stat.executeQuery(sql);
            while (rSet.next()) {
                LoyerClient vrest = new LoyerClient(
                        rSet.getString(1),
                        rSet.getString(2),
                        rSet.getString(3),
                        rSet.getString(4),
                        rSet.getString(5),
                        rSet.getString(6),
                        rSet.getString(7),
                        rSet.getString(8),
                        rSet.getString(9),
                        rSet.getString(10),
                        rSet.getDouble(11),
                        rSet.getString(12));
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

    public LinkedList<LoyerClient> getLoyerClientAPayerEntre2Dates(String idClient, String date1, String date2, Connection con)
            throws Exception {
        LinkedList<LoyerClient> result = new LinkedList<LoyerClient>();
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
            +"        u_proprio.id AS utilisateur_id, "
            +"        u_proprio.username AS utilisateur_nom, "
            +"        u_client.id AS client_id, "
            +"        u_client.username AS client_nom, "
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
            +"    JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut) "
            +"        AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
            +"    JOIN biens b ON l.idBiens = b.id "
            +"    JOIN type_biens tb ON b.idType = tb.id "
            +"    JOIN commission c ON tb.id = c.idType "
            +"    JOIN utilisateur u_proprio ON b.idProprio = u_proprio.id "
            +"    JOIN utilisateur u_client ON l.idClient = u_client.id "
            +"    WHERE u_client.profil = 'user' "
            +") "
            +"SELECT  "
            +"    lm.client_id as idClient, "
            +"    lm.client_nom as client, "
            +"    lm.utilisateur_id as idProprio, "
            +"    lm.utilisateur_nom as proprio, "
            +"    lm.bien_id as idBiens, "
            +"    lm.biens as biens, "
            +"    lm.bien_adresse as region, "
            +"    lm.bien_type as type, "
            +"    lm.location_id as idLocation, "
            +"    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date, "
            +"    lm.montant_mensuel AS montant, "
            +"    CASE "
            +"        WHEN lm.mois <= DATE_TRUNC('month', CURRENT_DATE) THEN 'Payé' "
            +"        ELSE 'À payer' "
            +"    END AS statut_paiement "
            +"FROM locations_mensuelles lm "
            +"WHERE lm.client_id = '"+idClient+"' "
            +"  AND lm.mois BETWEEN DATE_TRUNC('month', '"+date1+"'::DATE) AND DATE_TRUNC('month', '"+date2+"'::DATE) "
            +"  AND lm.mois > DATE_TRUNC('month', CURRENT_DATE) "
            +"ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois";
            System.out.println(sql);
            ResultSet rSet = stat.executeQuery(sql);
            while (rSet.next()) {
                LoyerClient vrest = new LoyerClient(
                        rSet.getString(1),
                        rSet.getString(2),
                        rSet.getString(3),
                        rSet.getString(4),
                        rSet.getString(5),
                        rSet.getString(6),
                        rSet.getString(7),
                        rSet.getString(8),
                        rSet.getString(9),
                        rSet.getString(10),
                        rSet.getDouble(11),
                        rSet.getString(12));
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

    public LinkedList<LoyerClient> getLoyerClientPayeEntre2Dates(String idClient, String date1, String date2, Connection con)
            throws Exception {
        LinkedList<LoyerClient> result = new LinkedList<LoyerClient>();
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
            +"        u_proprio.id AS utilisateur_id, "
            +"        u_proprio.username AS utilisateur_nom, "
            +"        u_client.id AS client_id, "
            +"        u_client.username AS client_nom, "
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
            +"    JOIN location l ON mc.mois >= DATE_TRUNC('month', l.date_debut) "
            +"        AND mc.mois < DATE_TRUNC('month', l.date_debut + (l.duree || ' months')::INTERVAL) "
            +"    JOIN biens b ON l.idBiens = b.id "
            +"    JOIN type_biens tb ON b.idType = tb.id "
            +"    JOIN commission c ON tb.id = c.idType "
            +"    JOIN utilisateur u_proprio ON b.idProprio = u_proprio.id "
            +"    JOIN utilisateur u_client ON l.idClient = u_client.id "
            +"    WHERE u_client.profil = 'user' "
            +") "
            +"SELECT  "
            +"    lm.client_id as idClient, "
            +"    lm.client_nom as client, "
            +"    lm.utilisateur_id as idProprio, "
            +"    lm.utilisateur_nom as proprio, "
            +"    lm.bien_id as idBiens, "
            +"    lm.biens as biens, "
            +"    lm.bien_adresse as region, "
            +"    lm.bien_type as type, "
            +"    lm.location_id as idLocation, "
            +"    TO_CHAR(lm.mois, 'YYYY-MM-DD') AS date, "
            +"    lm.montant_mensuel AS montant, "
            +"    CASE "
            +"        WHEN lm.mois <= DATE_TRUNC('month', CURRENT_DATE) THEN 'Payé' "
            +"        ELSE 'À payer' "
            +"    END AS statut_paiement "
            +"FROM locations_mensuelles lm "
            +"WHERE lm.client_id = '"+idClient+"' "
            +"  AND lm.mois BETWEEN DATE_TRUNC('month', '"+date1+"'::DATE) AND DATE_TRUNC('month', '"+date2+"'::DATE) "
            +"  AND lm.mois <= DATE_TRUNC('month', CURRENT_DATE) "
            +"ORDER BY lm.utilisateur_id, lm.bien_id, lm.location_id, lm.mois";
            System.out.println(sql);
            ResultSet rSet = stat.executeQuery(sql);
            while (rSet.next()) {
                LoyerClient vrest = new LoyerClient(
                        rSet.getString(1),
                        rSet.getString(2),
                        rSet.getString(3),
                        rSet.getString(4),
                        rSet.getString(5),
                        rSet.getString(6),
                        rSet.getString(7),
                        rSet.getString(8),
                        rSet.getString(9),
                        rSet.getString(10),
                        rSet.getDouble(11),
                        rSet.getString(12));
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

    
	public String getColorsMont() {
        String color = "";
        try {
            if(this.getMontant() <= 700000){
				color = "bg-primary";
            }else if(this.getMontant() > 700000){
				color = "bg-danger";
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return color;
    }
    
	public String getColors() {
        String color = "";
        try {
			if(this.getStatut_paiement().equals("Payé")){
				color = "bg-success";
			}else if(this.getStatut_paiement().equals("À payer")){
				color = "bg-warning";
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return color;
    }



    public LoyerClient() {
    }

    public LoyerClient(String idClient, String client, String idProprio, String proprio, String idBiens, String biens, String region, String type, String idLocation, String date, Double montant, String statut_paiement) {
        this.idClient = idClient;
        this.client = client;
        this.idProprio = idProprio;
        this.proprio = proprio;
        this.idBiens = idBiens;
        this.biens = biens;
        this.region = region;
        this.type = type;
        this.idLocation = idLocation;
        this.date = date;
        this.montant = montant;
        this.statut_paiement = statut_paiement;
    }

    public String getIdClient() {
        return this.idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getClient() {
        return this.client;
    }

    public void setClient(String client) {
        this.client = client;
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

    public Double getMontant() {
        return this.montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getStatut_paiement() {
        return this.statut_paiement;
    }

    public void setStatut_paiement(String statut_paiement) {
        this.statut_paiement = statut_paiement;
    }

}
