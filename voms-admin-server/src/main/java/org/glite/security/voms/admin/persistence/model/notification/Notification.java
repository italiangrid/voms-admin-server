package org.glite.security.voms.admin.persistence.model.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.glite.security.voms.admin.persistence.model.notification.NotificationDelivery.NotificationDeliveryStatus;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "notification")
public class Notification {

  public static enum NotificationStatus {
    QUEUED,
    IN_DELIVERY,
    DELIVERED,
    ERROR
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @CollectionOfElements(fetch = FetchType.EAGER)
  @JoinTable(name = "notification_recipients")
  List<String> recipients = new ArrayList<String>();

  @Column(nullable = false, columnDefinition = "varchar(512)")
  @Index(name="notification_msg_type_idx")
  String messageType;

  @Column(nullable = false, columnDefinition = "text")
  String subject;

  @Column(nullable = false, columnDefinition = "text")
  String message;

  @Column(name = "handler_id", columnDefinition = "varchar(255)")
  String handlerId;

  @Column(name = "creation_time", nullable = false, updatable = false)
  Date creationTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Index(name = "notification_status_idx")
  NotificationStatus status = NotificationStatus.QUEUED;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "notification")
  List<NotificationDelivery> deliveryAttempts = new ArrayList<NotificationDelivery>();

  public Notification() {

  }

  public Long getId() {

    return id;
  }

  public void setId(Long id) {

    this.id = id;
  }

  public String getMessage() {

    return message;
  }

  public void setMessage(String message) {

    this.message = message;
  }

  public String getHandlerId() {

    return handlerId;
  }

  public void setHandlerId(String handlerId) {

    this.handlerId = handlerId;
  }

  public Date getCreationTime() {

    return creationTime;
  }

  public void setCreationTime(Date creationTime) {

    this.creationTime = creationTime;
  }

  public NotificationStatus getStatus() {

    return status;
  }

  public void setStatus(NotificationStatus status) {

    this.status = status;
  }

  public List<NotificationDelivery> getDeliveryAttempts() {

    return deliveryAttempts;
  }

  public void setDeliveryAttempts(List<NotificationDelivery> deliveryAttempts) {

    this.deliveryAttempts = deliveryAttempts;
  }

  public String getSubject() {

    return subject;
  }

  public void setSubject(String subject) {

    this.subject = subject;
  }

  public String getMessageType() {

    return messageType;
  }

  public void setMessageType(String messageType) {

    this.messageType = messageType;
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
    Notification other = (Notification) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  public List<String> getRecipients() {

    return recipients;
  }

  public void setRecipients(List<String> recipients) {

    this.recipients = recipients;
  }

  @Override
  public String toString() {

    return "Notification [id=" + id + ", recipients=" + recipients
      + ", messageType=" + messageType + ", subject=" + subject + ", message="
      + message + ", handlerId=" + handlerId + ", creationTime=" + creationTime
      + ", status=" + status + ", deliveryAttempts=" + deliveryAttempts + "]";
  }

  public void deliverySuccess(String handlerId) {

    setHandlerId(handlerId);

    NotificationDelivery d = new NotificationDelivery(this);
    getDeliveryAttempts().add(d);
    d.setStatus(NotificationDeliveryStatus.SUCCESS);
    setStatus(NotificationStatus.DELIVERED);
  }

  public void deliveryError(Throwable t, String handlerId) {

    setHandlerId(handlerId);

    NotificationDelivery d = new NotificationDelivery(this);
    d.setErrorMessage(t.getMessage());

    getDeliveryAttempts().add(d);
    d.setStatus(NotificationDeliveryStatus.ERROR);
    setStatus(NotificationStatus.ERROR);
  }
}
