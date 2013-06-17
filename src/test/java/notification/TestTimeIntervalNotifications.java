package notification;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.notification.InMemoryNotificationTimeStorage;
import org.glite.security.voms.admin.notification.TimeIntervalNotificationStrategy;
import org.glite.security.voms.admin.notification.WarningsBeforeExpirationNotificationStrategy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestTimeIntervalNotifications {

	
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void test() throws InterruptedException {

		RecordingVOMSNotification n = new RecordingVOMSNotification();
		
		TimeIntervalNotificationStrategy s = new TimeIntervalNotificationStrategy(
			new InMemoryNotificationTimeStorage(),
			new MockNotificationService(), 
			10, 
			TimeUnit.SECONDS);
		
		s.sendNotification(n);
		Thread.sleep(2000);
		for (int i=0; i < 10; i++)
			s.sendNotification(n);
		
		Assert.assertTrue(n.getDeliveryAttemptCount() == 1);
		
	}
	
	@Test
	public void testPeriod() throws InterruptedException {

		RecordingVOMSNotification n = new RecordingVOMSNotification();
		
		TimeIntervalNotificationStrategy s = new TimeIntervalNotificationStrategy(
			new InMemoryNotificationTimeStorage(),
			new MockNotificationService(), 
			1, 
			TimeUnit.SECONDS);
				
		for (int i=0; i < 5; i++){
			s.sendNotification(n);
			Thread.sleep(2000);
		}
		
		Assert.assertEquals(5, n.getDeliveryAttemptCount());
		
	}
	
	@Test
	public void testPeriodicWarnings() throws InterruptedException {
		
		long start = System.currentTimeMillis();
		long startPlus30secs = start + TimeUnit.SECONDS.toMillis(30);
		
		RecordingVOMSNotification n = new RecordingVOMSNotification();
		
		WarningsBeforeExpirationNotificationStrategy s = new 
			WarningsBeforeExpirationNotificationStrategy(
				new InMemoryNotificationTimeStorage(),
				new MockNotificationService(),
				new int[]{10,5,1},
				TimeUnit.SECONDS,
				startPlus30secs);
		
		for (;;){		
			long now = System.currentTimeMillis();
		
			if (now > startPlus30secs + 5000)
				break;
			
			s.sendNotification(n);
			Thread.sleep(1000);
		}
		
		Assert.assertEquals(4, n.getDeliveryAttemptCount());
		
		List<Date> sendTimes = n.getSendTimes();
		
		long[] expectedTimes = new long[]{
			startPlus30secs - TimeUnit.SECONDS.toMillis(10),
			startPlus30secs - TimeUnit.SECONDS.toMillis(5),
			startPlus30secs - TimeUnit.SECONDS.toMillis(1)
		};
		
		for (int i=0; i < expectedTimes.length; i++){
			
			long actualTime = sendTimes.get(i+1).getTime(); 
			
			String msg = String.format("Notification dates do not match. " +
					"Expected: %d. Actual: %d. Absdiff: %d\n",
					expectedTimes[i], 
					actualTime,
					Math.abs(expectedTimes[i]-actualTime));
			
			
			Assert.assertTrue(msg,
				timeEquals(expectedTimes[i], 
				actualTime, 
				TimeUnit.SECONDS.toMillis(1)));
		}
	
	}

	private boolean timeEquals(long expected, long actual, long epsilon){
		return Math.abs(expected - actual) < epsilon;
	}
}
