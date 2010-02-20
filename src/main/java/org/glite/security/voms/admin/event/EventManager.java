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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventManager {

	public static final Log log = LogFactory.getLog(EventManager.class);

	final List<EventListener> listeners = new ArrayList<EventListener>();

	static private EventManager instance = null;

	public static final EventManager instance() {
		if (instance == null)
			instance = new EventManager();
		return instance;
	}

	private EventManager() {

	}

	public void register(EventListener listener) {

		listeners.add(listener);
	}

	public void unRegister(EventListener listener) {

		listeners.remove(listener);
	}

	public List<EventListener> getListeners() {
		return listeners;
	}

	public void fireEvent(Event e) {
		try {
			for (EventListener l : getListeners()) {

				if (l.getMask() == null || l.getMask().get(e.getType().bitNo))
					l.fire(e);
			}
		} catch (Throwable t) {
			log.error("Error dispatching event '" + e + "': " + t.getMessage());
			if (log.isDebugEnabled())
				log.error("Error dispatching event '" + e + "': "
						+ t.getMessage(), t);
		}
	}

	public static void dispatch(Event e) {
		if (instance == null)
			log
					.debug("Event manager has not been initialized! The event will be lost...");
		else
			instance.fireEvent(e);
	}
}
