package org.glite.security.voms.admin.notification;


public interface NotificationSettings {

  public String getSender();
  public String getFrom();
  public String getSMTPHost();
  public int getSMTPPort();
  public String getUsername();
  public String getPassword();
  public boolean isTLS();
  
}
