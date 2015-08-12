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

import java.util.EnumSet;

public abstract class AbstractEventLister<T extends Event> implements
  EventListener {

  final Class<T> eventClass;
  final EnumSet<EventCategory> mask;

  public AbstractEventLister(EnumSet<EventCategory> mask, Class<T> eventClass) {

    this.mask = mask;
    this.eventClass = eventClass;
  }

  @Override
  public EnumSet<EventCategory> getCategoryMask() {

    return mask;
  }

  @Override
  public final void fire(Event e) {

    if (eventClass.isInstance(e)) {
      doFire((T)e);
    }
  }

  protected abstract void doFire(T e);

}
