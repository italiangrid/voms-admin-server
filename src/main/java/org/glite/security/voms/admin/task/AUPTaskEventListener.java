package org.glite.security.voms.admin.task;

public interface AUPTaskEventListener {

	public void signAUPTaskAssigned(AUPTaskEventContext e);

	public void signAUPTaskCompleted(AUPTaskEventContext e);

	public void signAUPTaskExpired(AUPTaskEventContext e);

}
