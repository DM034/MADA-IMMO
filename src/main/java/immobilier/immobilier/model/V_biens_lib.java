package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

@TableName(database = "immobilier", driver = "postgres", name = "v_biens_lib", password = "366325", user = "postgres")
public class V_biens_lib extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "description")
	String description;
	@ColumnField(columnName = "region")
	String region;
	@ColumnField(columnName = "loyer")
	Double loyer;
	@ColumnField(columnName = "idproprio")
	String idproprio;
	@ColumnField(columnName = "username")
	String username;
	@ColumnField(columnName = "etat")
	String etat;
	@ColumnField(columnName = "idtype")
	String idtype;
	@ColumnField(columnName = "type")
	String type;

	// public String getDisponibiliteByIdBiens(String idBiens, Connection con)throws Exception{
	// 	LinkedList<Disponiblite> di = new Disponiblite().getDisponibiliteBienByIdPropr(idBiens, con);
	// 	Disponiblite disp = di.getFirst();
	// 	String dispo = disp.getDate_disponibilite();
	// 	return dispo;
	// }

	public LinkedList<Disponiblite> getDisponibiliteBienByIdPropr(String idBiens, Connection con)
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


	public V_biens_lib getBiensById(String id)throws Exception{
		LinkedList<V_biens_lib> vb = new V_biens_lib().select("where id = '"+id+"'");
		V_biens_lib v = vb.getFirst();
		return v;
	}

	public LinkedList<V_biens_lib> getBiensByPropr(String idPropr)throws Exception{
		LinkedList<V_biens_lib> vb = new V_biens_lib().select("where idproprio = '"+idPropr+"'");
		return vb;
	}

	public V_biens_lib() throws Exception {

	}

	public V_biens_lib(String nom, String description, String region, Double loyer, String idproprio, String username,
			String etat, String idtype, String type) throws Exception {
		setNom(nom.trim());
		setDescription(description.trim());
		setRegion(region.trim());
		setLoyer(loyer);
		setIdproprio(idproprio.trim());
		setUsername(username.trim());
		setEtat(etat.trim());
		setIdtype(idtype.trim());
		setType(type.trim());
	}

	public int count(Connection connection) throws Exception {
		int count = 0;
		try {
			count = select(connection).size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Getter(columnName = "id")
	public String getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(String id) {
		this.id = id;
	}

	@Getter(columnName = "nom")
	public String getNom() {
		return this.nom;
	}

	@Setter(columnName = "nom")
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Getter(columnName = "description")
	public String getDescription() {
		return this.description;
	}

	@Setter(columnName = "description")
	public void setDescription(String description) {
		this.description = description;
	}

	@Getter(columnName = "region")
	public String getRegion() {
		return this.region;
	}

	@Setter(columnName = "region")
	public void setRegion(String region) {
		this.region = region;
	}

	@Getter(columnName = "loyer")
	public Double getLoyer() {
		return this.loyer;
	}

	@Setter(columnName = "loyer")
	public void setLoyer(Double loyer) {
		this.loyer = loyer;
	}

	@Getter(columnName = "idproprio")
	public String getIdproprio() {
		return this.idproprio;
	}

	@Setter(columnName = "idproprio")
	public void setIdproprio(String idproprio) {
		this.idproprio = idproprio;
	}

	@Getter(columnName = "username")
	public String getUsername() {
		return this.username;
	}

	@Setter(columnName = "username")
	public void setUsername(String username) {
		this.username = username;
	}

	@Getter(columnName = "etat")
	public String getEtat() {
		return this.etat;
	}

	@Setter(columnName = "etat")
	public void setEtat(String etat) {
		this.etat = etat;
	}

	@Getter(columnName = "idtype")
	public String getIdtype() {
		return this.idtype;
	}

	@Setter(columnName = "idtype")
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	@Getter(columnName = "type")
	public String getType() {
		return this.type;
	}

	@Setter(columnName = "type")
	public void setType(String type) {
		this.type = type;
	}

}
