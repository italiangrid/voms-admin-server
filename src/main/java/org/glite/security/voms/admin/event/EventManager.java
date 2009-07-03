package org.glite.security.voms.admin.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSException;

public class EventManager {

	public static final Log log = LogFactory.getLog(EventManager.class);
	
	final List<EventListener> listeners = new ArrayList<EventListener>();
	
	static private EventManager instance = null;
	
	
	public static final EventManager instance(){
		if (instance == null)
			instance = new EventManager();
		return instance;
	}
	
	private EventManager() {
		
	}
	
	public void register(EventListener listener){
		
		listeners.add(listener);
	}
	
	public void unRegister(EventListener listener){
		
		listeners.remove(listener);
	}

	public List<EventListener> getListeners() {
		return listeners;
	}
	
	
	public void fireEvent(Event e){
		try{
		for (EventListener l: getListeners()){
			
			if (l.getMask() == null || l.getMask().get(e.getType().bitNo))
				l.fire(e);
		}
		}catch (Throwable t) {
			log.error("Error dispatching event '"+e+"': "+t.getMessage());
			if (log.isDebugEnabled())
				log.error("Error dispatching event '"+e+"': "+t.getMessage(),t);
		}
	}
	
	public static void dispatch(Event e){
		if (instance == null)
			log.error("Event manager has not been initialized! The event will be lost...");
		else
			instance.fireEvent(e);	
	}
}
