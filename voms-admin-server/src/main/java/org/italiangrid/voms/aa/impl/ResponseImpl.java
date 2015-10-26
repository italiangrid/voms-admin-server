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
package org.italiangrid.voms.aa.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.italiangrid.voms.VOMSGenericAttribute;
import org.italiangrid.voms.aa.VOMSErrorMessage;
import org.italiangrid.voms.aa.VOMSResponse;
import org.italiangrid.voms.aa.VOMSWarningMessage;

public class ResponseImpl implements VOMSResponse {

  public ResponseImpl() {

    warnings = new ArrayList<VOMSWarningMessage>();
    errorMessages = new ArrayList<VOMSErrorMessage>();
    issuedFQANs = new ArrayList<String>();
    issuedGAs = new ArrayList<VOMSGenericAttribute>();
    targets = new ArrayList<String>();
    outcome = Outcome.SUCCESS;
  }

  private Outcome outcome;
  private List<VOMSWarningMessage> warnings;
  private List<VOMSErrorMessage> errorMessages;
  private List<String> issuedFQANs;
  private List<VOMSGenericAttribute> issuedGAs;
  private List<String> targets;
  private Date notAfter;
  private Date notBefore;

  @Override
  public Outcome getOutcome() {

    return outcome;
  }

  @Override
  public void setOutcome(Outcome o) {

    outcome = o;
  }

  @Override
  public List<VOMSWarningMessage> getWarnings() {

    return warnings;
  }

  @Override
  public List<VOMSErrorMessage> getErrorMessages() {

    return errorMessages;
  }

  @Override
  public List<String> getIssuedFQANs() {

    return issuedFQANs;
  }

  @Override
  public void setIssuedFQANs(List<String> issuedFQANs) {

    this.issuedFQANs = issuedFQANs;
  }

  @Override
  public List<VOMSGenericAttribute> getIssuedGAs() {

    return issuedGAs;
  }

  @Override
  public void setIssuedGAs(List<VOMSGenericAttribute> issuedGAs) {

    this.issuedGAs = issuedGAs;
  }

  @Override
  public List<String> getTargets() {

    return targets;
  }

  @Override
  public void setTargets(List<String> targets) {

    this.targets = targets;
  }

  @Override
  public Date getNotAfter() {

    return notAfter;
  }

  @Override
  public void setNotAfter(Date notAfter) {

    this.notAfter = notAfter;
  }

  @Override
  public Date getNotBefore() {

    return notBefore;
  }

  @Override
  public void setNotBefore(Date notBefore) {

    this.notBefore = notBefore;
  }

}
