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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStatsTask implements Runnable {

  public static final Logger logger = LoggerFactory
    .getLogger(UserStatsTask.class);

  public static final String DEFAULT_USER_STATS_BASE_PATH = "/var/tmp/voms-admin";
  public static final String DEFAULT_USER_STATS_FILENAME = "stats.properties";

  public static final Long DEFAULT_PERIOD_IN_SECONDS = TimeUnit.MINUTES
    .toSeconds(60);

  Properties monitoredProperties;

  public UserStatsTask() {

    createStatsDir();
    monitoredProperties = new Properties();

  }

  void storeProperties() {

    try {
      FileOutputStream fos = new FileOutputStream(getStatsFileName());
      monitoredProperties.store(fos, null);
      fos.close();

    } catch (IOException e) {
      logger.error("Couldn't write user stats file: {}", e.getMessage(), e);
      createStatsDir();
    }

  }

  String getStatsDirPath() {

    String statsDirPath = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.MONITORING_USER_STATS_BASE_PATH,
      DEFAULT_USER_STATS_BASE_PATH);
    String statsDir = String.format("%s/%s", statsDirPath, VOMSConfiguration
      .instance().getVOName());
    return statsDir;
  }

  String getStatsFileName() {

    return String.format("%s/%s", getStatsDirPath(),
      DEFAULT_USER_STATS_FILENAME);

  }

  void createStatsDir() {

    File statsDirFile = new File(getStatsDirPath());

    if (!statsDirFile.exists())
      statsDirFile.mkdirs();

  }

  public void run() {

    VOMSUserDAO dao = VOMSUserDAO.instance();

    Long expiredUsersCount = dao.countExpiredUsers();
    Long usersCount = dao.countUsers();
    Long suspendedUsersCount = dao.countSuspendedUsers();

    monitoredProperties.put("usersCount", usersCount.toString());
    monitoredProperties.put("expiredUsersCount", expiredUsersCount.toString());
    monitoredProperties
      .put("suspendUsersCount", suspendedUsersCount.toString());

    storeProperties();

  }

}
