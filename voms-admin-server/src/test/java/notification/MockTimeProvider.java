package notification;

import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.core.tasks.TimeProvider;

public class MockTimeProvider implements TimeProvider {

  long time;

  public MockTimeProvider(long timeVal) {

    time = timeVal;
  }

  @Override
  public long currentTimeMillis() {

    return time;
  }

  public void setTime(long time) {

    this.time = time;
  }

  public void add(TimeUnit unit, int amount) {

    time = time + unit.toMillis(amount);
  }

}
