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
