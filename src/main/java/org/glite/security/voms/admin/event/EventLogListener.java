package org.glite.security.voms.admin.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventLogListener implements EventListener {

	public static final Log log = LogFactory.getLog(EventLogListener.class);
	
	public EventLogListener() {
		
		
		
	}
	
	
	public void fire(Event e) {
		log.debug("Event received: "+e);
	}

	public EventMask getMask() {
		
		return null;
	}

}
