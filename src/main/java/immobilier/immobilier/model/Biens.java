package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.util.LinkedList;

@TableName(database = "immobilier", driver = "postgres", name = "biens", password = "366325", user = "postgres")
public class Biens extends JDBC {

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
	@ColumnField(columnName = "etat")
	String etat;
	@ColumnField(columnName = "idtype")
	String idtype;
	@ColumnField(columnName = "reference")
	String reference;

	public LinkedList<Biens> getAllBiens()throws Exception{
		LinkedList<Biens> bs = new Biens().select();
		return bs;
	}

	public Biens() throws Exception {

	}

	public Biens(String nom, String description, String region, Double loyer, String idproprio, String etat,
			String idtype, String reference) throws Exception {
		setNom(nom.trim());
		setDescription(description.trim());
		setRegion(region.trim());
		setLoyer(loyer);
		setIdproprio(idproprio.trim());
		setEtat(etat.trim());
		setIdtype(idtype.trim());
		setReference(reference.trim());
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

	@Getter(columnName = "reference")
	public String getReference() {
		return this.reference;
	}

	@Setter(columnName = "reference")
	public void setReference(String reference) {
		this.reference = reference;
	}

}
