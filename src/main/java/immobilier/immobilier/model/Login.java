package immobilier.immobilier.model;

public class Login {

	String id;
	String username;
	String profil;

	public Login() throws Exception {

	}

	public Login(String username)throws Exception{
		setUsername(username.trim());
	}

	public Login(String username, String profil)
			throws Exception {
		setUsername(username.trim());
		setProfil(profil.trim());
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfil() {
		return this.profil;
	}

	public void setProfil(String profil) {
		this.profil = profil;
	}

}
