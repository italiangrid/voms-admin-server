package org.glite.security.voms.admin.event;

public class GenericEvent extends AbstractEvent {

	public GenericEvent(EventType type) {
		super(type);

	}

	@Override
	public String toString() {
		return String.format("Event[type:%s, name:%s]", getType(), getName());
	}

}
