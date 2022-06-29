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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Location;
import org.openmrs.Person;
import org.openmrs.PersonAttributeType;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class DoctorUserMetadataTest {

  @Mock private UserService userService;

  @Mock private PersonService personService;

  @Mock private LocationService locationService;

  @Mock private ProviderService providerService;

  @InjectMocks private DoctorUserMetadata doctorUserMetadata;

  @Before
  public void setUp() {
    mockStatic(Context.class);

    when(Context.getUserService()).thenReturn(userService);
    when(Context.getPersonService()).thenReturn(personService);
    when(Context.getLocationService()).thenReturn(locationService);
    when(Context.getProviderService()).thenReturn(providerService);
  }

  @Test
  public void shouldGetProperVersion() {
    int actual = doctorUserMetadata.getVersion();

    assertEquals(1, actual);
  }

  @Test
  public void shouldCreateTestDoctorAccountIfDefaultLocationExists() throws Exception {
    when(userService.getUserByUsername("admin")).thenReturn(new User(1));
    when(locationService.getLocation("CFL Clinic")).thenReturn(new Location(1));
    when(personService.getPersonAttributeTypeByName("EmailAttribute"))
        .thenReturn(buildTestPersonAttributeType("EmailAttribute"));
    when(personService.getPersonAttributeTypeByName("Telephone Number"))
        .thenReturn(buildTestPersonAttributeType("Telephone Number"));

    doctorUserMetadata.installNewVersion();

    verify(personService).getPersonAttributeTypeByName("EmailAttribute");
    verify(personService).getPersonAttributeTypeByName("Telephone Number");
    verify(personService).savePerson(any(Person.class));
    verify(providerService).saveProvider(any(Provider.class));
    verify(locationService).getLocation("CFL Clinic");
    verify(userService).createUser(any(User.class), anyString());
  }

  @Test
  public void shouldCreateTestDoctorAccountIfDefaultLocationDoesNotExist() throws Exception {
    when(userService.getUserByUsername("admin")).thenReturn(new User(1));
    when(locationService.getLocation("CFL Clinic")).thenReturn(null);
    when(locationService.getAllLocations()).thenReturn(buildTestLocationList());
    when(personService.getPersonAttributeTypeByName("EmailAttribute"))
        .thenReturn(buildTestPersonAttributeType("EmailAttribute"));
    when(personService.getPersonAttributeTypeByName("Telephone Number"))
        .thenReturn(buildTestPersonAttributeType("Telephone Number"));

    doctorUserMetadata.installNewVersion();

    verify(personService).getPersonAttributeTypeByName("EmailAttribute");
    verify(personService).getPersonAttributeTypeByName("Telephone Number");
    verify(personService).savePerson(any(Person.class));
    verify(providerService).saveProvider(any(Provider.class));
    verify(locationService).getLocation("CFL Clinic");
    verify(userService).createUser(any(User.class), anyString());
  }

  @Test(expected = EntityNotFoundException.class)
  public void shouldThrowExceptionWhenGivenPersonAttributeTypeDoesNotExist() throws Exception {
    when(userService.getUserByUsername("admin")).thenReturn(new User(1));
    when(personService.getPersonAttributeTypeByName("EmailAttribute")).thenReturn(null);
    when(personService.getPersonAttributeTypeByName("Telephone Number")).thenReturn(null);

    doctorUserMetadata.installNewVersion();
  }

  private PersonAttributeType buildTestPersonAttributeType(String attributeTypeName) {
    PersonAttributeType personAttributeType = new PersonAttributeType();
    personAttributeType.setName(attributeTypeName);
    return personAttributeType;
  }

  private List<Location> buildTestLocationList() {
    Location location = new Location();
    location.setName("NonDefaultLocationName");
    return Collections.singletonList(location);
  }
}
