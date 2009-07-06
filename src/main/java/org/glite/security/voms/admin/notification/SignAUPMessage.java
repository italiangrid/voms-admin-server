package org.glite.security.voms.admin.notification;



import java.util.Date;

import org.glite.security.voms.admin.common.URLBuilder;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;

public class SignAUPMessage extends AbstractVelocityNotification {
	
	
	VOMSUser user;
	AUP aup;
	
	public SignAUPMessage(VOMSUser user, AUP aup){
		
		setUser(user);
		setAup(aup);
		
	}
	
	@Override
	protected void buildMessage() {
		
		setSubject(subjectPrefix+" Sign AUP notification");
		
		VOMSConfiguration conf = VOMSConfiguration.instance(); 
        String voName = conf.getVOName();
        
		Date expirationDate = null;
        
        if (user.hasSignAUPTaskPending(aup))
        	expirationDate = user.getPendingSignAUPTask(aup).getExpiryDate();
        
        
        context.put( "voName", voName );
        context.put( "aup", aup);
        context.put("user", user);
        context.put( "recipient", getRecipientList().get(0));
        context.put("signAUPURL", URLBuilder.baseVOMSURL()+"/aup/sign!input.action?aupId="+aup.getId());
        context.put("expirationDate", expirationDate);
		
        super.buildMessage();
	}

	public VOMSUser getUser() {
		return user;
	}

	public void setUser(VOMSUser user) {
		this.user = user;
	}

	public AUP getAup() {
		return aup;
	}

	public void setAup(AUP aup) {
		this.aup = aup;
	}
	
	
	
}
