package com.lswebworld.camelfhirperson.utils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

import com.lswebworld.camelfhirperson.configuration.OidSettings;
import java.util.Date;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent;
import org.hl7.fhir.dstu3.model.DocumentReference.ReferredDocumentStatus;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Duration;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Person;
import org.hl7.fhir.dstu3.model.Provenance;
import org.hl7.fhir.dstu3.model.Provenance.ProvenanceAgentComponent;
import org.hl7.fhir.dstu3.model.Provenance.ProvenanceEntityComponent;
import org.hl7.fhir.dstu3.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FhirUtil {

  private OidSettings oids;
  private Hl7Util util;

  @Autowired
  public FhirUtil(OidSettings oids, Hl7Util util) {
    this.oids = oids;
    this.util = util;
  }

  /**
   * Creates a Fhir Encounter from an HL7 Message.
   */
  public Encounter createEncounter(Message hl7Message) throws HL7Exception {
    Identifier id = new Identifier();
    id.setSystem("urn:oid:" + oids.getEncounter());
    id.setUse(IdentifierUse.USUAL);
    id.setValue(util.getValue(hl7Message,"PV1-19-1"));

    Encounter encounter = new Encounter();
    encounter.addIdentifier(id);
    
    String admitDate = util.getValue(hl7Message, "PV1-44-1");
    String dischargeDate = util.getValue(hl7Message, "PV1-45-1");

    Date admit = util.toDate(admitDate);
    Date discharge = util.toDate(dischargeDate);

    Period period = new Period();
    if (admit != null) {
      period.setStart(admit);
      Duration duration = new Duration();
      if (discharge != null) {
        period.setEnd(discharge);
        duration.setValue(period.getEnd().getTime() - period.getStart().getTime());
      } else {
        duration.setValue(new Date().getTime() - period.getStart().getTime());
      }
      encounter.setPeriod(period);
      encounter.setLength(duration);
    }
    return encounter;
  } 

  /**
   * Creates a Fhir Patient from an HL7 Message.
   * @param hl7Message HL7 Message.
   * @return Fhir Patient
   */
  public Patient createPatient(Message hl7Message) throws HL7Exception {
    Identifier id = new Identifier();
    id.setSystem(oids.getPatient());
    id.setValue(util.getValue(hl7Message, "PID-3-1"));
    id.setUse(IdentifierUse.USUAL);

    Identifier ssn = new Identifier();
    ssn.setSystem("urn:oid:patient.ssn.oid");
    ssn.setValue(util.getValue(hl7Message, "PID-19-1"));
    ssn.setUse(IdentifierUse.OFFICIAL);

    Coding ssnType = new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "SS", "Social Security number");
    CodeableConcept ssnCode = new CodeableConcept();
    ssnCode.addCoding(ssnType);
    ssn.setType(ssnCode);

    Coding mrnType = new Coding("http://terminology.hl7.org/CodeSystem/v2-0203","MR","Medical record number");
    CodeableConcept code = new CodeableConcept();
    code.addCoding(mrnType);
    id.setType(code);

    HumanName name = new HumanName();
    name.setFamily(util.getValue(hl7Message, "PID-5-1"));
    name.addGiven(util.getValue(hl7Message, "PID-5-2"));
    name.addGiven(util.getValue(hl7Message, "PID-5-3"));

    Patient patient = new Patient();
    patient.addIdentifier(id);
    patient.addIdentifier(ssn);
    patient.addName(name);

    return patient;
  }


  /**
   * Creates a Fhir Person from an HL7 Message.
   * @param hl7Message HL7 Message
   * @return Fhir Person
   */
  public Person createPerson(Message hl7Message) throws HL7Exception {
    Identifier ssn = new Identifier();
    ssn.setSystem("urn:oid:patient.ssn.oid");
    ssn.setValue(util.getValue(hl7Message, "PID-19-1"));
    ssn.setUse(IdentifierUse.OFFICIAL);

    Coding ssnType = new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "SS", "Social Security number");
    CodeableConcept ssnCode = new CodeableConcept();
    ssnCode.addCoding(ssnType);
    ssn.setType(ssnCode);

    HumanName name = new HumanName();
    name.setFamily(util.getValue(hl7Message, "PID-5-1"));
    name.addGiven(util.getValue(hl7Message, "PID-5-2"));
    name.addGiven(util.getValue(hl7Message, "PID-5-3"));

    Person person = new Person();
    person.addIdentifier(ssn);
    person.addName(name);

    return person;
  }

  /**
   * Creates a Fhir Location of a provided Type from an HL7 Message.
   * @param hl7Message HL7 Message
   * @param type Type of Location
   * @return Fhir Location
   */
  public Location createLocation(Message hl7Message, String type) throws HL7Exception {
    
    Identifier id = new Identifier();
    Coding physicalType = new Coding("http://terminology.hl7.org/CodeSystem/location-physical-type","","");
    switch (type.toLowerCase()) {
      case "building": {
        id.setValue(util.getValue(hl7Message, "PV1-3-2"));
        id.setSystem(oids.getBuilding());
        physicalType.setCode("bu");
        physicalType.setDisplay("Building");
        break;
      }
      case "room": {
        id.setValue(util.getValue(hl7Message, "PV1-3-3"));
        String[] arr = oids.getRoom().split("\\.");
        String oid = arr[0].toString() + "." + util.getValue(hl7Message, "PV1-3-2");
        for (int i = 1; i < arr.length; i++) {
          if (i == arr.length) {
            oid += arr[i];
          } else {
            oid += arr[i] + ".";
          }
        }
        id.setSystem(oid);
        physicalType.setCode("ro");
        physicalType.setDisplay("Room");
        break;
      }
      case "bed": {
        id.setValue(util.getValue(hl7Message, "PV1-3-4"));
        String[] arr = oids.getBed().split("\\.");
        String oid = arr[0].toString() + "." + util.getValue(hl7Message, "PV1-3-3");
        for (int i = 1; i < arr.length; i++) {
          if (i == arr.length) {
            oid += arr[i];
          } else {
            oid += arr[i] + ".";
          }
        }
        id.setSystem(oid);
        physicalType.setCode("bd");
        physicalType.setDisplay("Bed");
        break;
      }
      default:
        break;
    }
    Location location = new Location();
    location.addIdentifier(id);
    CodeableConcept code = new CodeableConcept();
    code.addCoding(physicalType);
    location.setPhysicalType(code);

    return location;
  }

  /**
   * Uses a Fhir Bundle to create a Provenance.
   * @param bundle Fhir Bundle
   * @return Provenance
   */
  public Provenance createProvenance(Bundle bundle) {
    Provenance provenance = new Provenance();
    provenance.setRecorded(new Date());
  
    for (BundleEntryComponent entry : bundle.getEntry()) {
      if (entry.getResource() instanceof DocumentReference) {
        ProvenanceEntityComponent entity = new ProvenanceEntityComponent();
        entity.setWhat(new Reference("urn:uuid:" 
            + UuidFactory.getValue((DomainResource)entry.getResource())));  
        provenance.addEntity(entity);
      } else {
        if (entry.getResource() instanceof Device) {
          ProvenanceAgentComponent agent = new ProvenanceAgentComponent();
          agent.setWho(new Reference("urn:uuid:" 
              + UuidFactory.getValue((DomainResource)entry.getResource())));
          Coding agentType = new Coding(
              "http://terminology.hl7.org/CodeSystem/provenance-participant-type", "assembler", "Assembler");  
          Coding roleType = new Coding(
                "http://terminology.hl7.org/CodeSystem/contractsignertypecodes", "AGNT", "Agent");
          CodeableConcept agentCode = new CodeableConcept();
          agentCode.addCoding(agentType);
          CodeableConcept role = new CodeableConcept();
          role.addCoding(roleType);
          agent.addRole(role);
          agent.setRelatedAgentType(agentCode);
          provenance.addAgent(agent);
        } else {
          provenance.addTarget(new Reference("urn:uuid:" 
              + UuidFactory.getValue((DomainResource)entry.getResource())));
        }
      }

    }
    Coding reason = new Coding("http://terminology.hl7.org/CodeSystem/v3-ActReason", "HOPERAT", "healthcare operations");
    Coding activity = new Coding("http://terminology.hl7.org/CodeSystem/v3-DocumentCompletion", "UPDATE", "revise");

    provenance.setActivity(activity);
    provenance.addReason(reason);


    return provenance;
  }

  /**
   * Creates a Fhir Device.
   * @return Device
   */
  public Device createDevice() {
    Device device = new Device();
    Identifier id = new Identifier();
    id.setSystem(oids.getDevice());
    id.setValue("IngestionEngine");
    device.addIdentifier(id);
    return device;
  }

  /**
   * Creates a Document reference from an HL7 Message.
   * @param hl7Message HL7 Message
   * @return Document Reference
   */
  public DocumentReference createDocument(Message hl7Message) throws HL7Exception {
    Identifier id = new Identifier();
    id.setSystem(oids.getDocument());
    id.setValue(util.getValue(hl7Message, "MSH-10-1"));
    id.setUse(IdentifierUse.USUAL);

    DocumentReference document = new DocumentReference();
    document.setMasterIdentifier(id);
    document.setStatus(DocumentReferenceStatus.CURRENT);
    document.setDocStatus(ReferredDocumentStatus.FINAL);
    document.setCreated(new Date());
    document.setDescription("HL7 Provenance Document");
    DocumentReferenceContentComponent content = new DocumentReferenceContentComponent();
    content.setAttachment(createAttachment(hl7Message));
    content.setFormat(new Coding("http://ihe.net/fhir/ValueSet/IHE.FormatCode.codesystem", "urn:ihe:iti:xds:2017:mimeTypeSufficient", "mimeType Sufficient"));

    document.addContent(content);
    return document;
  }

  /**
   * Creates an Attachment from an HL7 Message.
   */
  public Attachment createAttachment(Message hl7Message) {
    Attachment attachment = new Attachment();
    attachment.setContentType("application/hl7-v2");
    attachment.setLanguage("en-US");
    attachment.setCreation(new Date());
    attachment.setData(hl7Message.toString().getBytes());
    attachment.setTitle("HL7 Message");
    attachment.setSize(hl7Message.toString().getBytes().length);
    return attachment;
  }

  /**
   * Creates a Bundle Entry for a Patient.
   * @param patient Patient
   * @return Bundle Entry
   */
  public BundleEntryComponent createEntry(Patient patient) {
    BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(patient);
    component.setFullUrl("urn:uuid:" + UuidFactory.getValue(patient));
    
    BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(HTTPVerb.POST);
    request.setUrl("Patient");
    request.setIfNoneExist("identifier=" 
        + patient.getIdentifierFirstRep().getSystem()
        + "|"
        + patient.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for a Person.
   * @param person Person
   * @return Bundle Entry
   */
  public BundleEntryComponent createEntry(Person person) {
    BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(person);
    component.setFullUrl("urn:uuid:" + UuidFactory.getValue(person));
    
    BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(HTTPVerb.POST);
    request.setUrl("Person");
    request.setIfNoneExist("identifier=" 
        + person.getIdentifierFirstRep().getSystem()
        + "|"
        + person.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for a Location.
   * @param location Location
   * @return Bundle Entry
   */
  public BundleEntryComponent createEntry(Location location) {
    BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(location);
    component.setFullUrl("urn:uuid:" + UuidFactory.getValue(location));
    
    BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(HTTPVerb.POST);
    request.setUrl("Location");
    request.setIfNoneExist("identifier=" 
        + location.getIdentifierFirstRep().getSystem()
        + "|"
        + location.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for a Device.
   * @param device Device
   * @return Bundle Entry
   */
  public BundleEntryComponent createEntry(Device device) {
    BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(device);
    component.setFullUrl("urn:uuid:" + UuidFactory.getValue(device));
    
    BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(HTTPVerb.POST);
    request.setUrl("Device");
    request.setIfNoneExist("identifier=" 
        + device.getIdentifierFirstRep().getSystem()
        + "|"
        + device.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for a Provenance.
   * @param provenance Provenance
   * @return Bundle Entry
   */
  public BundleEntryComponent createEntry(Provenance provenance) {
    BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(provenance);
    component.setFullUrl("urn:uuid:" + UuidFactory.getValue(provenance));
    
    BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(HTTPVerb.POST);
    request.setUrl("Provenance");
    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for an Encounter.
   * @param encounter Encounter
   * @return Bundle Entry
   */
  public BundleEntryComponent createEntry(Encounter encounter) {
    BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(encounter);
    component.setFullUrl("urn:uuid:" + UuidFactory.getValue(encounter));
    
    BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(HTTPVerb.PUT);
    request.setUrl("Encounter?identifier=" 
        + encounter.getIdentifierFirstRep().getSystem()
        + "|"
        + encounter.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }

  /**
   * Creates a Bundle Entry for a Document Reference.
   * @param document Document Reference
   * @return Bundle Entry
   */
  public BundleEntryComponent createEntry(DocumentReference document) {
    BundleEntryComponent component = new BundleEntryComponent();
    component.setResource(document);
    component.setFullUrl("urn:uuid:" + UuidFactory.getValue(document));
    
    BundleEntryRequestComponent request = new BundleEntryRequestComponent();
    request.setMethod(HTTPVerb.POST);
    request.setUrl("DocumentReference");
    request.setIfNoneExist("identifier=" 
        + document.getIdentifierFirstRep().getSystem()
        + "|"
        + document.getIdentifierFirstRep().getValue());
    component.setRequest(request);
    return component;
  }
}