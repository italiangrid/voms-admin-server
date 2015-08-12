package org.glite.security.voms.admin.persistence;


public class SchemaVersion {

  /**
   * Legacy VOMS core database version.
   */
  public static final Integer VOMS_DB_VERSION = new Integer(3);
  
  /**
   * Current VOMS Admin database version.
   */
  public static final String VOMS_ADMIN_DB_VERSION = "4";
  
  private SchemaVersion() {}

}
