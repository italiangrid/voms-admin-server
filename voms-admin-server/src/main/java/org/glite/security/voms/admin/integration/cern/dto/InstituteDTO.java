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
package org.glite.security.voms.admin.integration.cern.dto;

import java.util.Map;

public class InstituteDTO {

  String id;

  String name;

  String town;

  String country;

  public InstituteDTO() {
    // empty ctor
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return "InstituteDTO [id=" + id + ", name=" + name + ", town=" + town + ", country=" + country
        + "]";
  }

  public static InstituteDTO fromJson(Map<String, Object> map) {
    
    InstituteDTO institute = new InstituteDTO();
    institute.setId((String)map.get("id"));
    institute.setCountry((String)map.get("country"));
    institute.setName((String)map.get("name"));
    institute.setTown((String)map.get("town"));
    return institute;
  }

}
