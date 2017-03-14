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
package org.italiangrid.voms.aa.x509.impl;

import java.util.List;

import org.italiangrid.voms.aa.RequestContext;
import org.italiangrid.voms.aa.x509.ACRequestLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ACRequestLoggerImpl implements ACRequestLogger {

  public static final Logger log = LoggerFactory.getLogger("VOMS-X509-AA");
  public static final String NULL_REQUESTED_FQANs = "<unknown>";

  public ACRequestLoggerImpl() {

  }

  @Override
  public void logSuccess(RequestContext context) {

    log.info("Issued attribute certificate to '{}' for fqans: '{}'.", context
      .getRequest().getHolderSubject(), context.getResponse().getIssuedFQANs());
  }

  @Override
  public void logFailure(RequestContext context, String errorMessage) {

    log.warn("AC request error: {}. Client DN: '{}'. Requested fqans: '{}'",
      new Object[] { errorMessage, context.getRequest().getHolderSubject(),
        context.getRequest().getRequestedFQANs() });
  }

}
