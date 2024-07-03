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

@TableName(database = "immobilier", driver = "postgres", name = "csv_commission", password = "366325", user = "postgres")
public class Csv_commission extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "type")
	String type;
	@ColumnField(columnName = "commission")
	String commission;
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
			Csv_commission csv_modele = new Csv_commission();
			numero = numero + 1;
			setLigne(numero);
			csv_modele.setType(data[0]);
			String desc = data[1].replace("%", "").replace(",", ".");
			csv_modele.setCommission(desc);


			// if (erreurs.isEmpty()) {
			csv_modele.insert(connection);
			// } else {
			// csv_modele.saveErreur(connection);
			// }
		}
		PreparedStatement preparedStatement1 = null;
		// PreparedStatement preparedStatement2 = null;
		// PreparedStatement preparedStatement3 = null;
		// // PreparedStatement preparedStatement4 = null;

		try {
			String sql1 = "insert into commission (idType, valeur) \r\n" + //
								"    select type_biens.id, commission from csv_commission\r\n" + //
								"    join type_biens on type_biens.nom = csv_commission.type";
			preparedStatement1 = connection.prepareStatement(sql1);
			preparedStatement1.executeUpdate();
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			Connector.CloseStatement(preparedStatement1);
		}

	}

	public Csv_commission() throws Exception {

	}

	public Csv_commission(String type, String commission) throws Exception {
		setType(type.trim());
		setCommission(commission);
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

	@Getter(columnName = "type")
	public String getType() {
		return this.type;
	}

	@Setter(columnName = "type")
	public void setType(String type) {
		this.type = type;
	}

	@Getter(columnName = "commission")
	public String getCommission() {
		return this.commission;
	}

	@Setter(columnName = "commission")
	public void setCommission(String commission) {
		this.commission = commission;
	}

}
