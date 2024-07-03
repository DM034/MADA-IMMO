package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.sql.Date;
import java.util.LinkedList;

@TableName(database = "immobilier", driver = "postgres", name = "v_liste_locations", password = "366325", user = "postgres")
public class V_liste_locations extends JDBC {

	@ColumnField(columnName = "idlocation")
	String idlocation;
	@ColumnField(columnName = "idbiens")
	String idbiens;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "idclient")
	String idclient;
	@ColumnField(columnName = "username")
	String username;
	@ColumnField(columnName = "duree")
	Double duree;
	@ColumnField(columnName = "date_debut")
	Date date_debut;
	@ColumnField(columnName = "date_fin")
	Date date_fin;

	
	public LinkedList<V_liste_locations> getAllV_liste_locations()throws Exception{
		LinkedList<V_liste_locations> locs = new V_liste_locations().select();
		return locs;
	}

	public V_liste_locations() throws Exception {

	}

	public V_liste_locations(String idlocation, String idbiens, String nom, String idclient, String username,
			Double duree, Date date_debut, Date date_fin) throws Exception {
		setIdlocation(idlocation.trim());
		setIdbiens(idbiens.trim());
		setNom(nom.trim());
		setIdclient(idclient.trim());
		setUsername(username.trim());
		setDuree(duree);
		setDate_debut(date_debut);
		setDate_fin(date_fin);
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

	@Getter(columnName = "idlocation")
	public String getIdlocation() {
		return this.idlocation;
	}

	@Setter(columnName = "idlocation")
	public void setIdlocation(String idlocation) {
		this.idlocation = idlocation;
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

	@Getter(columnName = "idclient")
	public String getIdclient() {
		return this.idclient;
	}

	@Setter(columnName = "idclient")
	public void setIdclient(String idclient) {
		this.idclient = idclient;
	}

	@Getter(columnName = "username")
	public String getUsername() {
		return this.username;
	}

	@Setter(columnName = "username")
	public void setUsername(String username) {
		this.username = username;
	}

	@Getter(columnName = "duree")
	public Double getDuree() {
		return this.duree;
	}

	@Setter(columnName = "duree")
	public void setDuree(Double duree) {
		this.duree = duree;
	}

	@Getter(columnName = "date_debut")
	public Date getDate_debut() {
		return this.date_debut;
	}

	@Setter(columnName = "date_debut")
	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}

	public void setDate_debut(String date_debut) {
		Date daty_temp = Date.valueOf(date_debut.trim());
		setDate_debut(daty_temp);
	}

	@Getter(columnName = "date_fin")
	public Date getDate_fin() {
		return this.date_fin;
	}

	@Setter(columnName = "date_fin")
	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}

	public void setDate_fin(String date_fin) {
		Date daty_temp = Date.valueOf(date_fin.trim());
		setDate_fin(daty_temp);
	}

}
