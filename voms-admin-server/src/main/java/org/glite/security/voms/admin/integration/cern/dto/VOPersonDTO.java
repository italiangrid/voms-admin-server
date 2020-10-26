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


import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class VOPersonDTO {

  private Long id;
  private String name;
  private String firstName;
  private String department;
  private String group;
  private String sector;
  private String building;
  private String floor;
  private String room;
  private String tel1;
  private String tel2;
  private String tel3;
  private String portablePhone;
  private String beeper;
  private String email;
  private String physicalEmail;
  private Set<ParticipationDTO> participations;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getSector() {
    return sector;
  }

  public void setSector(String sector) {
    this.sector = sector;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getTel1() {
    return tel1;
  }

  public void setTel1(String tel1) {
    this.tel1 = tel1;
  }

  public String getTel2() {
    return tel2;
  }

  public void setTel2(String tel2) {
    this.tel2 = tel2;
  }

  public String getTel3() {
    return tel3;
  }

  public void setTel3(String tel3) {
    this.tel3 = tel3;
  }

  public String getPortablePhone() {
    return portablePhone;
  }

  public void setPortablePhone(String portablePhone) {
    this.portablePhone = portablePhone;
  }

  public String getBeeper() {
    return beeper;
  }

  public void setBeeper(String beeper) {
    this.beeper = beeper;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhysicalEmail() {
    return physicalEmail;
  }

  public void setPhysicalEmail(String physicalEmail) {
    this.physicalEmail = physicalEmail;
  }

  public Set<ParticipationDTO> getParticipations() {
    return participations;
  }

  public void setParticipations(Set<ParticipationDTO> participations) {
    this.participations = participations;
  }

  public Optional<ParticipationDTO> findValidParticipationForExperiment(Instant now,
      String experimentName) {
    
    return getParticipations().stream()
      .filter(p -> p.getExperiment().equalsIgnoreCase(experimentName) && p.isValidAtInstant(now))
      .findFirst();
  }

  @Override
  public String toString() {
    return "VOPersonDTO [id=" + id + ", name=" + name + ", firstName=" + firstName + ", department="
        + department + ", group=" + group + ", sector=" + sector + ", building=" + building
        + ", floor=" + floor + ", room=" + room + ", tel1=" + tel1 + ", tel2=" + tel2 + ", tel3="
        + tel3 + ", portablePhone=" + portablePhone + ", beeper=" + beeper + ", email=" + email
        + ", physicalEmail=" + physicalEmail + ", participations=" + participations + "]";
  }

  @SuppressWarnings("unchecked")
  public static VOPersonDTO fromJsonMap(Map<String, Object> json) {
    VOPersonDTO person = new VOPersonDTO();
    person.setId((Long) json.get("id"));
    person.setName((String) json.get("name"));
    person.setFirstName((String) json.get("firstName"));
    person.setEmail((String) json.get("email"));
    person.setPhysicalEmail((String) json.get("physicalEmail"));


    Object[] participations = (Object[]) json.get("participations");

    person.setParticipations(Stream.of(participations)
      .map(m -> ParticipationDTO.fromJsonMap((Map<String, Object>) m))
      .collect(Collectors.toSet()));
    return person;
  }

}
