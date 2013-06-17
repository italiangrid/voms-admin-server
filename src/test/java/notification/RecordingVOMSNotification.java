package notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.notification.messages.VOMSNotification;


public class RecordingVOMSNotification implements VOMSNotification {

	private List<Date> sendTimes = new ArrayList<Date>();
	
	public RecordingVOMSNotification() {

	}

	@Override
	public void send() {

		sendTimes.add(new Date());
	}

	@Override
	public int getDeliveryAttemptCount() {

		return sendTimes.size();
	}

	public List<Date> getSendTimes(){
		return sendTimes;
	}
	
	
}
