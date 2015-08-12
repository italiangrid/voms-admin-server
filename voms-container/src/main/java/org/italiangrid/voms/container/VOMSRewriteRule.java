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
package org.italiangrid.voms.container;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.rewrite.handler.Rule;
import org.eclipse.jetty.server.Request;

/**
 * A rewrite rule that maps a generate-ac VOMS request on a given port to a
 * given web app context.
 * 
 * @author andreaceccanti
 *
 */
public class VOMSRewriteRule extends Rule implements Rule.ApplyURI {

  /**
   * A map of port number -> vo names. {15000 -> "atlas"}
   */
  private final Map<Integer, String> voPorts;

  /**
   * A map of the replaced targets, built when the rewrite rule is constructed
   * to get a more efficient implementation.
   */
  private final Map<Integer, String> newTargets;

  /**
   * This rewrite rule matches only request to generate-ac web service.
   */
  private static final String VOMS_REQUEST_PATTERN = "/generate-ac";

  public VOMSRewriteRule(Map<Integer, String> voPorts) {

    setHandling(false);
    setTerminating(false);

    this.voPorts = voPorts;
    newTargets = new HashMap<Integer, String>();

    for (Map.Entry<Integer, String> e : voPorts.entrySet())
      newTargets.put(e.getKey(), "/voms/" + e.getValue() + "/generate-ac");

  }

  @Override
  public String matchAndApply(String target, HttpServletRequest request,
    HttpServletResponse response) throws IOException {

    int incomingPort = request.getLocalPort();

    if (voPorts.keySet().contains(incomingPort))
      if (target.startsWith(VOMS_REQUEST_PATTERN))
        return newTargets.get(incomingPort);

    return null;
  }

  @Override
  public void applyURI(Request request, String oldTarget, String newTarget)
    throws IOException {

    request.setRequestURI(newTarget);

  }
}
