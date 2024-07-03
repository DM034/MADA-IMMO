package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.util.LinkedList;

@TableName(database = "immobilier", driver = "postgres", name = "v_controlle_dispo", password = "366325", user = "postgres")
public class V_controlle_dispo extends JDBC {

	@ColumnField(columnName = "idproprio")
	String idproprio;
	@ColumnField(columnName = "proprio")
	String proprio;
	@ColumnField(columnName = "idbiens")
	String idbiens;
	@ColumnField(columnName = "biens")
	String biens;
	@ColumnField(columnName = "bien_adresse")
	String bien_adresse;
	@ColumnField(columnName = "type")
	String type;
	@ColumnField(columnName = "date_disponibilite")
	String date_disponibilite;

	public V_controlle_dispo getBiensV_controlle_dispo(String idbiens)throws Exception{
		V_controlle_dispo vc = (V_controlle_dispo) new V_controlle_dispo().select("where idbiens = '"+idbiens+"' ").getFirst();
		return vc;
	}

	public LinkedList<V_controlle_dispo> getAllV_controlle_dispo()throws Exception{
		LinkedList<V_controlle_dispo> vc = new V_controlle_dispo().select();
		return vc;
	}

	public V_controlle_dispo() throws Exception {

	}

	public V_controlle_dispo(String idproprio, String proprio, String idbiens, String biens, String bien_adresse,
			String type, String date_disponibilite) throws Exception {
		setIdproprio(idproprio.trim());
		setProprio(proprio.trim());
		setIdbiens(idbiens.trim());
		setBiens(biens.trim());
		setBien_adresse(bien_adresse.trim());
		setType(type.trim());
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

	@Getter(columnName = "idproprio")
	public String getIdproprio() {
		return this.idproprio;
	}

	@Setter(columnName = "idproprio")
	public void setIdproprio(String idproprio) {
		this.idproprio = idproprio;
	}

	@Getter(columnName = "proprio")
	public String getProprio() {
		return this.proprio;
	}

	@Setter(columnName = "proprio")
	public void setProprio(String proprio) {
		this.proprio = proprio;
	}

	@Getter(columnName = "idbiens")
	public String getIdbiens() {
		return this.idbiens;
	}

	@Setter(columnName = "idbiens")
	public void setIdbiens(String idbiens) {
		this.idbiens = idbiens;
	}

	@Getter(columnName = "biens")
	public String getBiens() {
		return this.biens;
	}

	@Setter(columnName = "biens")
	public void setBiens(String biens) {
		this.biens = biens;
	}

	@Getter(columnName = "bien_adresse")
	public String getBien_adresse() {
		return this.bien_adresse;
	}

	@Setter(columnName = "bien_adresse")
	public void setBien_adresse(String bien_adresse) {
		this.bien_adresse = bien_adresse;
	}

	@Getter(columnName = "type")
	public String getType() {
		return this.type;
	}

	@Setter(columnName = "type")
	public void setType(String type) {
		this.type = type;
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
