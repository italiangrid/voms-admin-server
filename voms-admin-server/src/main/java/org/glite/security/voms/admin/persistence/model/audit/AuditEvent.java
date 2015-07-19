package org.glite.security.voms.admin.persistence.model.audit;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "audit_event")
@org.hibernate.annotations.Table(appliesTo="audit_event",
  indexes={
    @Index(columnNames={"principal","event_timestamp"}, name="ae_principal_idx"),
    @Index(columnNames={"event_timestamp","event_type"}, name="ae_type_idx")
  }
)
public class AuditEvent {

  @Id
  @Column(name = "event_id")
  @GeneratedValue
  Long id;

  @Column(name = "principal", nullable = false, length = 255)
  String principal;

  @Column(name = "event_timestamp", nullable = false, updatable = false)
  Date timestamp;

  @Column(name = "event_type", nullable = false, length = 255)
  String type;

  @CollectionOfElements
  @JoinTable(name = "audit_event_data", joinColumns = @JoinColumn(
    name = "event_id"))
  Set<AuditEventData> data = new HashSet<AuditEventData>();

  public Long getId() {

    return id;
  }

  public void setId(Long id) {

    this.id = id;
  }

  public String getPrincipal() {

    return principal;
  }

  public void setPrincipal(String principal) {

    this.principal = principal;
  }

  public Date getTimestamp() {

    return timestamp;
  }

  public void setTimestamp(Date timestamp) {

    this.timestamp = timestamp;
  }

  public String getType() {

    return type;
  }

  public void setType(String type) {

    this.type = type;
  }

  public Set<AuditEventData> getData() {

    return data;
  }

  public void setData(Set<AuditEventData> data) {

    this.data = data;
  }

  public AuditEvent addDataPoint(String name, String value){
    
    getData().add(new AuditEventData(name, value));
    return this;
    
  }
  
  public String getDataPoint(String name){
    
    for (AuditEventData dp: getData()){
      if (dp.getName().equals(name)){
        return dp.getValue();
      }
    }
    
    return null;
  }
  
  
  @Override
  public String toString() {

    return "AuditEvent [id=" + id + ", principal=" + principal + ", timestamp="
      + timestamp + ", type=" + type + ", data=" + data + "]";
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((principal == null) ? 0 : principal.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    AuditEvent other = (AuditEvent) obj;
    if (principal == null) {
      if (other.principal != null)
        return false;
    } else if (!principal.equals(other.principal))
      return false;
    if (timestamp == null) {
      if (other.timestamp != null)
        return false;
    } else if (!timestamp.equals(other.timestamp))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    return true;
  }

  public String getShortType(){
    return type.substring(type.lastIndexOf(".")+1);
  }
}
