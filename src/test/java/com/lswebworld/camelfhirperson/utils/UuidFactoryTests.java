package com.lswebworld.camelfhirperson.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Provenance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UuidFactoryTests {

  private Patient patient;
  private Provenance provenance;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    UuidFactory.clear();

    patient = new Patient();
    Identifier id = new Identifier();
    id.setSystem("TEST.MRN.OID");
    id.setValue("00983635");
    patient.addIdentifier(id);

    provenance = new Provenance();
  }

  @Test
  public void testAddKey() {
    //UUID guid = UuidFactory.addKey("MyKey");
    assertNotNull(UuidFactory.addKey("MyKey"));
  }

  @Test
  public void testAddKeyResource() {
    assertNotNull(UuidFactory.addKey(patient));
  }

  @Test
  public void testAddKeyResourceNoId() {
    assertNotNull(UuidFactory.addKey(provenance));
  }

  @Test
  public void testGetValueKey() {
    UUID guid = UuidFactory.addKey("MyKey");
    assertEquals(guid, UuidFactory.getValue("MyKey"));
  }

  @Test
  public void testGetValueResource() {
    UUID guid = UuidFactory.addKey(patient);
    assertEquals(guid, UuidFactory.getValue(patient));
  }

  @Test
  public void testGetValueResourceNoId() {
    UUID guid = UuidFactory.addKey(provenance);
    assertEquals(guid, UuidFactory.getValue(provenance));
  }

}