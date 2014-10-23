/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.persistence.model.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "membership_rem_req")
public class MembershipRemovalRequest extends Request {

  @Column(nullable = false)
  String reason;

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  public String getTypeName() {

    return "Membership removal request";
  }

  @Override
  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof MembershipRemovalRequest))
      return false;

    if (other == null)
      return false;

    MembershipRemovalRequest that = (MembershipRemovalRequest) other;

    EqualsBuilder builder = new EqualsBuilder();

    return builder.appendSuper(super.equals(other))
      .append(reason, that.getReason()).isEquals();
  }

  @Override
  public int hashCode() {

    HashCodeBuilder builder = new HashCodeBuilder(7, 73);
    builder.appendSuper(super.hashCode()).append(reason);
    return builder.toHashCode();
  }

  public String getReason() {

    return reason;
  }

  public void setReason(String reason) {

    this.reason = reason;
  }

  @Override
  public String toString() {

    ToStringBuilder builder = new ToStringBuilder(this);

    return builder.appendSuper(super.toString()).append("reason", reason)
      .toString();
  }

}
