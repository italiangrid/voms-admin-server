package org.glite.security.voms.admin.notification;

public abstract class BaseNotificationSettings implements NotificationSettings {

  protected String sender = null;
  protected String from;
  protected String smtpHost = "localhost";
  protected int smtpPort = 25;
  protected String username;
  protected String password;
  protected boolean tls = false;

  @Override
  public String getSender() {

    return sender;
  }

  @Override
  public String getFrom() {

    return from;
  }

  @Override
  public String getSMTPHost() {

    return smtpHost;
  }

  @Override
  public int getSMTPPort() {

    return smtpPort;
  }

  @Override
  public String getUsername() {

    return username;
  }

  @Override
  public String getPassword() {

    return password;
  }

  @Override
  public boolean isTLS() {

    return tls;
  }

}
