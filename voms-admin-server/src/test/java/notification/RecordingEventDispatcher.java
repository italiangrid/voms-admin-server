package notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.core.tasks.TimeProvider;
import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventDispatcher;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskReminderEvent;

public class RecordingEventDispatcher implements EventDispatcher {

  final List<Event> recordedEvents = new ArrayList<Event>();
  final TimeProvider time;
  
  public RecordingEventDispatcher(TimeProvider timeProvider) {
    time =  timeProvider;
  }

  @Override
  public void dispatch(Event e) {
    recordedEvents.add(e);
    if (e instanceof SignAUPTaskReminderEvent){
      SignAUPTaskReminderEvent ee = (SignAUPTaskReminderEvent)e;
      ee.getTask().setLastNotificationTime(new Date(time.currentTimeMillis()));
    }
  }

  public List<Event> getRecordedEvents() {

    return recordedEvents;
  }

}
