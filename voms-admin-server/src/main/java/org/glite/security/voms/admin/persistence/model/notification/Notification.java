package org.glite.security.voms.admin.persistence.model.notification;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name="notification")
public class Notification {

  public static enum NotificationStatus {
    QUEUED,
    IN_DELIVERY,
    DELIVERED,
    ERROR
  }
  
  @GeneratedValue(strategy=GenerationType.AUTO)
  Long id;
  
  String recipients;
  
  String message;
  
  String handlerId;
  
  int deliveryAttempt;
  
  NotificationStatus status;
  
  public Notification() {

    
  }

}
