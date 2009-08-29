package org.glite.security.voms.admin.event;

public interface EventListener {

	public EventMask getMask();

	public void fire(Event e);
}
