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
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class LocationMetadataTest {

  @Mock private MetadataDeployService metadataDeployService;

  @Mock private UserService userService;

  @Mock private User user;

  @Mock private Location location;

  @Mock private LocationService locationService;

  @InjectMocks private LocationMetadata locationMetadata;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getUserService()).thenReturn(userService);
    when(Context.getLocationService()).thenReturn(locationService);
  }

  @Test
  public void shouldGetProperVersion() {
    int actual = locationMetadata.getVersion();

    assertEquals(1, actual);
  }

  @Test
  public void shouldInstallLocationMetadata() {
    when(userService.getUserByUsername("admin")).thenReturn(user);
    when(locationService.getLocationByUuid("8d6c993e-c2cc-11de-8d13-0010c6dffd0f"))
        .thenReturn(location);
    when(locationService.getLocationAttributeTypeByName("siteCode"))
        .thenReturn(createTestLocationAttributeType());

    locationMetadata.installNewVersion();

    verify(metadataDeployService).installObject(any(LocationTag.class));
    verify(user).setUserProperty("locationUuid", "8d6c993e-c2cc-11de-8d13-0010c6dffd0f");
    verify(location).setName("CFL Clinic");
    verify(location).setAttribute(any(LocationAttribute.class));
  }

  private LocationAttributeType createTestLocationAttributeType() {
    LocationAttributeType locationAttributeType = new LocationAttributeType();
    locationAttributeType.setName("siteCode");
    return locationAttributeType;
  }
}
