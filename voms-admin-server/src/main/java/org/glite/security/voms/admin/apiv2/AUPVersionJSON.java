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
package org.glite.security.voms.admin.apiv2;

import java.util.Date;

import org.glite.security.voms.admin.persistence.model.AUPVersion;

public class AUPVersionJSON {
  
  String version;

  String url;

  String text;

  Date creationTime;

  Date lastForcedReacceptanceTime;

  Boolean active;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Date getLastForcedReacceptanceTime() {
    return lastForcedReacceptanceTime;
  }

  public void setLastForcedReacceptanceTime(Date lastForcedReacceptanceTime) {
    this.lastForcedReacceptanceTime = lastForcedReacceptanceTime;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public static AUPVersionJSON fromAUPVersion(AUPVersion v){
    AUPVersionJSON aupVersion = new AUPVersionJSON();
    aupVersion.setActive(v.getActive());
    aupVersion.setCreationTime(v.getCreationTime());
    aupVersion.setLastForcedReacceptanceTime(v.getLastForcedReacceptanceTime());
    aupVersion.setText(v.getText());
    aupVersion.setUrl(v.getUrl());
    aupVersion.setVersion(v.getVersion());
    
    return aupVersion;
  }
  
}
