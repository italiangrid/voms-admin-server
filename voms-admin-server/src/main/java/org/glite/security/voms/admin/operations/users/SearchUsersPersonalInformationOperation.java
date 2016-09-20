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
package org.glite.security.voms.admin.operations.users;

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.SearchResults;

public class SearchUsersPersonalInformationOperation
  extends BaseVoReadOperation<SearchResults> {

  private final Callable<SearchResults> searchCallable;

  public static SearchUsersPersonalInformationOperation instance(
    Callable<SearchResults> callable) {

    return new SearchUsersPersonalInformationOperation(callable);
  }

  private SearchUsersPersonalInformationOperation(
    Callable<SearchResults> searchCallable) {
    this.searchCallable = searchCallable;
  }

  @Override
  protected SearchResults doExecute() {

    try {
      return searchCallable.call();
    } catch (Exception e) {
      throw new VOMSException(e);
    }

  }

  @Override
  protected void setupPermissions() {

    // @formatter:off
    addRequiredPermission(VOMSContext.getVoContext(), 
      VOMSPermission
        .getEmptyPermissions()
        .setContainerReadPermission()
        .setMembershipReadPermission()
        .setPersonalInfoReadPermission());
  }

}
