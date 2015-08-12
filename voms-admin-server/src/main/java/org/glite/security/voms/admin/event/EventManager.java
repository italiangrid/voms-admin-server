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
package org.glite.security.voms.admin.event;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventManager implements EventDispatcher {

  public static final Logger log = LoggerFactory.getLogger(EventManager.class);

  final List<EventListener> listeners = new ArrayList<EventListener>();

  static private volatile EventManager instance = null;

  public static synchronized EventManager instance() {

    if (instance == null)
      instance = new EventManager();
    return instance;
  }

  private EventManager() {

  }

  public synchronized void register(EventListener listener) {

    listeners.add(listener);
  }

  public synchronized void unRegister(EventListener listener) {

    listeners.remove(listener);
  }

  public synchronized List<EventListener> getListeners() {

    return listeners;
  }

  public synchronized void dispatch(Event e) {

    try {
      for (EventListener l : getListeners()) {

        if (l.getCategoryMask().contains(e.getCategory())) {
          l.fire(e);
        }

      }
    } catch (Throwable t) {

      log.error("Error dispatching event '" + e + "': " + t.getMessage(), t);
    }

  }
}
