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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.italiangrid.voms.container.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSStatusFilter implements Filter {

  public static final Logger log = LoggerFactory
    .getLogger(VOMSStatusFilter.class);

  public static final String STATUS_MAP_KEY = "statusMap";
  public static final String VO_NAMES = "voNames";

  public static final String HOST_KEY = "host";
  public static final String PORT_KEY = "port";
  public static final String VERSION_KEY = "version";

  public static final long STATUS_MAP_UPDATE_TIME = TimeUnit.SECONDS
    .toMillis(30);

  private final DeploymentManager manager;
  private final String hostname;
  private final String port;

  private Map<String, Boolean> statusMap;
  private List<String> voNames;

  long statusMapLastUpdateTime = 0;

  public VOMSStatusFilter(DeploymentManager manager, String host, String port) {

    this.manager = manager;
    this.hostname = host;
    this.port = port;
  }

  protected void updateStatusMap() {

    long currentTime = System.currentTimeMillis();
    if (currentTime - statusMapLastUpdateTime > STATUS_MAP_UPDATE_TIME) {
      log.debug("Updating VOs status map");
      statusMap = StatusUtil.getStatusMap(manager);
      statusMapLastUpdateTime = System.currentTimeMillis();
      voNames = ConfiguredVOsUtil.getConfiguredVONames();
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain) throws IOException, ServletException {

    updateStatusMap();

    request.setAttribute(STATUS_MAP_KEY, statusMap);
    request.setAttribute(VO_NAMES, voNames);
    request.setAttribute(HOST_KEY, hostname);
    request.setAttribute(PORT_KEY, port);
    request.setAttribute(VERSION_KEY, Version.version());

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {

  }

}
