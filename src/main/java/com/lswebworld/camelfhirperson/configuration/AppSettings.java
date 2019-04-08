package com.lswebworld.camelfhirperson.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppSettings {

  private String fhirServer;
  private String fhirVersion;
  private boolean fhirLogging;
  private String hl7Port;

  public String getFhirServer() {
    return fhirServer;
  }

  public void setFhirServer(String fhirServer) {
    this.fhirServer = fhirServer;
  }

  public String getFhirVersion() {
    return fhirVersion;
  }

  public void setFhirVersion(String fhirVersion) {
    this.fhirVersion = fhirVersion;
  }

  public boolean isFhirLogging() {
    return fhirLogging;
  }

  public void setFhirLogging(boolean fhirLogging) {
    this.fhirLogging = fhirLogging;
  }

  public String getHl7Port() {
    return hl7Port;
  }

  public void setHl7Port(String hl7Port) {
    this.hl7Port = hl7Port;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AppSettings) {
      AppSettings settings = (AppSettings)obj;
      return (settings.getFhirServer().equals(this.fhirServer) 
          && settings.getFhirVersion().equals(this.fhirVersion)
          && settings.getHl7Port().equals(this.hl7Port)
          && settings.isFhirLogging() == this.fhirLogging);
    } else {
      return false;
    }
  }
}