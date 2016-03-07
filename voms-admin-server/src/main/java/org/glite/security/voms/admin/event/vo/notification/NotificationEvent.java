package org.glite.security.voms.admin.event.vo.notification;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.notification.messages.VOMSNotification;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class NotificationEvent extends VOEvent<VOMSNotification>{

  protected NotificationEvent(VOMSNotification payload) {
    super(payload);
    
  }
  
  
  @Override
  protected void decorateAuditEvent(AuditEvent e) {
  
    VOMSNotification payload = getPayload();
    
    e.addDataPoint("notificationType", payload.getClass().getSimpleName());
    
    int counter = 0;
    
    for (String recipient: payload.getRecipientList()){
      e.addDataPoint(String.format("notificationRecipient%d", counter++), 
        recipient);
    }
    
    e.addDataPoint("notificationSubject", payload.getSubject());
    
    super.decorateAuditEvent(e);
  }
  

}
