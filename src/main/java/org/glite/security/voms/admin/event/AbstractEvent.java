package org.glite.security.voms.admin.event;

import java.util.HashMap;
import java.util.Map;


public class AbstractEvent implements Event {

	EventType type;
	
	long creationTime;
	long completionTime;
	boolean completed = false;
	
	String name;
	
	HashMap<String, Object> payload;
	
	
	public AbstractEvent(EventType type) {
		this.type = type;
		creationTime = System.currentTimeMillis();
		payload = new HashMap<String, Object>();
	}
	
	public long getCompletionTime() {

		return completionTime;
	}

	public long getCreationTime() {
		
		return creationTime;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted() {
		
		completed = true;
		completionTime = System.currentTimeMillis();
		
	}

	public String getName() {
		
		if (name == null)
			return this.getClass().getSimpleName();
		
		return name;
	}
	
	protected void setOperation(String op){
		
		name = op;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
