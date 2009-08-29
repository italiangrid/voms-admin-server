package org.glite.security.voms.admin.event;

public interface EventGenerator {

	public void register(EventListener listener);

	public void unRegister(EventListener listener);

}
