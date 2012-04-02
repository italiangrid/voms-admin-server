package org.glite.security.voms.admin.core.validation;

import java.util.Calendar;
import java.util.Date;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;

public class ValidationUtil {

	
	public static Date membershipExpirationDateStartingFromNow(){
		
		Calendar cal = Calendar.getInstance();
		VOMSConfiguration conf = VOMSConfiguration.instance();
		
		int defaultMembershipPeriod = conf.getInt(VOMSConfigurationConstants.DEFAULT_MEMBERSHIP_LIFETIME, 12);
		
		cal.add(Calendar.MONTH, defaultMembershipPeriod);
		
		return cal.getTime();
		
	}
}
