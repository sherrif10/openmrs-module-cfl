/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.metadata;

import org.openmrs.Location;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoctorUserMetadata extends VersionedMetadataBundle {

  private static final String GIVEN_NAME = "CfL";
  private static final String FAMILY_NAME = "Doctor";
  private static final String OTHER_GENDER = "O";
  private static final String EMAIL_ATTRIBUTE_TYPE_NAME = "EmailAttribute";
  private static final String EMAIL_VALUE = "cfl@cfl.xyz";
  private static final String PHONE_NUMBER_ATTRIBUTE_TYPE_NAME = "Telephone Number";
  private static final String PHONE_NUMBER_VALUE = "+15555551234";
  private static final String USERNAME = "cfldoctor1";
  private static final String PASSWORD = "Cfldoctor1";

  private static final String FORCE_PASSWORD_PROPERTY_NAME = "forcePassword";
  private static final String LOCATION_UUID_PROPERTY_NAME = "locationUuid";
  private static final String DOCTOR_ROLE = "Privilege Level: Doctor";
  private static final String LOCATION_NAME = "CFL Clinic";

  private UserService userService;
  private PersonService personService;
  private LocationService locationService;
  private ProviderService providerService;

  @Override
  public int getVersion() {
    return 1;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setPersonService(PersonService personService) {
    this.personService = personService;
  }

  public void setLocationService(LocationService locationService) {
    this.locationService = locationService;
  }

  public void setProviderService(ProviderService providerService) {
    this.providerService = providerService;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  public void installNewVersion() throws Exception {
    if (isDoctorUserDoesNotExist()) {
      setUpDoctorUser();
    }
  }

  private boolean isDoctorUserDoesNotExist() {
    return userService.getUserByUsername(USERNAME) == null;
  }

  private void setUpDoctorUser() {
    Person doctor = createDoctorPerson();
    createDoctorProvider(doctor);

    userService.createUser(createDoctorUser(doctor), PASSWORD);
  }

  private Person createDoctorPerson() {
    Person doctor = new Person();
    doctor.setGender(OTHER_GENDER);
    doctor.setNames(createDoctorNames(doctor));
    doctor.setAttributes(createDoctorAttributes(doctor));

    return personService.savePerson(doctor);
  }

  private Set<PersonName> createDoctorNames(Person person) {
    PersonName personName = new PersonName();
    personName.setGivenName(GIVEN_NAME);
    personName.setFamilyName(FAMILY_NAME);
    personName.setPerson(person);

    return Stream.of(personName).collect(Collectors.toCollection(TreeSet::new));
  }

  private Set<PersonAttribute> createDoctorAttributes(Person person) {
    return Stream.of(
            createPersonAttribute(person, EMAIL_ATTRIBUTE_TYPE_NAME, EMAIL_VALUE),
            createPersonAttribute(person, PHONE_NUMBER_ATTRIBUTE_TYPE_NAME, PHONE_NUMBER_VALUE))
        .collect(Collectors.toCollection(TreeSet::new));
  }

  private PersonAttribute createPersonAttribute(Person person, String attributeName, String value) {
    PersonAttributeType attributeType = personService.getPersonAttributeTypeByName(attributeName);
    if (attributeType == null) {
      throw new EntityNotFoundException(
          String.format("Person attribute type with name: %s not found", attributeName));
    }

    PersonAttribute personAttribute = new PersonAttribute();
    personAttribute.setPerson(person);
    personAttribute.setAttributeType(attributeType);
    personAttribute.setValue(value);

    return personAttribute;
  }

  private void createDoctorProvider(Person person) {
    Provider provider = new Provider();
    provider.setPerson(person);
    provider.setIdentifier(USERNAME);

    providerService.saveProvider(provider);
  }

  private User createDoctorUser(Person person) {
    User user = new User();
    user.setUsername(USERNAME);
    user.setPerson(person);
    user.setSystemId(USERNAME);
    user.setUserProperties(createUserProperties());
    user.setRoles(Collections.singleton(userService.getRole(DOCTOR_ROLE)));

    return user;
  }

  @SuppressWarnings("findsecbugs:HARD_CODE_PASSWORD")
  private Map<String, String> createUserProperties() {
    Map<String, String> userProperties = new HashMap<>();
    userProperties.put(FORCE_PASSWORD_PROPERTY_NAME, "false");
    userProperties.put(LOCATION_UUID_PROPERTY_NAME, getDoctorLocation().getUuid());

    return userProperties;
  }

  private Location getDoctorLocation() {
    Location location = locationService.getLocation(LOCATION_NAME);
    if (location != null) {
      return location;
    }

    return locationService.getAllLocations().get(0);
  }
}
