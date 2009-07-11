package org.glite.security.voms.admin.notification;



import java.util.Date;

import org.glite.security.voms.admin.common.URLBuilder;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.task.SignAUPTask;

public class SignAUPMessage extends AbstractVelocityNotification {
	
	
	VOMSUser user;
	AUP aup;
	
	public SignAUPMessage(VOMSUser user, AUP aup){
		
		setUser(user);
		setAup(aup);
		
	}
	
	@Override
	protected void buildMessage() {
		
		VOMSConfiguration conf = VOMSConfiguration.instance(); 
        String voName = conf.getVOName();
        
        setSubject("Sign '"+aup.getName()+"' notification for VO '"+conf.getVOName()+"'.");
        
		Date expirationDate = null;
        
		SignAUPTask t = user.getPendingSignAUPTask(aup);
        
		if (t != null)
        	expirationDate = t.getExpiryDate();
        
        
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
