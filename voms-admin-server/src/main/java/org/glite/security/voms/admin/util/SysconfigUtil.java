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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.glite.security.voms.admin.configuration.VOMSConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysconfigUtil {

  public static Logger log = LoggerFactory.getLogger(SysconfigUtil.class);

  public static final String SYSCONFIG_DEFAULT_FILE_PATH = "/etc/sysconfig/voms-admin";
  public static final String SYSCONFIG_CONF_DIR = "CONF_DIR";
  public static final String SYSCONFIG_DEFAULT_VO_NAME = "DEFAULT_VO_NAME";

  public static final String SYCONFIG_VOMSES_SHUTDOWN_PORT = "VOMSES_SHUTDOWN_PORT";
  public static final String SYCONFIG_VOMSES_SHUTDOWN_PASSWORD = "VOMSES_SHUTDOWN_PASSWORD";

  public static final String DEFAULT_PREFIX = "/";
  public static final String PREFIX_PROP_NAME = "package.prefix";

  synchronized static Properties loadPackagingProperties() {

    Properties packagingProps = new Properties();

    try {

      packagingProps.load(SysconfigUtil.class.getClassLoader()
        .getResourceAsStream("packaging.properties"));

    } catch (IOException e) {

      packagingProps = null;
      log
        .warn("Packaging properties not found in classloader... using default value for sysconfig location");
    }

    return packagingProps;

  }

  public synchronized static String getInstallationPrefix() {

    Properties packagingProps = loadPackagingProperties();

    if (packagingProps == null)
      return DEFAULT_PREFIX;

    if (packagingProps.getProperty(PREFIX_PROP_NAME) == null) {
      log.warn("No {} property found in packaging properties.",
        PREFIX_PROP_NAME);
      return DEFAULT_PREFIX;
    }

    return packagingProps.getProperty(PREFIX_PROP_NAME);

  }

  public synchronized static String getSysconfigFilePath() {

    String sysconfigFilePath = SYSCONFIG_DEFAULT_FILE_PATH;

    String prefix = getInstallationPrefix();

    if (prefix != null) {

      sysconfigFilePath = String.format("%s/etc/sysconfig/voms-admin", prefix)
        .replaceAll("/+", "/");

    } else {

      log
        .warn("Packaging properties do not specify package.prefix property...using default value for sysconfig location");
    }

    return sysconfigFilePath;

  }

  public synchronized static Properties loadSysconfig() {

    String sysconfigFilePath = getSysconfigFilePath();

    FileReader sysconfigReader;

    Properties props = new Properties();

    try {

      sysconfigReader = new FileReader(sysconfigFilePath);
      props.load(sysconfigReader);

      return props;

    } catch (FileNotFoundException e) {

      log.error("Error opening VOMS Admin system configuration file "
        + sysconfigFilePath);
      throw new VOMSConfigurationException(
        "Error opening VOMS Admin system configuration file "
          + sysconfigFilePath, e);

    } catch (IOException e) {
      log.error("Error parsing VOMS Admin system configuration file "
        + SYSCONFIG_DEFAULT_FILE_PATH);
      throw new VOMSConfigurationException(
        "Error parsing VOMS Admin system configuration file "
          + sysconfigFilePath, e);
    }
  }
}
