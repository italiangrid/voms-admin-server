/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.core.tasks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.event.auditing.AuditLogHelper;
import org.glite.security.voms.admin.event.vo.acl.ACLCreatedEvent;
import org.glite.security.voms.admin.event.vo.admin.AdminCreatedEvent;
import org.glite.security.voms.admin.event.vo.aup.AUPCreatedEvent;
import org.glite.security.voms.admin.event.vo.group.GroupCreatedEvent;
import org.glite.security.voms.admin.event.vo.role.RoleCreatedEvent;
import org.glite.security.voms.admin.operations.CurrentAdminPrincipal;
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
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.task.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseSetupTask implements Runnable {

  private static final Logger log = LoggerFactory
    .getLogger(DatabaseSetupTask.class);

  private static DatabaseSetupTask instance = null;

  private AuditLogHelper auditLogHelper = new AuditLogHelper(
    CurrentAdminPrincipal.LOCAL_DB_PRINCIPAL);

  public synchronized static DatabaseSetupTask instance() {

    if (instance == null)
      instance = new DatabaseSetupTask();
    return instance;

  }

  private void addAdminCreatedAuditEvent(VOMSAdmin admin) {

    auditLogHelper.saveAuditEvent(AdminCreatedEvent.class, admin);

  }

  private void addACLCreatedAuditEvent(ACL acl) {

    auditLogHelper.saveAuditEvent(ACLCreatedEvent.class, acl);

  }

  private void addGroupCreatedAuditEvent(VOMSGroup g) {

    auditLogHelper.saveAuditEvent(GroupCreatedEvent.class, g);

  }

  private void addRoleCreatedAuditEvent(VOMSRole r) {

    auditLogHelper.saveAuditEvent(RoleCreatedEvent.class, r);

  }

  private void setupRootGroup() {

    VOMSGroup rootGroup = VOMSGroupDAO.instance().getVOGroup();

    if (rootGroup == null) {
      log.info("Setting up VO root group...");

      rootGroup = VOMSGroupDAO.instance().createVOGroup();
      addGroupCreatedAuditEvent(rootGroup);
    }

  }

  private void setupInternalCAs() {

    log.info("Setting up voms-admin internal CAs..");

    VOMSCADAO caDAO = VOMSCADAO.instance();

    caDAO.createIfMissing(VOMSServiceConstants.VIRTUAL_CA,
      "A dummy CA for local mainteneance");

    caDAO.createIfMissing(VOMSServiceConstants.GROUP_CA,
      "A virtual CA for VOMS groups.");

    caDAO.createIfMissing(VOMSServiceConstants.ROLE_CA,
      "A virtual CA for VOMS roles.");

    caDAO.createIfMissing(VOMSServiceConstants.AUTHZMANAGER_ATTRIBUTE_CA,
      "A virtual CA for authz manager attributes");

  }

  private void setupInternalAdmins() {

    List<VOMSAdmin> admins = VOMSAdminDAO.instance().getAll();

    if (admins.isEmpty()) {

      log.info("Setting up voms-admin internal administrators...");

      VOMSGroup voGroup = VOMSGroupDAO.instance().getVOGroup();
      if (voGroup == null)
        setupRootGroup();

      VOMSAdminDAO adminDAO = VOMSAdminDAO.instance();

      VOMSAdmin internalAdmin = adminDAO.createFromSubjectAndIssuer(
        VOMSServiceConstants.INTERNAL_ADMIN, VOMSServiceConstants.VIRTUAL_CA);

      addAdminCreatedAuditEvent(internalAdmin);

      VOMSAdmin localAdmin = adminDAO.createFromSubjectAndIssuer(VOMSServiceConstants.LOCAL_ADMIN,
        VOMSServiceConstants.VIRTUAL_CA);

      addAdminCreatedAuditEvent(localAdmin);

      VOMSAdmin anyone = adminDAO.createFromSubjectAndIssuer(VOMSServiceConstants.PUBLIC_ADMIN,
        VOMSServiceConstants.VIRTUAL_CA);

      addAdminCreatedAuditEvent(anyone);

      VOMSAdmin authenticatedUser = adminDAO.createFromSubjectAndIssuer(
        VOMSServiceConstants.ANYUSER_ADMIN, VOMSServiceConstants.VIRTUAL_CA);

      addAdminCreatedAuditEvent(authenticatedUser);

      VOMSAdmin unauthenticatedClient = adminDAO.createFromSubjectAndIssuer(
        VOMSServiceConstants.UNAUTHENTICATED_CLIENT,
        VOMSServiceConstants.VIRTUAL_CA);

      addAdminCreatedAuditEvent(unauthenticatedClient);

      VOMSPermission allPermissions = VOMSPermission.getAllPermissions();

      ACL voGroupACL = new ACL(voGroup, false);
      voGroup.getAcls().add(voGroupACL);

      voGroupACL.setPermissions(localAdmin, allPermissions);
      voGroupACL.setPermissions(internalAdmin, allPermissions);

      // Create VO-Admin role and admin

      VOMSRole voAdminRole = VOMSRoleDAO.instance().create("VO-Admin");

      addRoleCreatedAuditEvent(voAdminRole);

      VOMSAdmin voAdmin = VOMSAdminDAO.instance().createFromFqan(
        voGroup.getName() + "/Role=VO-Admin");

      addAdminCreatedAuditEvent(voAdmin);

      voGroupACL.setPermissions(voAdmin, allPermissions);

      voAdminRole.importACL(voGroup);

      addACLCreatedAuditEvent(voGroupACL);
      addACLCreatedAuditEvent(voAdminRole.getACL(voGroup));

    }
  }

  public void setupTasks() {

    TaskTypeDAO ttDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
      .getTaskTypeDAO();

    if (ttDAO.findAll().isEmpty()) {

      log.info("Setting up voms-admin task infrastructure...");

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

      log.info("Setting up voms-admin aup infrastructure...");
      // Setup VO AUP
      String voAUPUrlString = VOMSConfiguration.instance().getString(
        VOMSConfigurationConstants.VO_AUP_URL,
        VOMSConfiguration.instance().getDefaultVOAUPURL());

      if (voAUPUrlString.trim().equals("")) {
        log.warn("No url defined for VO AUP, using default setting...");
        voAUPUrlString = VOMSConfiguration.instance().getDefaultVOAUPURL();
      }

      try {

        URL voAUPURL = new URL(voAUPUrlString);

        AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();

        AUP voAUP = aupDAO.createVOAUP("", "1.0", voAUPURL);
        auditLogHelper.saveAuditEvent(AUPCreatedEvent.class, voAUP);
        

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
