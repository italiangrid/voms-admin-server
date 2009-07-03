package org.glite.security.voms.admin.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogListener implements EventListener {

	public static final Log log = LogFactory.getLog(LogListener.class);
	
	public LogListener() {
		
	}
	
	
	public void fire(Event e) {
		log.info("Event received: "+e);
	}

	public EventMask getMask() {
		
		return null;
	}

}
