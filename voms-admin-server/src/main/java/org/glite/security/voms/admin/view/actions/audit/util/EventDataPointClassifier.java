package org.glite.security.voms.admin.view.actions.audit.util;

import java.util.Set;


public interface EventDataPointClassifier {
  
  public Set<String> getDefaultDataPointsForEventType(String eventType);

}
