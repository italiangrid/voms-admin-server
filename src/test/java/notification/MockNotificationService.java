package notification;

import java.util.List;

import org.glite.security.voms.admin.notification.NotificationServiceIF;
import org.glite.security.voms.admin.notification.messages.VOMSNotification;


public class MockNotificationService implements NotificationServiceIF {


	@Override
	public void send(VOMSNotification n) {
		n.send();
	}

	@Override
	public List<Runnable> shutdownNow() {

		// TODO Auto-generated method stub
		return null;
	}

}
