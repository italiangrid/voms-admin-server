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
package org.glite.security.voms.admin.event;

import java.util.HashMap;

public class AbstractEvent implements Event {

  EventType type;

  long creationTime;
  long completionTime;
  boolean completed = false;

  String name;

  HashMap<String, Object> payload;

  public AbstractEvent(EventType type) {

    this.type = type;
    creationTime = System.currentTimeMillis();
    payload = new HashMap<String, Object>();
  }

  public long getCompletionTime() {

    return completionTime;
  }

  public long getCreationTime() {

    return creationTime;
  }

  public boolean isCompleted() {

    return completed;
  }

  public void setCompleted() {

    completed = true;
    completionTime = System.currentTimeMillis();

  }

  public String getName() {

    if (name == null)
      return this.getClass().getSimpleName();

    return name;
  }

  protected void setOperation(String op) {

    name = op;
  }

  public EventType getType() {

    return type;
  }

  public void setType(EventType type) {

    this.type = type;
  }

  public void setName(String name) {

    this.name = name;
  }

}
