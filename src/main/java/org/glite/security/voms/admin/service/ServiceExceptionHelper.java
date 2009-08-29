package org.glite.security.voms.admin.service;

import org.apache.commons.logging.Log;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.hibernate.JDBCException;

public class ServiceExceptionHelper {

	public static void handleServiceException(Log log, RuntimeException t) {

		// If the exception is a DB exception, clean out the session gracefully
		if (t instanceof JDBCException) {

			Throwable cause = t.getCause();

			log.error("Database error caught: " + cause.getMessage());

			// Print stack trace in logs if log is debug enabled.
			if (log.isDebugEnabled())
				log.error("Database error caught: " + cause.getMessage(), t);

			try {

				HibernateFactory.rollbackTransaction();

			} finally {

				HibernateFactory.closeSession();

			}

		} else {

			if (log.isDebugEnabled())
				log.error(t.getClass().getSimpleName() + " exception caught: ",
						t);
			else
				log.error(t.getClass().getSimpleName() + " exception caught: "
						+ t.getMessage());
		}

		throw t;
	}

}
