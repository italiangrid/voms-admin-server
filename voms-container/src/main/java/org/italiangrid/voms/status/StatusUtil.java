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
package org.italiangrid.voms.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

public class StatusUtil {

  public static final Map<String, Boolean> getStatusMap(
    DeploymentManager manager) {

    List<String> voNames = ConfiguredVOsUtil.getConfiguredVONames();

    Map<String, Boolean> statusMap = new HashMap<String, Boolean>();

    for (String vo : voNames) {

      App voApp = manager.getAppByOriginId(vo);
      if (voApp == null) {
        statusMap.put(vo, false);
      } else {
        try {

          statusMap.put(
            vo,
            DeploymentManager.getState(voApp.getContextHandler()).equals(
              AbstractLifeCycle.STARTED));

        } catch (Exception e1) {
          statusMap.put(vo, true);
        }
      }
    }
    return statusMap;

  }

}
