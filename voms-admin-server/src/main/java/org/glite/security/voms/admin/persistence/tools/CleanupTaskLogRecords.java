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
package org.glite.security.voms.admin.persistence.tools;

import static java.lang.Long.parseLong;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.DBUtil;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.lookup.LookupPolicyProvider;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.servlets.InitSecurityContext;
import org.hibernate.ScrollableResults;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupTaskLogRecords implements Runnable {

  public static final Logger LOG = LoggerFactory.getLogger(CleanupTaskLogRecords.class);

  final String vo;
  final long batchSize;

  public CleanupTaskLogRecords(String vo, long batchSize) {
    this.vo = vo;
    this.batchSize = batchSize;
  }



  public static void usage() {
    System.out.println("usage: cmd <vo_name> <batch_size>");
    System.exit(1);
  }

  private void loadVomsAdminConfiguration() {
    System.setProperty(VOMSConfigurationConstants.VO_NAME, vo);
    VOMSConfiguration.load(null);
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      usage();
    }

    new CleanupTaskLogRecords(args[0], parseLong(args[1])).run();
  }

  private void initializeLookupPolicyProvider() {

    boolean skipCaCheck =
        VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.SKIP_CA_CHECK, false);

    LookupPolicyProvider.initialize(skipCaCheck);

  }

  @Override
  public void run() {
    loadVomsAdminConfiguration();
    initializeLookupPolicyProvider();
    Configuration hibernateConfig = DBUtil.loadHibernateConfiguration("/etc/voms-admin", vo);

    hibernateConfig.configure();
    HibernateFactory.initialize(hibernateConfig);
    HibernateFactory.beginTransaction();
    try {
      InitSecurityContext.setInternalAdminContext();
      VOMSUserDAO dao = VOMSUserDAO.instance();
      ScrollableResults expiredUsers = dao.findUsersExpiredSinceDays(100);

      long processedRecords = 0;
      
      while (expiredUsers.next() && processedRecords < batchSize) {
        VOMSUser user = (VOMSUser) expiredUsers.get(0);

        LOG.info("Deleting user '{} ({})' tasks who expired on '{}'", user.getShortName(),
            user.getId(), user.getEndTime());

        String deleteTLRs = "delete from task_log_record where task_id in ( select t.task_id from "
            + "task t, usr u where t.usr_id = u.userid and u.userid = :userId )";
        HibernateFactory.getSession()
          .createSQLQuery(deleteTLRs)
          .setLong("userId", user.getId())
          .executeUpdate();

        String deleteSATs = "delete from sign_aup_task where task_id in (select t.task_id from "
            + "task t, usr u where t.usr_id = u.userid and u.userid = :userId )";
        HibernateFactory.getSession()
          .createSQLQuery(deleteSATs)
          .setLong("userId", user.getId())
          .executeUpdate();

        String deleteTasks = "delete from task where usr_id = :userId";
        HibernateFactory.getSession()
          .createSQLQuery(deleteTasks)
          .setLong("userId", user.getId())
          .executeUpdate();
        processedRecords++;
      }

      HibernateFactory.commitTransaction();
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      HibernateFactory.rollbackTransaction();
    }

  }
}
