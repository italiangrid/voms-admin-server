package org.glite.security.voms.admin.view.actions.dev;

import java.util.UUID;

import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.notification.NotificationServiceFactory;
import org.glite.security.voms.admin.notification.messages.TestNotification;
import org.glite.security.voms.admin.view.actions.BaseAction;


@Result(name = BaseAction.SUCCESS, location = "/home/login.action",
type = "redirect")
public class SendTestNotificationAction extends BaseDevAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  

  @Override
  public String execute() throws Exception {
  
    TestNotification ts = new TestNotification();
    
    ts.setMessage("This is a test");
    ts.addRecipient("test@dev.local.io");
    ts.setSubject("Test message "+UUID.randomUUID().toString());
    
    NotificationServiceFactory.getNotificationService().send(ts);
    
    return SUCCESS;
  }

}
