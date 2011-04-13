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
package org.glite.security.voms.admin.core.tasks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskTypeDAO;
import org.glite.security.voms.admin.persistence.error.VOMSInconsistentDatabaseException;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSSeqNumber;
import org.glite.security.voms.admin.persistence.model.task.TaskType;
import org.glite.security.voms.admin.util.DNUtil;

/**
 * 
 * @author andrea
 * 
 */
public class DatabaseSetupTask extends TimerTask {

	private static final Logger log = LoggerFactory.getLogger(DatabaseSetupTask.class);

	private Timer timer;

	private static DatabaseSetupTask instance = null;

	public static DatabaseSetupTask instance() {
		return instance(null);
	}

	public static DatabaseSetupTask instance(Timer t) {

		if (instance == null)
			instance = new DatabaseSetupTask(t);
		return instance;

	}

	private DatabaseSetupTask(Timer timer) {

		this.timer = timer;
	}

	private void setupRootGroup() {

		try{
			
			VOMSGroupDAO.instance().getVOGroup();
		
		}catch(VOMSInconsistentDatabaseException ex){
			log.info("Setting up VO root group...");
			VOMSGroupDAO.instance().createVOGroup();
			
		}
	}

	private void setupInternalCAs() {

		log.info("Setting up voms-admin internal CAs..");

		VOMSCADAO caDAO = VOMSCADAO.instance();

		caDAO
				.createIfMissing(VOMSServiceConstants.VIRTUAL_CA,
						"A dummy CA for local org.glite.security.voms.admin.persistence.error mainteneance");
		caDAO.createIfMissing(VOMSServiceConstants.GROUP_CA,
				"A virtual CA for VOMS groups.");
		caDAO.createIfMissing(VOMSServiceConstants.ROLE_CA,
				"A virtual CA for VOMS roles.");
		caDAO.createIfMissing(VOMSServiceConstants.AUTHZMANAGER_ATTRIBUTE_CA,
				"A virtual CA for authz manager attributes");

	}

	private void setupInternalAdmins() {

		List admins = VOMSAdminDAO.instance().getAll();

		if (admins.isEmpty()) {

			log.info("Setting up voms-admin internal administrators...");
			
			VOMSGroup voGroup = VOMSGroupDAO.instance().getVOGroup();
			if (voGroup == null)
				setupRootGroup();

			VOMSAdminDAO adminDAO = VOMSAdminDAO.instance();

			VOMSAdmin internalAdmin = adminDAO.create(
					VOMSServiceConstants.INTERNAL_ADMIN,
					VOMSServiceConstants.VIRTUAL_CA);

			VOMSAdmin localAdmin = adminDAO.create(
					VOMSServiceConstants.LOCAL_ADMIN,
					VOMSServiceConstants.VIRTUAL_CA);

			adminDAO.create(VOMSServiceConstants.PUBLIC_ADMIN,
					VOMSServiceConstants.VIRTUAL_CA);

			adminDAO.create(VOMSServiceConstants.ANYUSER_ADMIN,
					VOMSServiceConstants.VIRTUAL_CA);
			
			adminDAO.create(VOMSServiceConstants.UNAUTHENTICATED_CLIENT,
					VOMSServiceConstants.VIRTUAL_CA);

			VOMSPermission allPermissions = VOMSPermission.getAllPermissions();
			

			ACL voGroupACL = new ACL(voGroup, false);
			voGroup.getAcls().add(voGroupACL);

			voGroupACL.setPermissions(localAdmin, allPermissions);
			voGroupACL.setPermissions(internalAdmin, allPermissions);

			// Create VO-Admin role and admin

			VOMSRole voAdminRole = VOMSRoleDAO.instance().create("VO-Admin");

			VOMSAdmin voAdmin = VOMSAdminDAO.instance().create(
					voGroup.getName() + "/Role=VO-Admin");

			voGroupACL.setPermissions(voAdmin, allPermissions);

			VOMSSeqNumber seqNum = new VOMSSeqNumber();
			seqNum.setSeq("0");
			HibernateFactory.getSession().save(seqNum);

			// Trusted admin creation

			String trustedAdminDn = VOMSConfiguration.instance().getString(
					"voms.trusted.admin.subject");

			if (trustedAdminDn == null) {

				voAdminRole.importACL(voGroup);
				HibernateFactory.commitTransaction();
				return;
			}

			String trustedAdminCa = VOMSConfiguration.instance().getString(
					"voms.trusted.admin.ca");

			if (trustedAdminCa == null) {
				log
						.error("Missing \"voms.trusted.admin.ca\" configuration parameter. Skipping creation of the trusted admin...");
				return;
			}

			VOMSCA ca = VOMSCADAO.instance().getByName(trustedAdminCa);

			if (ca == null) {

				log
						.error("Trusted admin ca \""
								+ trustedAdminCa
								+ "\" not found in database. Skipping creation of the trusted admin...");
				return;
			}

			VOMSAdmin trustedAdmin = VOMSAdminDAO.instance().getByName(
					trustedAdminDn, ca.getSubjectString());

			if (trustedAdmin == null) {

				String emailAddress = DNUtil
						.getEmailAddressFromDN(trustedAdminDn);

				// Get default email address from voms service configuration
				if (emailAddress == null)
					emailAddress = VOMSConfiguration.instance().getString(
							"voms.notification.email-address");

				trustedAdmin = VOMSAdminDAO.instance().create(trustedAdminDn,
						ca.getSubjectString(), emailAddress);

			}

			voGroupACL.setPermissions(trustedAdmin, allPermissions);

			log.info("Trusted admin created.");

			if (VOMSConfiguration.instance().getBoolean(
					VOMSConfigurationConstants.READ_ACCESS_FOR_AUTHENTICATED_CLIENTS,
					false)) {

				// Grant read-only access to authenticated clients
				VOMSAdmin anyUserAdmin = VOMSAdminDAO.instance()
						.getAnyAuthenticatedUserAdmin();
				VOMSPermission readOnlyPerms = VOMSPermission
						.getEmptyPermissions().setContainerReadPermission()
						.setMembershipReadPermission();
				voGroupACL.setPermissions(anyUserAdmin, readOnlyPerms);

			}

			// Import ACL *after* trusted and anyuser admins have been created!
			voAdminRole.importACL(voGroup);
		}
	}

