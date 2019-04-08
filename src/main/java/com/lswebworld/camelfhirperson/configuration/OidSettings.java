package com.lswebworld.camelfhirperson.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oid")
public class OidSettings {

  private String encounter;
  private String person;
  private String patient;
  private String building;
  private String room;
  private String bed;
  private String document;
  private String device;
  
  public String getEncounter() {
    return encounter;
  }

  public void setEncounter(String encounter) {
    this.encounter = encounter;
  }

  public String getPerson() {
    return person;
  }

  public void setPerson(String person) {
    this.person = person;
  }

  public String getPatient() {
    return patient;
  }

  public void setPatient(String patient) {
    this.patient = patient;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getBed() {
    return bed;
  }

  public void setBed(String bed) {
    this.bed = bed;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof OidSettings) {
      OidSettings oids = (OidSettings)obj;
      return (oids.getBed().equals(this.bed)
          && oids.getBuilding().equals(this.building)
          && oids.getEncounter().equals(this.encounter)
          && oids.getPatient().equals(this.patient)
          && oids.getPerson().equals(this.person)
          && oids.getRoom().equals(this.room)
          && oids.getDocument().equals(this.document)
          && oids.getDevice().equals(this.device));
    } else {
      return false;
    }
  }

  public String getDocument() {
    return document;
  }

  public void setDocument(String document) {
    this.document = document;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

}