package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
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
