package org.glite.security.voms.admin.common.tasks;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadUncaughtExceptionHandler implements UncaughtExceptionHandler {
	public static final Log log = LogFactory
			.getLog(ThreadUncaughtExceptionHandler.class);

	public void uncaughtException(Thread t, Throwable e) {

		String errorMessage = String.format(
				"Thread (%d - '%s') uncaught exception: %s at line %d pf %s%n",
				t.getId(), t.getName(), e.toString(), e.getStackTrace()[0]
						.getLineNumber(), e.getStackTrace()[0].getFileName());

		log.error(errorMessage, e);

	}

}
