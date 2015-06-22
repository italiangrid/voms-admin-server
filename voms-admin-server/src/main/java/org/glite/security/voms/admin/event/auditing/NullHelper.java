package org.glite.security.voms.admin.event.auditing;


public class NullHelper {

  private NullHelper() {
  }
  
  public static <T> String nullSafeValue(T val){
    if (val == null){
      return "<null>";
    }
    
    return val.toString();
  }

}
