/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.util.version;

import java.util.regex.Pattern;

public class VersionNumber implements EndpointVersion {

  private static final String versionRegex = "\\d+\\.\\d+\\.\\d+$";
  private static final Pattern versionPattern = Pattern.compile(versionRegex);

  private String version;

  private int majorVersionNumber = 0;
  private int minorVersionNumber = 0;
  private int patchVersionNumber = 1;

  private void parseVersion() {

    if (!versionPattern.matcher(version).matches())
      throw new IllegalArgumentException(
        "voms version number must comply to the following format: MAJOR.MINOR.PATCH.");

    String tokens[] = version.split("\\.");

    if (tokens.length != 3)
      throw new IllegalArgumentException(
        "voms version numbers must comply to the following format: MAJOR.MINOR.PATCH.");

    majorVersionNumber = Integer.valueOf(tokens[0]);
    minorVersionNumber = Integer.valueOf(tokens[1]);
    patchVersionNumber = Integer.valueOf(tokens[2]);
  }

  public VersionNumber(String versionString) {

    assert versionString != null : "Cannot create a VersionNumber from a null string!";
    version = versionString;

    parseVersion();

  }

  public int getMajorVersionNumber() {

    return majorVersionNumber;
  }

  public int getMinorVersionNumber() {

    return minorVersionNumber;
  }

  public int getPatchVersionNumber() {

    return patchVersionNumber;
  }

  public String getVersion() {

    return version;
  }

  public String toString() {

    return version;
  }
}
