package com.lswebworld.camelfhirperson.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OidSettingsTests {

  private OidSettings settings;
  private OidSettings settings2;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    settings = new OidSettings();
    settings.setBed("test.bed.oid");
    settings.setBuilding("test.building.oid");
    settings.setEncounter("test.encounter.oid");
    settings.setPatient("test.mrn.oid");
    settings.setPerson("test.empi.oid");
    settings.setRoom("test.room.oid");
    settings.setDocument("test.document.oid");
    settings.setDevice("test.device.oid");

    settings2 = new OidSettings();
    settings2.setBed("dev.bed.oid");
    settings2.setBuilding("dev.building.oid");
    settings2.setEncounter("dev.encounter.oid");
    settings2.setPatient("dev.mrn.oid");
    settings2.setPerson("dev.empi.oid");
    settings2.setRoom("dev.room.oid");
    settings2.setDocument("dev.document.oid");
    settings2.setDevice("dev.device.oid");
  }

  @Test
  public void testGetBed() {
    assertEquals("test.bed.oid", settings.getBed());
  }

  @Test
  public void testGetBuilding() {
    assertEquals("test.building.oid", settings.getBuilding());
  }

  @Test
  public void testGetEncounter() {
    assertEquals("test.encounter.oid", settings.getEncounter());
  }

  @Test
  public void testGetPatient() {
    assertEquals("test.mrn.oid", settings.getPatient());
  }

  @Test
  public void testGetPerson() {
    assertEquals("test.empi.oid", settings.getPerson());
  }

  @Test
  public void testGetRoom() {
    assertEquals("test.room.oid", settings.getRoom());
  }

  @Test
  public void testGetDevice() {
    assertEquals("test.device.oid", settings.getDevice());
  }

  @Test
  public void testGetDocument() {
    assertEquals("test.document.oid", settings.getDocument());
  }

  @Test
  public void testEquals() {
    assertTrue(settings.equals(settings));
  }

  @Test
  public void testNotEquals() {
    assertFalse(settings.equals(settings2));
  }

  @Test
  public void testNotEqualsWrongObject() {
    assertFalse(settings.equals(new Object()));
  }
}