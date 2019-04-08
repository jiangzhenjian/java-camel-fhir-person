package com.lswebworld.camelfhirperson.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Hl7UtilTests {

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

  private Hl7Util util;

  /**
   * Setup Method.
   */
  @Before
  public void setup() {
    util = new Hl7Util();
  }

  @Test
  public void testToMessage() throws HL7Exception, IOException {
    assertNotNull(util.toMessage(HL7_STRING));
  }

  @Test
  public void testGetValue() throws HL7Exception, IOException {
    Message message = util.toMessage(HL7_STRING);
    assertEquals("PREOP",util.getValue(message, "PV1-3-1"));
  }

  @Test
  public void testToDate() {
    String value = "20050110045253";
    assertNotNull(util.toDate(value));
  }

  @Test
  public void testToDateBadDate() {
    String value = "200513010452";
    assertNull(util.toDate(value));
  }
}