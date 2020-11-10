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
package org.glite.security.voms.admin.integration.cern;

import static java.lang.String.format;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class HrDbProperties {

  public static final String EXPERIMENT_KEY = "experiment";
  public static final String MEMBERSHIP_CHECK_PERIOD_KEY = "membership_check.period";
  public static final String MEMBERSHIP_CHECK_ENABLED_KEY = "membership_check.enabled";
  public static final String MEMBERSHIP_CHECK_START_HOUR_KEY = "membership_check.start_hour";
  public static final String MEMBERSHIP_CHECK_RUN_AT_STARTUP_KEY =
      "membership_check.run_at_startup";

  public static final String API_ENDPOINT_KEY = "api.endpoint";
  public static final String API_USERNAME_KEY = "api.username";
  public static final String API_PASSWORD_KEY = "api.password";
  public static final String API_TIMEOUT_KEY = "api.timeout_secs";

  public static final String[] REQUIRED_KEYS =
      {EXPERIMENT_KEY, API_ENDPOINT_KEY, API_USERNAME_KEY, API_PASSWORD_KEY};

  public static class MembershipCheck {

    boolean enabled = true;

    boolean runAtStartup = true;

    int startHour = 23;

    long periodInSeconds = TimeUnit.HOURS.toSeconds(12);

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public long getPeriodInSeconds() {
      return periodInSeconds;
    }

    public void setPeriodInSeconds(long periodInSeconds) {
      this.periodInSeconds = periodInSeconds;
    }

    public int getStartHour() {
      return startHour;
    }

    public void setStartHour(int startHour) {
      this.startHour = startHour;
    }

    public void setRunAtStartup(boolean runAtStartup) {
      this.runAtStartup = runAtStartup;
    }

    public boolean isRunAtStartup() {
      return runAtStartup;
    }
  }

  public static class HrDbApiProperties {

    String endpoint = "http://localhost:8080";
    String username = "user";
    String password = "pwd";

    long timeoutInSeconds = 5L;

    public String getEndpoint() {
      return endpoint;
    }

    public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public void setTimeoutInSeconds(long timeoutInSeconds) {
      this.timeoutInSeconds = timeoutInSeconds;
    }

    public long getTimeoutInSeconds() {
      return timeoutInSeconds;
    }
  }

  HrDbApiProperties api = new HrDbApiProperties();
  MembershipCheck membershipCheck = new MembershipCheck();

  String experimentName = "experiment";

  public HrDbApiProperties getApi() {
    return api;
  }

  public void setApi(HrDbApiProperties api) {
    this.api = api;
  }

  public String getExperimentName() {
    return experimentName;
  }

  public void setExperimentName(String experimentName) {
    this.experimentName = experimentName;
  }

  public void setMembershipCheck(MembershipCheck membershipCheck) {
    this.membershipCheck = membershipCheck;
  }

  public MembershipCheck getMembershipCheck() {
    return membershipCheck;
  }

  private static void requireProperty(String key, Properties properties) {
    if (!properties.containsKey(key)) {
      throw new HrDbError(format("Configuration error: required property '%s' not found", key));
    }
  }

  public static final HrDbProperties fromProperties(Properties properties) {

    Stream.of(REQUIRED_KEYS).forEach(p -> requireProperty(p, properties));

    HrDbProperties config = new HrDbProperties();

    config.setExperimentName(properties.getProperty(EXPERIMENT_KEY));

    if (properties.containsKey(MEMBERSHIP_CHECK_START_HOUR_KEY)) {
      int startHour = Integer.parseInt(properties.getProperty(MEMBERSHIP_CHECK_START_HOUR_KEY));

      if (startHour >= 0 && startHour < 24) {
        config.getMembershipCheck()
          .setStartHour(
              (Integer.parseInt(properties.getProperty(MEMBERSHIP_CHECK_START_HOUR_KEY))));
      }
    }

    if (properties.containsKey(MEMBERSHIP_CHECK_RUN_AT_STARTUP_KEY)) {
      config.getMembershipCheck()
        .setRunAtStartup(
            Boolean.parseBoolean(properties.getProperty(MEMBERSHIP_CHECK_RUN_AT_STARTUP_KEY)));
    }

    if (properties.containsKey(MEMBERSHIP_CHECK_PERIOD_KEY)) {
      config.getMembershipCheck()
        .setPeriodInSeconds(Long.parseLong(properties.getProperty(MEMBERSHIP_CHECK_PERIOD_KEY)));
    }

    if (properties.containsKey(MEMBERSHIP_CHECK_ENABLED_KEY)) {
      config.getMembershipCheck().setEnabled(Boolean.parseBoolean(MEMBERSHIP_CHECK_ENABLED_KEY));
    }

    config.getApi().setEndpoint(properties.getProperty(API_ENDPOINT_KEY));
    config.getApi().setUsername(properties.getProperty(API_USERNAME_KEY));
    config.getApi().setPassword(properties.getProperty(API_PASSWORD_KEY));

    if (properties.containsKey(API_TIMEOUT_KEY)) {
      long apiTimeout = Long.parseLong(properties.getProperty(API_TIMEOUT_KEY));

      if (apiTimeout > 0) {
        config.getApi().setTimeoutInSeconds(apiTimeout);
      }
    }

    return config;
  }

}
