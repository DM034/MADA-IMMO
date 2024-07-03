package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import immobilier.immobilier.tools.Tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import org.springframework.web.multipart.MultipartFile;

@TableName(database = "immobilier", driver = "postgres", name = "csv_location", password = "366325", user = "postgres")
public class Csv_location extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "reference")
	String reference;
	@ColumnField(columnName = "date_debut")
	String date_debut;
	@ColumnField(columnName = "duree_mois")
	Double duree_mois;
	@ColumnField(columnName = "client")
	String client;
	int ligne;
	// LinkedList<Erreur> erreurs = new LinkedList<Erreur>();

	public void saveData(MultipartFile file, Connection connection) throws Exception {
		InputStream inputStream = file.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		reader.readLine();
		int numero = 1;

		while ((line = reader.readLine()) != null) {
			// String[] data = line.split(",");
			String[] data = Tools.parseCSVLine(line);
			Csv_location csv_modele = new Csv_location();
			numero = numero + 1;
			setLigne(numero);
			csv_modele.setReference(data[0]);

			String dtd = Tools.formatDate(data[1], "dd/MM/yyyy", "yyyy-MM-dd");
			csv_modele.setDate_debut(dtd);

			// String duree = data[2].replace(",", ".");
			csv_modele.setDuree_mois(Double.valueOf(data[2]));

			csv_modele.setClient(data[3]);

			// if (erreurs.isEmpty()) {
			csv_modele.insert(connection);
			// } else {
			// csv_modele.saveErreur(connection);
			// }
		}
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;

		try {
		String sql1 = "insert into utilisateur (username) select client from csv_location group by client";
		preparedStatement1 = connection.prepareStatement(sql1);
		preparedStatement1.executeUpdate();

		String sql2 = "update utilisateur set profil = 'user' WHERE profil is null";
		preparedStatement2 = connection.prepareStatement(sql2);
		preparedStatement2.executeUpdate();

		String sql3 = "insert into location (idBiens, idClient, duree, date_debut)\r\n" + //
						"    select biens.id, u.id, csv.duree_mois, TO_DATE(csv.date_debut, 'YYYY-MM-DD') AS date_debut from csv_location csv\r\n" + //
						"    join biens on biens.reference = csv.reference\r\n" + //
						"    join utilisateur u on u.username = csv.client;";
		preparedStatement3 = connection.prepareStatement(sql3);
		preparedStatement3.executeUpdate();

		String sql4 = "insert into loyer_mensuel (idBiens, montant, date_changement) "+
						"select biens.id, csv_biens.loyer_mensuel, (select MIN(TO_DATE(date_debut, 'YYYY-MM-DD')) from csv_location) as date_changement from csv_biens "+
						"join biens on biens.reference = csv_biens.reference";
		preparedStatement4 = connection.prepareStatement(sql4);
		preparedStatement4.executeUpdate();

		} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		} finally {

		Connector.CloseStatement(preparedStatement1);
		Connector.CloseStatement(preparedStatement2);
		Connector.CloseStatement(preparedStatement3);
		Connector.CloseStatement(preparedStatement4);
		}

	}

	public 	Csv_location() throws Exception {

	}

	public Csv_location(String reference, String date_debut, Double duree_mois, String client) throws Exception {
		setReference(reference.trim());
		setDate_debut(date_debut);
		setDuree_mois(duree_mois);
		setClient(client.trim());
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

	public int getLigne() {
		return this.ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	@Getter(columnName = "id")
	public Long getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(Long id) {
		this.id = id;
	}

	@Getter(columnName = "reference")
	public String getReference() {
		return this.reference;
	}

	@Setter(columnName = "reference")
	public void setReference(String reference) {
		this.reference = reference;
	}

	@Getter(columnName = "date_debut")
	public String getDate_debut() {
		return this.date_debut;
	}

	@Setter(columnName = "date_debut")
	public void setDate_debut(String date_debut) {
		this.date_debut = date_debut;
	}

	@Getter(columnName = "duree_mois")
	public Double getDuree_mois() {
		return this.duree_mois;
	}

	@Setter(columnName = "duree_mois")
	public void setDuree_mois(Double duree_mois) {
		this.duree_mois = duree_mois;
	}

	@Getter(columnName = "client")
	public String getClient() {
		return this.client;
	}

	@Setter(columnName = "client")
	public void setClient(String client) {
		this.client = client;
	}

}
