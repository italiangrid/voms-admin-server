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
