/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.common;

import it.infn.cnaf.voms.x509.X509ACGenerator;

import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.glite.security.voms.admin.common.tasks.MembershipValidityCheckTask;
import org.glite.security.voms.admin.common.tasks.ExpiredRequestsPurgerTask;
import org.glite.security.voms.admin.common.tasks.TaskStatusUpdater;
import org.glite.security.voms.admin.common.tasks.ThreadUncaughtExceptionHandler;
import org.glite.security.voms.admin.common.tasks.UpdateCATask;
import org.glite.security.voms.admin.dao.VOMSVersionDAO;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.database.VOMSDatabaseException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.DebugEventLogListener;
import org.glite.security.voms.admin.model.VOMSDBVersion;
import org.glite.security.voms.admin.notification.GroupMembershipNotificationDispatcher;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.DefaultNotificationDispatcher;
import org.glite.security.voms.admin.notification.RoleMembershipNotificationDispatcher;
import org.glite.security.voms.admin.notification.VOMembershipNotificationDispatcher;
import org.opensaml.xml.ConfigurationException;

public final class VOMSService {

	static final Log log = LogFactory.getLog(VOMSService.class);

	static final Timer vomsTimer = new Timer(true);

	protected static void checkDatabaseVersion() {

		int version = VOMSVersionDAO.instance().getVersion();

		if (version != VOMSServiceConstants.VOMS_DB_VERSION) {

			if (version < VOMSServiceConstants.VOMS_DB_VERSION) {
				log
						.fatal("VOMS DATABASE SCHEMA ERROR: old voms database schema found: version "
								+ version);
				log
						.fatal("PLEASE UPGRADE TO CURRENT VERSION, usign voms-admin-configure or voms-db-deploy.py commands!");
				throw new VOMSFatalException(
						"INCOMPATIBLE DATABASE SCHEMA FOUND! Is '" + version
								+ "', while it should be '"
								+ VOMSServiceConstants.VOMS_DB_VERSION + "'");
			} else {

				log
						.fatal("UNKNOWN SCHEMA VERSION NUMBER FOUND IN DATABASE! version: "
								+ version);
				throw new VOMSFatalException(
						"INCOMPATIBLE DATABASE SCHEMA FOUND! Is '" + version
								+ "', while it should be '"
								+ VOMSServiceConstants.VOMS_DB_VERSION + "'");
			}
		}
	}

	protected static void configureVelocity() {

		try {

			Properties p = new Properties();

			p.put("resource.loader", "cpath");
			p
					.put("cpath.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			p.put("runtime.log.logsystem.class",
					"org.glite.security.voms.admin.velocity.VelocityLogger");

			Velocity.init(p);
			log.info("Velocity setup ok!");

		} catch (Exception e) {

			log.error("Error initializing velocity template engine!");
			throw new VOMSFatalException(e);
		}

	}

	protected static void configureEventManager() {

		EventManager.instance();
		

		DebugEventLogListener.instance();
		
		DefaultNotificationDispatcher.instance();
		
		GroupMembershipNotificationDispatcher.instance();
		
		RoleMembershipNotificationDispatcher.instance();
		
		VOMembershipNotificationDispatcher.instance();
	}

	protected static void startBackgroundTasks() {

		UpdateCATask.instance(getTimer());

		ExpiredRequestsPurgerTask.instance(getTimer());

		TaskStatusUpdater.instance(getTimer());

		MembershipValidityCheckTask.instance(getTimer());

	}

	protected static void bootstrapAttributeAuthorityServices() {

		VOMSConfiguration conf = VOMSConfiguration.instance();

		// Bootstrap VOMS SAML attribute authority

		try {
			org.opensaml.DefaultBootstrap.bootstrap();

		} catch (ConfigurationException e) {

			log.error("Error initializing OpenSAML:" + e.getMessage());

			if (log.isDebugEnabled())
				log.error("Error initializing OpenSAML:" + e.getMessage(), e);

			log.info("SAML endpoint will not be activated.");
			conf.setProperty(VOMSConfiguration.VOMS_AA_SAML_ACTIVATE_ENDPOINT,
					false);
		}
		
		X509ACGenerator.instance(conf.getServiceCertificate(), conf.getServicePrivateKey());

	}

	public static void start(ServletContext ctxt) {

		Thread
				.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());

		VOMSConfiguration conf;

		try {

			conf = VOMSConfiguration.instance(ctxt);

		} catch (VOMSConfigurationException e) {
			log.fatal("VOMS-Admin configuration failed!", e);
			throw new VOMSFatalException(e);
		}

		log.info("VOMS-Admin starting for vo:" + conf.getVOName());

		checkDatabaseVersion();

		configureVelocity();

		configureEventManager();

		startBackgroundTasks();

		bootstrapAttributeAuthorityServices();

		log.info("VOMS-Admin started succesfully.");
	}

	public static void stop() {

		getTimer().cancel();

		NotificationService.instance().stop();

		// Close hibernate session factory
		HibernateFactory.getFactory().close();

		log.info("VOMS admin stopped .");
	}

	public static Timer getTimer() {

		return vomsTimer;
	}
}
