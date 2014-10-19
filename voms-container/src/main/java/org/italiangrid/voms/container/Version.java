package org.italiangrid.voms.container;

public class Version {

  private Version() {

  }

  public static String version() {

    String version = Version.class.getPackage().getImplementationVersion();
    if (version == null)
      return "devel";
    return version;
  }

}
