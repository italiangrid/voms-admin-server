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
package org.glite.security.voms.admin.integration;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;

public abstract class AbstractPluginConfigurator implements PluginConfigurator {

  private String pluginName = "<undefined>";

  /**
   * @return the pluginName
   */
  public String getPluginName() {

    return pluginName;
  }

  /**
   * @param pluginName
   *          the pluginName to set
   */
  public void setPluginName(String pluginName) {

    this.pluginName = pluginName;
  }

  public String getPluginProperty(String propertyName, String defaultValue) {

    return VOMSConfiguration.instance().getExternalValidatorProperty(
      pluginName, propertyName, defaultValue);
  }

  public String getPluginProperty(String propertyName) {

    return VOMSConfiguration.instance().getExternalValidatorProperty(
      pluginName, propertyName);

  }

  public String getVomsConfigurationDirectoryPath() {

    return VOMSConfiguration.instance().getConfigurationDirectoryPath();
  }

}
