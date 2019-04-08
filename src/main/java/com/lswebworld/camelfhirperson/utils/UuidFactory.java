package com.lswebworld.camelfhirperson.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Identifier;
import org.springframework.stereotype.Service;

@Service
public class UuidFactory {

  private static HashMap<String, UUID> guids = new HashMap<String, UUID>();

  /**
   * Adds a New Guid to the Listing.
   * @param key Key Value.
   * @return UUID
   */
  public static UUID addKey(String key) {
    if (guids.containsKey(key)) {
      return guids.get(key);
    }
    guids.put(key, UUID.randomUUID());
    return guids.get(key);
  }

  /**
   * Adds a new Guid to the Listing for a Fhir Domain Resoucre.
   */
  public static UUID addKey(DomainResource resource) {
    try {
      Method method = resource.getClass().getMethod("getIdentifierFirstRep");
      Identifier id = (Identifier)method.invoke(resource);
      String key = id.getSystem() + "|" + id.getValue();
      if (guids.containsKey(key)) {
        return guids.get(key);
      } else {
        guids.put(key, UUID.randomUUID());
        return guids.get(key);
      }
    } catch (Exception ex) {
      String key = resource.getClass().getName().split("\\.")[0];
      if (guids.containsKey(key)) {
        return guids.get(key);
      } 
      guids.put(key, UUID.randomUUID());
      return guids.get(key);
    }
  }

  /**
   * Returns a Guid from the Listing for a given Key.
   * @param key Key
   * @return UUID
   */
  public static UUID getValue(String key) {
    return addKey(key);
  }

  /**
   * Returns a Guid from the Listing for a given Fhir Domain Resource.
   * @param resource Fhir Resource
   * @return UUID
   */
  public static UUID getValue(DomainResource resource) {
    return addKey(resource);
  }

  /**
   * Clears the listing.
   */
  public static void clear() {
    guids.clear();
  }
}