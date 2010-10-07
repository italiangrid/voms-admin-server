package org.glite.security.voms.admin.notification.messages;

import java.util.Date;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class MembershipExpirationWarningMessage extends
	AbstractVelocityNotification {

    VOMSUser user;
    Date expirationDate;
    
    public MembershipExpirationWarningMessage(VOMSUser u, Date expirationDate) {
	this.user = u;
	this.expirationDate = expirationDate;
    }
    
    @Override
    protected void buildMessage() {
	
	VOMSConfiguration conf = VOMSConfiguration.instance();
	String voName = conf.getVOName();
	
	setSubject(String.format("VO %s Membership expiration warning", voName));
	context.put("voName", voName);
	context.put("recipient", getRecipientList().get(0));
	
	Date realExpirationDate = user.getEndTime();
	if (expirationDate != null)
	    realExpirationDate = expirationDate;
	
	context.put("expirationDate", realExpirationDate);
	
        super.buildMessage();
    }

}
