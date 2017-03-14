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
package org.glite.security.voms.admin.persistence.dao.generic;

import java.net.URL;

import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

public interface AUPDAO extends NamedEntityDAO<AUP, Long> {

  public AUPVersion setActiveVersion(AUP aup, String version);

  public AUPVersion addVersion(AUP aup, String version, URL url);

  public AUPVersion addVersion(AUP aup, String version, String text);

  public AUPVersion removeVersion(AUP aup, String version);

  public AUP getGridAUP();

  public AUP getVOAUP();

  public AUP createGridAUP(String description, String version, URL url);

  public AUP createGridAUP(String description, String version, String text);

  public AUP createVOAUP(String description, String version, URL url);

  public AUP createVOAUP(String description, String version, String text);

}
