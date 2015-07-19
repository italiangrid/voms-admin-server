package org.glite.security.voms.admin.notification.dispatchers;

import java.util.Date;
import java.util.EnumSet;

import org.glite.security.voms.admin.event.AbstractEventLister;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskReminderEvent;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.messages.SignAUPReminderMessage;

public class SignAUPReminderDispatcher extends
  AbstractEventLister<SignAUPTaskReminderEvent> {

  public SignAUPReminderDispatcher() {

    super(EnumSet.of(EventCategory.UserLifecycleEvent),
      SignAUPTaskReminderEvent.class);

  }

  @Override
  protected void doFire(SignAUPTaskReminderEvent e) {

    SignAUPReminderMessage msg = new SignAUPReminderMessage(e);
    NotificationService.instance().send(msg);
    
    e.getTask().setLastNotificationTime(new Date());
    
    // HibernateFactory.getSession().saveOrUpdate(e.getTask());
    
  }

}
