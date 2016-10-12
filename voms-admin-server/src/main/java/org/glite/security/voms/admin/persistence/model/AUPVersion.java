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
package org.glite.security.voms.admin.persistence.model;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.glite.security.voms.admin.util.URLContentFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AUPVersion implements Serializable, Comparable<AUPVersion> {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(AUPVersion.class);

  Long id;

  AUP aup = null;

  String version;

  String url;

  String text;

  Date creationTime;

  Date lastForcedReacceptanceTime;

  Boolean active;

  public AUPVersion() {

    setActive(false);
  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof AUPVersion))
      return false;

    if (other == null)
      return false;

    AUPVersion that = (AUPVersion) other;

    if (getAup().equals(that.getAup()))
      return getVersion().equals(that.getVersion());
    else
      return false;

  }

  @Override
  public int hashCode() {

    if (this.getId() != null)
      return getId().hashCode();

    return super.hashCode();
  }

  // Getters and Setters
  /**
   * @return the id
   */
  public Long getId() {

    return id;
  }

  /**
   * @return the aup
   */
  public AUP getAup() {

    return aup;
  }

  /**
   * @return the version
   */
  public String getVersion() {

    return version;
  }

  /**
   * @return the creationTime
   */
  public Date getCreationTime() {

    return creationTime;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @param aup
   *          the aup to set
   */
  public void setAup(AUP aup) {

    this.aup = aup;
  }

  /**
   * @param version
   *          the version to set
   */
  public void setVersion(String version) {

    this.version = version;
  }

  /**
   * @param creationTime
   *          the creationTime to set
   */
  public void setCreationTime(Date creationTime) {

    this.creationTime = creationTime;
  }

  /**
   * @return the url
   */
  public String getUrl() {

    return url;
  }

  /**
   * @param url
   *          the url to set
   */
  public void setUrl(String url) {

    this.url = url;
  }

  @Override
  public String toString() {

    ToStringBuilder builder = new ToStringBuilder(this);
    builder.append("version", version).append("url", url)
      .append("creationTime", creationTime)
      .append("lastForceReacceptanceTime", lastForcedReacceptanceTime)
      .append("active", active);

    return builder.toString();

  }

  public int compareTo(AUPVersion o) {

    if (this.equals(o))
      return 0;
    else
      return getVersion().compareTo(o.getVersion());
  }

  public String getText() {

    return text;
  }

  public void setText(String text) {

    this.text = text;
  }

  public String getURLContent() {

    return URLContentFetcher.fetchTextFromURL(getUrl());
  }

  public Boolean getActive() {

    return active;
  }

  public void setActive(Boolean active) {

    this.active = active;
  }

  public Date getLastForcedReacceptanceTime() {

    return lastForcedReacceptanceTime;
  }

  public void setLastForcedReacceptanceTime(Date lastForcedReacceptanceTime) {

    this.lastForcedReacceptanceTime = lastForcedReacceptanceTime;
  }

  public Date getLastUpdateTime() {

    if (getLastForcedReacceptanceTime() != null)
      return getLastForcedReacceptanceTime();
    else
      return getCreationTime();
  }

}
