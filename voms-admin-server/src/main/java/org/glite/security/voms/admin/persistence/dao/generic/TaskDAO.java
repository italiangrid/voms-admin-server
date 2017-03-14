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
package org.glite.security.voms.admin.persistence.dao.generic;

import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.task.ApproveUserRequestTask;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task;

public interface TaskDAO extends GenericDAO<Task, Long> {

  SignAUPTask createSignAUPTask(AUP aup);

  SignAUPTask createSignAUPTask(AUP aup, Date expiryDate);

  ApproveUserRequestTask createApproveUserRequestTask(Request req);

  List<SignAUPTask> findActiveSignAUPTasks();
  
  List<Task> findSignAUPTasks();

  List<Task> findApproveUserRequestTasks();

  void removeAllTasks();

  List<Task> getActiveTasks();

}
