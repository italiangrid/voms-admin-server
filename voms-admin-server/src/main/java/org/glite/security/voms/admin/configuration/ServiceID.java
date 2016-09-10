package org.glite.security.voms.admin.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.glite.security.voms.admin.error.VOMSException;


public class ServiceID {

  private ServiceID() {
  }

  public static String getServiceID(){
    try {
      String hostname = InetAddress.getLocalHost().getHostName();
      return String.format("%s:8443", hostname);
    } catch (UnknownHostException e) {
      throw new VOMSException("Error resolving local hostname", e);
    }
  }
}
