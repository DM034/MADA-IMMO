package immobilier.immobilier.model;

import immobilier.immobilier.annotation.ColumnField;
import immobilier.immobilier.annotation.Getter;
import immobilier.immobilier.annotation.Setter;
import immobilier.immobilier.annotation.TableName;
import immobilier.immobilier.table.JDBC;
import java.sql.Connection;
import java.util.LinkedList;

@TableName(database = "immobilier", driver = "postgres", name = "utilisateur", password = "366325", user = "postgres")
public class Utilisateur extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "username")
	String username;
	@ColumnField(columnName = "profil")
	String profil;

	public Login log() throws Exception {
		Login login = new Login();
		if (username == null) {
			throw new Exception("Veuillez remplir votre username");
		}
		try {
			Utilisateur cli = (Utilisateur) select("WHERE username='" + username + "'")
					.getFirst();
			login.setUsername(cli.getUsername());
			login.setProfil(cli.getProfil());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erreur d'authentification");
		}
		return login;
	}

	public Utilisateur getIdUtilisateurByUsername(String username) throws Exception {
		Utilisateur eq = (Utilisateur) new Utilisateur().select("where username = '"+username+"'").getFirst();
		return eq;
	}

	public LinkedList<Utilisateur> getAllClient()throws Exception{
		LinkedList<Utilisateur> bs = new Utilisateur().select("where profil = 'user'");
		return bs;
	}

	public Utilisateur() throws Exception {

	}

	public Utilisateur(String username) throws Exception {
		setUsername(username.trim());
	}

	public Utilisateur(String username, String profil) throws Exception {
		setUsername(username.trim());
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
	public String getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(String id) {
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

	@Getter(columnName = "profil")
	public String getProfil() {
		return this.profil;
	}

	@Setter(columnName = "profil")
	public void setProfil(String profil) {
		this.profil = profil;
	}

}
