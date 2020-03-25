package my.rnd.totp.ctrl;

import org.springframework.data.repository.CrudRepository;

import my.rnd.totp.dao.AuthSecret;

public interface AppRepository extends CrudRepository<AuthSecret, String> {
	
	AuthSecret findByAppID(String id);
}
