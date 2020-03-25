package my.rnd.totp.util;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

public class AppIDGenerator extends IdentityGenerator {

	private String numberFormat;
	public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
    public static final String NUMBER_FORMAT_DEFAULT = "%d";
	
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		String code = RandomStringUtils.random(4, true,true);
		Serializable ids = super.generate(session, object);
		//Serializable ids = String.format(numberFormat, super.generate(session, object));
		return code+ids;
	}
	
	/*@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		super.configure(StringType.INSTANCE, params, serviceRegistry);
		numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER,
                params, NUMBER_FORMAT_DEFAULT);
	}*/
}
