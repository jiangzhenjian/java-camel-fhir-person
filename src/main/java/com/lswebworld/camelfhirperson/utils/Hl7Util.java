package com.lswebworld.camelfhirperson.utils;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class Hl7Util {

  /**
   * Parses a String into an HL7 Message.
   * @param hl7String HL7 String.
   * @return HL7 Message
   */
  public Message toMessage(String hl7String) throws HL7Exception, IOException {
    HapiContext ctx = new DefaultHapiContext(new CanonicalModelClassFactory());
    Parser parser = ctx.getGenericParser();

    Message message = parser.parse(hl7String);
    ctx.close();
    return message;
  }

  /**
   * Retrieves a value from an HL7 Message based on an HL7 Path.
   * @param message HL7 Message
   * @param path Path
   * @return String value
   */
  public String getValue(Message message, String path) throws HL7Exception {
    Terser terser = new Terser(message);
    return terser.get(path);
  }

  /**
   * Converts an HL7 Message value to a Date Time.
   * @param value String value
   * @return Date
   */
  public Date toDate(String value) {
    SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddhhmmss");
    try {
      Date date = sdt.parse(value);
      return date;
    } catch (Exception ex) {
      return null;
    }
  }
}