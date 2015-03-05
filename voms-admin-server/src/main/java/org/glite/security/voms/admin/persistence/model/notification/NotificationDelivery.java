package org.glite.security.voms.admin.persistence.model.notification;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notification_delivery")
public class NotificationDelivery {

  public static enum NotificationDeliveryStatus {
    SUCCESS, ERROR
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @ManyToOne(optional = false)
  Notification notification;

  @Column(name = "delivery_timestamp", nullable = false, updatable = false)
  Date deliveryTimestamp;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  NotificationDeliveryStatus status;

  @Column(name = "error_message", columnDefinition = "text")
  String errorMessage;

  @Column(name = "handler_id", nullable = false, updatable = false)
  String handlerId;

  public NotificationDelivery(){
    
  }
  
  NotificationDelivery(Notification n) {
    this.notification = n;
    this.handlerId = n.handlerId;
    this.deliveryTimestamp = new Date();
    
  }

  public Long getId() {

    return id;
  }

  public void setId(Long id) {

    this.id = id;
  }

  public Notification getNotification() {

    return notification;
  }

  public void setNotification(Notification notification) {

    this.notification = notification;
  }

  public Date getDeliveryTimestamp() {

    return deliveryTimestamp;
  }

  public void setDeliveryTimestamp(Date deliveryTimestamp) {

    this.deliveryTimestamp = deliveryTimestamp;
  }

  public NotificationDeliveryStatus getStatus() {

    return status;
  }

  public void setStatus(NotificationDeliveryStatus status) {

    this.status = status;
  }

  public String getErrorMessage() {

    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {

    this.errorMessage = errorMessage;
  }

  public String getHandlerId() {

    return handlerId;
  }

  public void setHandlerId(String handlerId) {

    this.handlerId = handlerId;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NotificationDelivery other = (NotificationDelivery) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {

    return "NotificationDelivery [id=" + id + ", notificationId=" + notification.getId()
      + ", deliveryTimestamp=" + deliveryTimestamp + ", status=" + status
      + ", errorMessage=" + errorMessage + ", handlerId=" + handlerId + "]";
  }

}
