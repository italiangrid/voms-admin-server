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
