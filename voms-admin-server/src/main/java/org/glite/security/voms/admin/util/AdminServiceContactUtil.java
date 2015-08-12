/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminServiceContactUtil {

  public static final String SERVICE_CONTACT_FILENAME = "service-endpoint";

  public static final Logger log = LoggerFactory
    .getLogger(AdminServiceContactUtil.class);

  private AdminServiceContactUtil() {

  }

  private static AdminServiceContactInfo parseContactInfo(String voConfDirPath)
    throws IOException {

    String voName = voConfDirPath.substring(voConfDirPath.lastIndexOf('/') + 1);

    File voDir = new File(voConfDirPath);

    if (voDir.exists() && voDir.isDirectory() && voDir.canRead()
      && voDir.canExecute()) {

      File contactFile = new File(voDir.getAbsolutePath() + "/"
        + SERVICE_CONTACT_FILENAME);
      if (contactFile.exists() && contactFile.canRead()) {

        String contactString = FileUtils.readFileToString(contactFile);
        String[] parts = contactString.split(":");

        AdminServiceContactInfo info = new AdminServiceContactInfo(voName,
          parts[0], Integer.parseInt(parts[1]));

        return info;
      }
    }

    throw new IllegalArgumentException(
      "VOMS Admin service configuration cannot be read from " + voConfDirPath);

  }

  public static List<AdminServiceContactInfo> getAdminServiceContactInfo(
    String configDir) {

    List<AdminServiceContactInfo> contacts = new ArrayList<AdminServiceContactInfo>();

    File confDir = new File(configDir);
    if (confDir.exists() && confDir.isDirectory() && confDir.canExecute()
      && confDir.canRead()) {

      File[] voFiles = confDir.listFiles(new FileFilter() {

        @Override
        public boolean accept(File pathname) {

          return pathname.isDirectory();
        }
      });

      for (File f : voFiles) {
        try {

          AdminServiceContactInfo info = parseContactInfo(f.getAbsolutePath());
          contacts.add(info);

        } catch (IOException e) {
          log.error(e.getMessage(), e);
          continue;
        }
      }
    }

    return contacts;
  }
}
