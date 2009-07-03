package org.glite.security.voms.admin.notification;



import java.util.Date;

import org.apache.velocity.VelocityContext;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.tasks.URLBuilder;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;

public class SignAUPMessage extends VelocityEmailNotification {

	public static final String templateFile = SignAUPMessage.class.getSimpleName()+".vm";
	
	VOMSUser user;
	AUP aup;
	
	public SignAUPMessage(VOMSUser u, AUP aup){
		setTemplateFile(templateFile);
		setUser(u);
		setAup(aup);
		
		
	}
	@Override
	protected void buildMessage() {
		
		VOMSConfiguration conf = VOMSConfiguration.instance(); 
        String voName = conf.getVOName();
        
		Date expirationDate = null;
        
        if (user.hasSignAUPTaskPending(aup))
        	expirationDate = user.getPendingSignAUPTask(aup).getExpiryDate();
        	
        setSubject(subjectPrefix+" Sign AUP request notification");
        
        VelocityContext context = new VelocityContext();
        context.put( "voName", voName );
        context.put( "aup", aup);
        context.put("user", getUser());
        context.put( "recipient", getRecipientList().get(0));
        context.put("signAUPURL", URLBuilder.baseVOMSURL()+"/aup/sign!input.action?aupId="+aup.getId());
        context.put("expirationDate", expirationDate);
        
        
        buildMessageFromTemplate(context);

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
