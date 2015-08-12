package org.glite.security.voms.admin.notification.messages;

import java.util.Date;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskReminderEvent;
import org.glite.security.voms.admin.util.URLBuilder;


public class SignAUPReminderMessage extends AbstractVelocityNotification{

  final SignAUPTaskReminderEvent event;
  
  public SignAUPReminderMessage(SignAUPTaskReminderEvent e) {
    event = e;
    getRecipientList().add(e.getPayload().getEmailAddress());
  }
  
  @Override
  protected void buildMessage() {
  
    VOMSConfiguration conf = VOMSConfiguration.instance();
    String voName = conf.getVOName();

    String theSubject = 
      String.format("Request to sign VO %s acceptable usage policy (AUP).", 
        conf.getVOName());
    
    setSubject(theSubject);
    
    Date expirationDate = event.getTask().getExpiryDate();
    
    context.put("voName", voName);
    context.put("aup", event.getAup());
    context.put("user", event.getTask().getUser());
    context.put("recipient", getRecipientList().get(0));
    
    context.put("signAUPURL", URLBuilder.baseVOMSURLFromConfiguration()
      + "/aup/sign!input.action?aupId=" + event.getAup().getId());
    
    context.put("expirationDate", expirationDate);
    
    super.buildMessage();
  }

}
