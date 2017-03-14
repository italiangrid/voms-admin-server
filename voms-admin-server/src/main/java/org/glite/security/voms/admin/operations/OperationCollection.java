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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.error.VOMSException;

public abstract class OperationCollection {

  public static final Logger log = LoggerFactory
    .getLogger(OperationCollection.class);

  Class opClazz;
  List<VOMSOperation> operations;

  protected abstract void instantiateOperations() throws Exception;

  public final List<Object> execute() {

    try {

      instantiateOperations();

    } catch (Throwable e) {
      log.error("Error instantiating operation: " + e.getMessage(),e);

      throw new VOMSException("Error instantiating operation: "
        + e.getMessage(), e);
    }

    List returnValues = new ArrayList();

    for (VOMSOperation op : operations) {

      try {

        Object retVal = op.execute();
        returnValues.add(retVal);

      } catch (RuntimeException e) {
        log.error("Error executing operation '" + op.getName() + "' :"
          + e.getMessage());
        if (log.isDebugEnabled())
          log.error(
            "Error executing operation '" + op.getName() + "' :"
              + e.getMessage(), e);

        throw e;
      }
    }

    return returnValues;
  }

}
