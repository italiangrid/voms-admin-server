package org.glite.security.voms.admin.notification.dispatchers;

import java.util.EnumSet;

import org.glite.security.voms.admin.event.AbstractEventLister;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.NotificationUtil;
import org.glite.security.voms.admin.notification.messages.AdminTargetedUserSuspensionMessage;
import org.glite.security.voms.admin.notification.messages.UserTargetedUserSuspensionMessage;

public class UserSuspendedDispatcher extends
  AbstractEventLister<UserSuspendedEvent> {

  public UserSuspendedDispatcher() {

    super(EnumSet.of(EventCategory.UserLifecycleEvent),
      UserSuspendedEvent.class);
  }

  @Override
  protected void doFire(UserSuspendedEvent e) {

    AdminTargetedUserSuspensionMessage msg = new AdminTargetedUserSuspensionMessage(
      e.getPayload(), e.getReason().getMessage());

    UserTargetedUserSuspensionMessage usrMsg = new UserTargetedUserSuspensionMessage(
      e.getPayload(), e.getReason().getMessage());

    msg.addRecipients(NotificationUtil.getAdministratorsEmailList());
    usrMsg.addRecipient(e.getPayload().getEmailAddress());

    NotificationService.instance().send(msg);
    NotificationService.instance().send(usrMsg);
  }

  public static UserSuspendedDispatcher instance(){
    return new UserSuspendedDispatcher();
  }
}
