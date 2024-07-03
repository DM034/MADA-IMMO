package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.sql.Date;
import java.util.LinkedList;

@TableName(database = "immobilier", driver = "postgres", name = "v_detail_biens_disponibilite", password = "366325", user = "postgres")
public class V_detail_biens_disponibilite extends JDBC {

	@ColumnField(columnName = "idbiens")
	String idbiens;
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
	@ColumnField(columnName = "etat")
	String etat;
	@ColumnField(columnName = "idtype")
	String idtype;
	@ColumnField(columnName = "proprio")
	String proprio;
	@ColumnField(columnName = "type_bien")
	String type_bien;
	@ColumnField(columnName = "dernier_montant_loyer")
	Double dernier_montant_loyer;
	@ColumnField(columnName = "date_dernier_changement_loyer")
	Date date_dernier_changement_loyer;
	@ColumnField(columnName = "date_disponibilite")
	String date_disponibilite;

	public V_detail_biens_disponibilite getBiensById(String id) throws Exception {
		LinkedList<V_detail_biens_disponibilite> vb = new V_detail_biens_disponibilite()
				.select("where idbiens = '" + id + "'");
		V_detail_biens_disponibilite v = vb.getFirst();
		return v;
	}

	public LinkedList<V_detail_biens_disponibilite> getBiensByPropr(String idPropr) throws Exception {
		LinkedList<V_detail_biens_disponibilite> vb = new V_detail_biens_disponibilite()
				.select("where idproprio = '" + idPropr + "'");
		return vb;
	}

	public V_detail_biens_disponibilite() throws Exception {

	}

	public V_detail_biens_disponibilite(String idbiens, String nom, String description, String region, Double loyer,
			String idproprio, String etat, String idtype, String proprio, String type_bien,
			Double dernier_montant_loyer, Date date_dernier_changement_loyer, String date_disponibilite)
			throws Exception {
		setIdbiens(idbiens.trim());
		setNom(nom.trim());
		setDescription(description.trim());
		setRegion(region.trim());
		setLoyer(loyer);
		setIdproprio(idproprio.trim());
		setEtat(etat.trim());
		setIdtype(idtype.trim());
		setProprio(proprio.trim());
		setType_bien(type_bien.trim());
		setDernier_montant_loyer(dernier_montant_loyer);
		setDate_dernier_changement_loyer(date_dernier_changement_loyer);
		setDate_disponibilite(date_disponibilite.trim());
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

	@Getter(columnName = "idbiens")
	public String getIdbiens() {
		return this.idbiens;
	}

	@Setter(columnName = "idbiens")
	public void setIdbiens(String idbiens) {
		this.idbiens = idbiens;
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

	@Getter(columnName = "proprio")
	public String getProprio() {
		return this.proprio;
	}

	@Setter(columnName = "proprio")
	public void setProprio(String proprio) {
		this.proprio = proprio;
	}

	@Getter(columnName = "type_bien")
	public String getType_bien() {
		return this.type_bien;
	}

	@Setter(columnName = "type_bien")
	public void setType_bien(String type_bien) {
		this.type_bien = type_bien;
	}

	@Getter(columnName = "dernier_montant_loyer")
	public Double getDernier_montant_loyer() {
		return this.dernier_montant_loyer;
	}

	@Setter(columnName = "dernier_montant_loyer")
	public void setDernier_montant_loyer(Double dernier_montant_loyer) {
		this.dernier_montant_loyer = dernier_montant_loyer;
	}

	@Getter(columnName = "date_dernier_changement_loyer")
	public Date getDate_dernier_changement_loyer() {
		return this.date_dernier_changement_loyer;
	}

	@Setter(columnName = "date_dernier_changement_loyer")
	public void setDate_dernier_changement_loyer(Date date_dernier_changement_loyer) {
		this.date_dernier_changement_loyer = date_dernier_changement_loyer;
	}

	public void setDate_dernier_changement_loyer(String date_dernier_changement_loyer) {
		Date daty_temp = Date.valueOf(date_dernier_changement_loyer.trim());
		setDate_dernier_changement_loyer(daty_temp);
	}

	@Getter(columnName = "date_disponibilite")
	public String getDate_disponibilite() {
		return this.date_disponibilite;
	}

	@Setter(columnName = "date_disponibilite")
	public void setDate_disponibilite(String date_disponibilite) {
		this.date_disponibilite = date_disponibilite;
	}

}
