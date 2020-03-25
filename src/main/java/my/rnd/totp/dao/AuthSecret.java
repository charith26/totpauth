package my.rnd.totp.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuthSecret {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String appID;
	private String secret = "";
	
	public AuthSecret() {
	}

	public AuthSecret(String appID, String secret) {
		this.appID = appID;
		this.secret = secret;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
