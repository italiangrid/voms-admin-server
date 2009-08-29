package org.glite.security.voms.admin.task;

public class AUPTaskHandler implements AUPTaskEventListener {

	private static AUPTaskHandler instance = null;

	private AUPTaskHandler() {

	}

	public static AUPTaskHandler instance() {
		if (instance == null)
			instance = new AUPTaskHandler();

		return instance;

	}

	public void signAUPTaskAssigned(AUPTaskEventContext e) {

	}

	public void signAUPTaskCompleted(AUPTaskEventContext e) {

	}

	public void signAUPTaskExpired(AUPTaskEventContext e) {

	}

}
