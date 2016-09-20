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
