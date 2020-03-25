package my.rnd.totp.api;

import java.util.Hashtable;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import my.rnd.totp.ctrl.AppRepository;
import my.rnd.totp.dao.AuthResponse;
import my.rnd.totp.dao.AuthSecret;

@RestController
@RequestMapping (path = "/totp")
public class TotpAPI {
	
	private DefaultSecretGenerator secretGenerator;
	//private Hashtable<String, String> apps;
	private SystemTimeProvider timeProvider;
	private DefaultCodeGenerator codeGenerator;
	private DefaultCodeVerifier verifier;
	@Autowired
	private AppRepository appRepo;

	public TotpAPI() {
		secretGenerator = new DefaultSecretGenerator();
		//apps = new Hashtable<String, String>();
		
		timeProvider = new SystemTimeProvider();
		codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA512);
		verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
		verifier.setTimePeriod(30);
		verifier.setAllowedTimePeriodDiscrepancy(1);
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Started");
	}
	
	@GetMapping(path="/auth", produces = "application/json")
    public AuthSecret generateAuthCode() {
		String appID = generateAppID();
		
		/**
		 * This is a horrible way to generate an App ID, using it because i'm lazy
		 */
		while(appRepo.findByAppID(appID)!=null) 
			appID = generateAppID();
		
		String secret = secretGenerator.generate();
		//apps.put(appID, secret);
		AuthSecret authSecret = new AuthSecret(appID, secret);
		appRepo.save(authSecret);
		return authSecret;
	}
	
	@GetMapping(path="/api", produces = "application/json")
    public AuthResponse getInfoAPI(@RequestHeader("totp") String totp, @RequestHeader("appID") String appID) {
		if(validateAPI(appID, totp))
			return new AuthResponse(200, "Valid");
		else
			return new AuthResponse(401, "Unauthorized");
	}
	
	private boolean validateAPI(String appID, String totp) {
		AuthSecret authSecret = appRepo.findByAppID(appID);
		if(authSecret == null)
			return false;
		else
			return verifier.isValidCode(authSecret.getSecret(), totp);
	}
	
	/**
	 * 
	 * @return
	 */
	private String generateAppID() {
		return RandomStringUtils.random(6, true,true);
	}
}
