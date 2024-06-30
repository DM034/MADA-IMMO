package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;

@TableName(database = "immobilier", driver = "postgres", name = "admin", password = "366325", user = "postgres")
public class Admin extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "username")
	String username;
	@ColumnField(columnName = "password")
	String password;
	@ColumnField(columnName = "profil")
	String profil;

	public Login logAdmin() throws Exception {
		Login login = new Login();
		if (username == null || password == null) {
			throw new Exception("Veuillez remplir le mot de passe et/ou le pseudo");
		}
		try {
			Admin admin = (Admin) select("WHERE username='" + username + "' AND password='" + password + "'")
					.getFirst();
			login.setUsername(admin.getUsername());
			login.setProfil(admin.getProfil());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erreur d'authentification");
		}
		return login;
	}

	public Admin() throws Exception {

	}

	public Admin(String username, String password) throws Exception {
		setUsername(username.trim());
		setPassword(password.trim());
	}

	public Admin(String username, String password, String profil) throws Exception {
		setUsername(username.trim());
		setPassword(password.trim());
		setProfil(profil.trim());
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
	public Long getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(Long id) {
		this.id = id;
	}

	@Getter(columnName = "username")
	public String getUsername() {
		return this.username;
	}

	@Setter(columnName = "username")
	public void setUsername(String username) {
		this.username = username;
	}

	@Getter(columnName = "password")
	public String getPassword() {
		return this.password;
	}

	@Setter(columnName = "password")
	public void setPassword(String password) {
		this.password = password;
	}

	@Getter(columnName = "profil")
	public String getProfil() {
		return this.profil;
	}

	@Setter(columnName = "profil")
	public void setProfil(String profil) {
		this.profil = profil;
	}

}
