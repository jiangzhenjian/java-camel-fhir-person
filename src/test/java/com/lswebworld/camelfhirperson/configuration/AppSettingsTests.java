package com.lswebworld.camelfhirperson.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppSettingsTests {

  private AppSettings settings;
  private AppSettings settings2;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    settings = new AppSettings();
    settings.setFhirLogging(true);
    settings.setFhirServer("http://localhost/fhir");
    settings.setFhirVersion("STU3");
    settings.setHl7Port("6666");

    settings2 = new AppSettings();
    settings2.setFhirLogging(false);
    settings2.setFhirServer("http://localhost:8086/fhir");
    settings2.setFhirVersion("R4");
    settings2.setHl7Port("6667"); 
  }

  @Test
  public void testGetFhirServer() {
    assertEquals("http://localhost/fhir", settings.getFhirServer());
  }

  @Test
  public void testGetFhirLogging() {
    assertEquals(true, settings.isFhirLogging());
  }

  @Test
  public void testGetFhirVersion() {
    assertEquals("STU3", settings.getFhirVersion());
  }

  @Test
  public void testGetHl7Port() {
    assertEquals("6666", settings.getHl7Port());
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