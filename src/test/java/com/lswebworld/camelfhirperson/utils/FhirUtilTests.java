package com.lswebworld.camelfhirperson.utils;

import static org.junit.Assert.assertNotNull;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import java.io.IOException;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Provenance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class FhirUtilTests {

  @Autowired
  FhirUtil util;

  @Autowired
  Hl7Util hl7Util;

  private static final String HL7_STRING = "MSH|^~\\&|AccMgr|1|||20050110045504||ADT^A01|"
      + "599102|P|2.3|||\r"
      + "EVN|A01|20050110045502|||||\r"
      + "PID|1||10006579^^^1^MRN^1||DUCK^DONALD^D||19241010|M||1|"
      + "111 DUCK ST^^FOWL^CA^999990000^^M|1|8885551212|8885551212|1|2||"
      + "40007716^^^AccMgr^VN^1|123121234|||||||||||NO\r"
      + "NK1|1|DUCK^HUEY|SO|3583 DUCK RD^^FOWL^CA^999990000|8885552222||Y||||||||||||||\r"
      + "PV1|1|I|PREOP^101^1^1^^^S|3|||37^DISNEY^WALT^^^^^^AccMgr^^^^CI|||01||||1|||"
      + "37^DISNEY^WALT^^^^^^AccMgr^^^^CI|2|40007716^^^AccMgr^VN|4"
      + "|||||||||||||||||||1||G|||20050110045253||||||\r"
      + "GT1|1|8291|DUCK^DONALD^D||111^DUCK ST^^FOWL^CA^999990000|"
      + "8885551212||19241010|M||1|123121234||||#Cartoon Ducks Inc|"
      + "111^DUCK ST^^FOWL^CA^999990000|8885551212||PT|\r"
      + "DG1|1|I9|71596^OSTEOARTHROS NOS-L/LEG ^I9|OSTEOARTHROS NOS-L/LEG ||A|\r"
      + "IN1|1|MEDICARE|3|MEDICARE|||||||Cartoon Ducks Inc|19891001|||4|DUCK^DONALD^D|1|"
      + "19241010|111^DUCK ST^^FOWL^CA^999990000|||||||||||||||||123121234A"
      + "||||||PT|M|111 DUCK ST^^FOWL^CA^999990000|||||8291\r"
      + "IN2|1||123121234|Cartoon Ducks Inc|||123121234A||||||"
      + "|||||||||||||||||||||||||||||||||||||||||||||||||||8885551212\r"
      + "IN1|2|NON-PRIMARY|9|MEDICAL MUTUAL CALIF.|PO BOX 94776^^HOLLYWOOD^CA^441414776||"
      + "8003621279|PUBSUMB|||Cartoon Ducks Inc||||7|"
      + "DUCK^DONALD^D|1|19241010|111 DUCK ST^^FOWL^CA^999990000|"
      + "||||||||||||||||056269770||||||PT|M|111^DUCK ST^^FOWL^CA^999990000|||||8291\r"
      + "IN2|2||123121234|Cartoon Ducks Inc||||||||||||||||"
      + "||||||||||||||||||||||||||||||||||||||||||||8885551212\r"
      + "IN1|3|SELF PAY|1|SELF PAY|||||||||||5||1\r";

  private Message hl7Message;
  private Bundle bundle;

  /**
   * Setup Method.
   */
  @Before
  public void setup() throws HL7Exception, IOException {
    hl7Message = hl7Util.toMessage(HL7_STRING);
    bundle = new Bundle();
  }

  @Test
  public void testCreatePatient() throws HL7Exception {
    assertNotNull(util.createPatient(hl7Message));
  }

  @Test
  public void testCreatePerson() throws HL7Exception {
    assertNotNull(util.createPerson(hl7Message));
  }

  @Test
  public void testCreateEncounter() throws HL7Exception {
    assertNotNull(util.createEncounter(hl7Message));
  }

  @Test
  public void testCreateBuilding() throws HL7Exception {
    assertNotNull(util.createLocation(hl7Message, "building"));
  }

  @Test
  public void testCreateRoom() throws HL7Exception {
    assertNotNull(util.createLocation(hl7Message, "room"));
  }

  @Test
  public void testCreateBed() throws HL7Exception {
    assertNotNull(util.createLocation(hl7Message, "bed"));
  }

  @Test
  public void testCreateDocumentReference() throws HL7Exception {
    assertNotNull(util.createDocument(hl7Message));
  }

  @Test
  public void testCreateAttachment() throws HL7Exception {
    assertNotNull(util.createAttachment(hl7Message));
  }

  @Test
  public void testCreateDevice() throws HL7Exception {
    assertNotNull(util.createDevice());
  }

  @Test
  public void testCreateBundleEntryEncounter() throws HL7Exception {
    Encounter encounter = util.createEncounter(hl7Message);
    assertNotNull(util.createEntry(encounter));
  } 

  @Test
  public void testCreateBundleEntryPatient() throws HL7Exception {
    Patient patient = util.createPatient(hl7Message);
    assertNotNull(util.createEntry(patient));
  }

  @Test
  public void testCreateBundleEntryLocation() throws HL7Exception {
    Location location = util.createLocation(hl7Message, "room");
    assertNotNull(util.createEntry(location));
  }

  @Test
  public void testCreateBundleEntryDocument() throws HL7Exception {
    DocumentReference document = util.createDocument(hl7Message);
    assertNotNull(util.createEntry(document));
  }

  @Test
  public void testCreateBundleEntryDevice() throws HL7Exception {
    Device device = util.createDevice();
    assertNotNull(util.createEntry(device));
  }

  @Test
  public void testCreateProvenance() throws HL7Exception {
    Patient patient = util.createPatient(hl7Message);
    DocumentReference document = util.createDocument(hl7Message);
    bundle.addEntry(util.createEntry(patient));
    bundle.addEntry(util.createEntry(document));
    assertNotNull(util.createProvenance(bundle));
  }

  @Test
  public void testCreateBundleEntryProvenance() throws HL7Exception {
    Patient patient = util.createPatient(hl7Message);
    DocumentReference document = util.createDocument(hl7Message);
    bundle.addEntry(util.createEntry(patient));
    bundle.addEntry(util.createEntry(document));
    Provenance provenance = util.createProvenance(bundle);
    assertNotNull(util.createEntry(provenance));
  }
}