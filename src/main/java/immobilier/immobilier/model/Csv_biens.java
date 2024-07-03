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
import java.sql.PreparedStatement;

import org.springframework.web.multipart.MultipartFile;

@TableName(database = "immobilier", driver = "postgres", name = "csv_biens", password = "366325", user = "postgres")
public class Csv_biens extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "reference")
	String reference;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "description")
	String description;
	@ColumnField(columnName = "type")
	String type;
	@ColumnField(columnName = "region")
	String region;
	@ColumnField(columnName = "loyer_mensuel")
	Double loyer_mensuel;
	@ColumnField(columnName = "proprietaire")
	String proprietaire;
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
			Csv_biens csv_modele = new Csv_biens();
			numero = numero + 1;
			setLigne(numero);
			csv_modele.setReference(data[0]);
			csv_modele.setNom(data[1]);
			String desc = data[2].replace(",", ".");
			csv_modele.setDescription(desc);
			csv_modele.setType(data[3]);
			csv_modele.setRegion(data[4]);

			csv_modele.setLoyer_mensuel(Double.valueOf(data[5]));

			csv_modele.setProprietaire(data[6]);

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
			String sql1 = "insert into utilisateur (username) select proprietaire from csv_biens group by proprietaire";
			preparedStatement1 = connection.prepareStatement(sql1);
			preparedStatement1.executeUpdate();
			
			String sql2 = "update utilisateur set profil = 'proprio'";
			preparedStatement2 = connection.prepareStatement(sql2);
			preparedStatement2.executeUpdate();
			
			String sql3 = "insert into type_biens (nom) select type from csv_biens group by type";
			preparedStatement3 = connection.prepareStatement(sql3);
			preparedStatement3.executeUpdate();
			
			String sql4 = "insert into biens (nom, description, region, loyer, idProprio, idType, reference) \r\n" + //
								"    select csv_biens.nom, csv_biens.description, csv_biens.region, csv_biens.loyer_mensuel, u.id as idProprio, t.id as idType, csv_biens.reference from csv_biens\r\n" + //
								"    join utilisateur u on u.username = csv_biens.proprietaire\r\n" + //
								"    join type_biens t on t.nom = csv_biens.type";
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

	public Csv_biens() throws Exception {

	}

	public Csv_biens(String reference, String nom, String description, String type, String region,
			Double loyer_mensuel, String proprietaire) throws Exception {
		setReference(reference.trim());
		setNom(nom.trim());
		setDescription(description.trim());
		setType(type.trim());
		setRegion(region.trim());
		setLoyer_mensuel(loyer_mensuel);
		setProprietaire(proprietaire.trim());
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

	@Getter(columnName = "type")
	public String getType() {
		return this.type;
	}

	@Setter(columnName = "type")
	public void setType(String type) {
		this.type = type;
	}

	@Getter(columnName = "region")
	public String getRegion() {
		return this.region;
	}

	@Setter(columnName = "region")
	public void setRegion(String region) {
		this.region = region;
	}

	@Getter(columnName = "loyer_mensuel")
	public Double getLoyer_mensuel() {
		return this.loyer_mensuel;
	}

	@Setter(columnName = "loyer_mensuel")
	public void setLoyer_mensuel(Double loyer_mensuel) {
		this.loyer_mensuel = loyer_mensuel;
	}

	@Getter(columnName = "proprietaire")
	public String getProprietaire() {
		return this.proprietaire;
	}

	@Setter(columnName = "proprietaire")
	public void setProprietaire(String proprietaire) {
		this.proprietaire = proprietaire;
	}

}