	public void setupTasks() {

		TaskTypeDAO ttDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
				.getTaskTypeDAO();

		if (ttDAO.findAll().isEmpty()) {
			
			log.info("Setting up voms-admin 2.5 task infrastructure...");

			TaskType signAupTaskType = new TaskType();

			signAupTaskType.setName("SignAUPTask");
			signAupTaskType
					.setDescription("Tasks of this type are assigned to users that need to sign, or resign an AUP.");

			TaskType approveUserRequestTaskType = new TaskType();
			approveUserRequestTaskType.setName("ApproveUserRequestTask");
			approveUserRequestTaskType
					.setDescription("Tasks of this type are assigned to VO admins that need to approve users' requests.");

			ttDAO.makePersistent(signAupTaskType);
			ttDAO.makePersistent(approveUserRequestTaskType);

		}

	}

	public void setupAUP() {

		AUPDAO dao = DAOFactory.instance().getAUPDAO();

		if (dao.findAll().isEmpty()) {

			log.info("Setting up voms-admin 2.5 aup infrastructure...");
			// Setup VO AUP
			String voAUPUrlString = VOMSConfiguration.instance().getString(
					VOMSConfigurationConstants.VO_AUP_URL,
					VOMSConfiguration.instance().getDefaultVOAUPURL());

			if (voAUPUrlString.trim().equals("")) {
				log.warn("No url defined for VO AUP, using default setting...");
				voAUPUrlString = VOMSConfiguration.instance()
						.getDefaultVOAUPURL();
			}

			try {

				URL voAUPURL = new URL(voAUPUrlString);

				AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();

				aupDAO.createVOAUP("", "1.0", voAUPURL);

			} catch (MalformedURLException e) {
				log.error("Error parsing AUP url: " + e.getMessage());
				log.error("Skipping creation of AUPs");
			}
		}

	}

	
	public void run() {

		setupRootGroup();
		setupInternalCAs();
		setupInternalAdmins();
		
		VOMSVersionDAO.instance().setupVersion();

		setupTasks();
		setupAUP();

		HibernateFactory.commitTransaction();

	}

}
