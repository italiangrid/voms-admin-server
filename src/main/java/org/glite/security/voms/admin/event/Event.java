package org.glite.security.voms.admin.event;

import java.util.Map;


public interface Event {
	
	EventType getType();
	
	String getName();
	
	long getCreationTime();
	long getCompletionTime();
	
	public boolean isCompleted();
	public void setCompleted();
	
}
