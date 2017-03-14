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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleArgumentOperationCollection<T> extends OperationCollection {

  public static final Logger log = LoggerFactory
    .getLogger(SingleArgumentOperationCollection.class);
  List<T> args;

  protected void instantiateOperations() throws Exception {

    operations = new ArrayList<VOMSOperation>();

    for (T arg : args) {

      Method instanceMethod = opClazz.getMethod("instance", arg.getClass());
      operations.add((VOMSOperation) instanceMethod.invoke(null, arg));
    }
  }

  public SingleArgumentOperationCollection(List<T> args, Class opClass) {

    this.args = args;
    this.opClazz = opClass;
  }

}
