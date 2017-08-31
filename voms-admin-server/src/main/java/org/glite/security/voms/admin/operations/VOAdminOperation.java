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
package org.glite.security.voms.admin.operations;

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.error.VOMSException;

public class VOAdminOperation<V> extends BaseVomsOperation<V> {

  private final Callable<V> task;

  public VOAdminOperation(Callable<V> task) {

    this.task = task;

  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(),
      VOMSPermission.getAllPermissions());
  }

  @Override
  protected V doExecute() {

    V result = null;

    try {

      result = task.call();

    } catch (Exception e) {
      String errorMessage = String
          .format("Error executing %s: %s", task.getClass().getSimpleName(), e.getMessage());
      throw new VOMSException(errorMessage, e);
    }

    return result;
  }

}
