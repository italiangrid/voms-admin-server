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
