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
package org.glite.security.voms.admin.operations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DoubleArgumentOperationCollection<T, U> extends
  OperationCollection {

  List<T> firstArgs;
  List<U> secondArgs;

  Class opClazz;

  public DoubleArgumentOperationCollection(List<T> args, List<U> secondArgs,
    Class opClass) {

    this.firstArgs = args;
    this.secondArgs = secondArgs;
    this.opClazz = opClass;
  }

  @Override
  protected void instantiateOperations() throws Exception {

    operations = new ArrayList<VOMSOperation>();

    if (firstArgs.size() != secondArgs.size())
      throw new IllegalArgumentException(
        "first argument lists and second argument lists must be of the same size!");

    for (int i = 0; i < firstArgs.size(); i++) {

      T firstArg = firstArgs.get(i);
      U secondArg = secondArgs.get(i);

      Method instanceMethod = opClazz.getMethod("instance",
        firstArg.getClass(), secondArg.getClass());
      operations.add((VOMSOperation) instanceMethod.invoke(null, firstArg,
        secondArg));

    }

  }

}
