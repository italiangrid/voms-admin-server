package org.glite.security.voms.admin.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestEvents implements EventGenerator{
	
	private static Log log = LogFactory.getLog(TestEvents.class);
	
	private EventManager manager;
	public TestEvents() {
	
		manager = EventManager.instance();
		
		register(new LogListener());
		register(new LogListener());
		
		
		
	}
	
	public static void main(String[] args) {
		
		new TestEvents();
	}

	public void register(EventListener listener) {
		manager.register(listener);
		
	}

	public void setEventManager(EventManager manager) {
		this.manager = manager;
		
	}

	public void unRegister(EventListener listener) {
		manager.unRegister(listener);
		
	}

}
