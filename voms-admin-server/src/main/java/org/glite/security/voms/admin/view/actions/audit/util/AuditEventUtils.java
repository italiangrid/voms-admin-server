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
package org.glite.security.voms.admin.view.actions.audit.util;

public class AuditEventUtils {

  private static final EventNameFormatter NAME_FORMATTER = new SimpleEventNameFormatter();

  private static final AdminNameFormatter ADMIN_NAME_FORMATTER = new SimpleAdminNameFormatter();

  private static final EventDataPointClassifier DATAPOINT_CLASSIFIER = new AnnotationDataPointClassifier();

  private static final EventDescriptor EVENT_DESCRIPTOR = new AnnotationEventMessageBuilder();
  
  private AuditEventUtils() {

  }

  public static EventNameFormatter eventNameFormatter() {

    return NAME_FORMATTER;
  }

  public static AdminNameFormatter adminNameFormatter() {

    return ADMIN_NAME_FORMATTER;
  }

  public static EventDataPointClassifier dataPointClassifier() {

    return DATAPOINT_CLASSIFIER;
  }
  
  public static EventDescriptor eventDescriptor() {
    return EVENT_DESCRIPTOR;
  }
}
