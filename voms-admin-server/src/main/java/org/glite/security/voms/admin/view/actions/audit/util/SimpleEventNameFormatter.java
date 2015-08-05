package org.glite.security.voms.admin.view.actions.audit.util;

public class SimpleEventNameFormatter implements EventNameFormatter {

  @Override
  public String formatEventName(String eventName) {

    if (eventName == null) {
      return "";
    }

    if (!eventName.contains(".")) {
      return eventName;
    }

    return eventName.substring(eventName.lastIndexOf(".") + 1);
  }

}
