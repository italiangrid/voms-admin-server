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
package org.glite.security.voms.admin.persistence.model.task;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.glite.security.voms.admin.persistence.model.request.Request;

@Entity
@Table(name = "user_request_task")
public class ApproveUserRequestTask extends Task implements Serializable {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  @ManyToOne
  @JoinColumn(name = "req_id", updatable = false, nullable = false)
  Request request;

  public ApproveUserRequestTask() {

  }

  /**
   * @return the request
   */
  public Request getRequest() {

    return request;
  }

  /**
   * @param request
   *          the request to set
   */
  public void setRequest(Request request) {

    this.request = request;
  }

}
