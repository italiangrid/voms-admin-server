package org.glite.security.voms.admin.view.actions.audit.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.xwork.Validate;
import org.glite.security.voms.admin.event.AuditableEvent;
import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationDataPointClassifier implements EventDataPointClassifier {

  final Map<String, Set<String>> mainDataPointsMap;
  
  public static final Logger LOG = 
    LoggerFactory.getLogger(AnnotationDataPointClassifier.class);
  
  private static final Set<String> EMPTY_LIST = Collections.emptySet();

  public AnnotationDataPointClassifier() {

    mainDataPointsMap = new ConcurrentHashMap<String, Set<String>>();

  }

  private void resolveDataPointsFromAnnotations(Class<?> eventClass,
    Set<String> foundDataPoints) {

    Validate.notNull(eventClass);
    Validate.notNull(foundDataPoints);

    if (eventClass.equals(AuditableEvent.class)
      || eventClass.equals(Object.class)) {
      return;
    }
    
    LOG.debug("Resolving data points for class {}", 
      eventClass.getSimpleName());
    
    MainEventDataPoints dataPoints = eventClass
      .getAnnotation(MainEventDataPoints.class);
    
    if (dataPoints != null){
      foundDataPoints.addAll(Arrays.asList(dataPoints.value()));
    }

    resolveDataPointsFromAnnotations(eventClass.getSuperclass(),
      foundDataPoints);

    LOG.debug("Class {}. Data points: {}", eventClass.getSimpleName(), 
      foundDataPoints);
    
  }

  @Override
  public Set<String> getDefaultDataPointsForEventType(String eventType) {

    Validate.notEmpty(eventType);

    if (mainDataPointsMap.containsKey(eventType)) {
      return mainDataPointsMap.get(eventType);
    }

    try {

      Class<?> eventClass = Class.forName(eventType);
      Set<String> mainDataPoints = new LinkedHashSet<String>();
      resolveDataPointsFromAnnotations(eventClass, mainDataPoints);

      mainDataPointsMap.put(eventType, mainDataPoints);
      return mainDataPoints;

    } catch (ClassNotFoundException e) {
      return EMPTY_LIST;
    }

  }

}
