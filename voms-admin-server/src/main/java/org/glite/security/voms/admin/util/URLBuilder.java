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

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public class URLBuilder {

  public static String buildAdminServiceBaseURL(String host, String portNumber,
    String vo) {

    return String.format("https://%s:%s/voms/%s", host, portNumber, vo);
  }

  public static String baseVOMSURLFromConfiguration() {

    VOMSConfiguration conf = VOMSConfiguration.instance();
    return buildAdminServiceBaseURL(conf.getHostname(), "8443",
      conf.getVOName());

  }

  public static String buildRequestConfirmURL(NewVOMembershipRequest r) {

    return buildRequestConfirmURL(baseVOMSURLFromConfiguration(), r);
  }

  public static String buildRequestCancelURL(NewVOMembershipRequest r) {

    return buildRequestCancelURL(baseVOMSURLFromConfiguration(), r);
  }

  private static String buildRequestURL(String op, String baseURL,
    NewVOMembershipRequest r) {

    return String.format("%s/register/%s-request.action?requestId=%d"
      + "&confirmationId=%s", baseURL, op, r.getId(), r.getConfirmId());
  }

  public static String buildRequestConfirmURL(String baseURL,
    NewVOMembershipRequest r) {

    return buildRequestURL("confirm", baseURL, r);
  }

  public static String buildRequestCancelURL(String baseURL,
    NewVOMembershipRequest r) {

    return buildRequestURL("cancel", baseURL, r);
  }

  public static String buildLoginURL() {

    return String
      .format("%s/home/login.action", baseVOMSURLFromConfiguration());

  }
}
