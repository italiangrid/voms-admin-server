package org.glite.security.voms.admin.core.tasks;

public enum SystemTimeProvider implements TimeProvider {

  INSTANCE;

  @Override
  public long currentTimeMillis() {

    return System.currentTimeMillis();
  }

}
