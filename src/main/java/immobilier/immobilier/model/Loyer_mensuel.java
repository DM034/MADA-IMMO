package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.sql.Date;

@TableName(database = "immobilier", driver = "postgres", name = "loyer_mensuel", password = "366325", user = "postgres")
public class Loyer_mensuel extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "idbiens")
	String idbiens;
	@ColumnField(columnName = "montant")
	Double montant;
	@ColumnField(columnName = "date_changement")
	Date date_changement;

	public Loyer_mensuel() throws Exception {

	}

	public Loyer_mensuel(String idbiens, Double montant, Date date_changement) throws Exception {
		setIdbiens(idbiens.trim());
		setMontant(montant);
		setDate_changement(date_changement);
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

	@Getter(columnName = "idbiens")
	public String getIdbiens() {
		return this.idbiens;
	}

	@Setter(columnName = "idbiens")
	public void setIdbiens(String idbiens) {
		this.idbiens = idbiens;
	}

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	@Getter(columnName = "date_changement")
	public Date getDate_changement() {
		return this.date_changement;
	}

	@Setter(columnName = "date_changement")
	public void setDate_changement(Date date_changement) {
		this.date_changement = date_changement;
	}

	public void setDate_changement(String date_changement) {
		Date daty_temp = Date.valueOf(date_changement.trim());
		setDate_changement(daty_temp);
	}

}
