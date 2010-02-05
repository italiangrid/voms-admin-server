package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.event.EventListener;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.EventMask;

public abstract class BaseNotificationDispatcher implements EventListener {

	private final EventMask mask;
	
	public BaseNotificationDispatcher(EventMask mask) {
		this.mask = mask;
		EventManager.instance().register(this);
	}

	public EventMask getMask() {
	
		return mask;
	}

}
