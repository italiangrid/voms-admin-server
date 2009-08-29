package org.glite.security.voms.admin.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSConfiguration;

public class SiblingsContextListener implements ServletContextListener {

	private static final Log log = LogFactory
			.getLog(SiblingsContextListener.class);

	public void contextDestroyed(ServletContextEvent ev) {
		// Release commons logging
		// see
		// http://wiki.apache.org/jakarta-commons/Logging/FrequentlyAskedQuestions
		// and
		// http://issues.apache.org/bugzilla/show_bug.cgi?id=26372#c15

		java.beans.Introspector.flushCaches();
		LogFactory.release(Thread.currentThread().getContextClassLoader());
		java.beans.Introspector.flushCaches();

	}

	public void contextInitialized(ServletContextEvent ev) {

		VOMSConfiguration.instance(ev.getServletContext());
		log.info("Siblings context initialized.");

	}

}
