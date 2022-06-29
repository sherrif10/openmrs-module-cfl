/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.advice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.Patient;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.module.cfl.api.metadata.LocationMetadata;
import org.openmrs.test.BaseContextMockTest;

import java.lang.reflect.Method;

public class DefaultLocationPropertiesAdviceTest extends BaseContextMockTest {
  private final LocationTag loginLocation = new LocationTag();
  private final LocationTag visitLocation = new LocationTag();

  @Mock private LocationService locationService;

  @Before
  public void setupMocks() {
    Mockito.when(locationService.getLocationTagByName(LocationMetadata.LOGIN_LOCATION_TAG_NAME))
        .thenReturn(loginLocation);
    Mockito.when(locationService.getLocationTagByName(LocationMetadata.VISIT_LOCATION_TAG_NAME))
        .thenReturn(visitLocation);
  }

  @Test
  public void before_shouldAddLocationTags() throws NoSuchMethodException {
    final Method saveMethod = LocationService.class.getMethod("saveLocation", Location.class);

    final Location location = new Location();

    final DefaultLocationPropertiesAdvice advice = new DefaultLocationPropertiesAdvice();
    advice.before(saveMethod, new Object[]{location}, locationService);

    Mockito.verify(locationService).getLocationTagByName(LocationMetadata.LOGIN_LOCATION_TAG_NAME);
    Mockito.verify(locationService).getLocationTagByName(LocationMetadata.VISIT_LOCATION_TAG_NAME);
    Assert.assertEquals(2, location.getTags().size());
    Assert.assertTrue(location.getTags().contains(loginLocation));
    Assert.assertTrue(location.getTags().contains(visitLocation));
  }

  @Test
  public void before_shouldNoAddMoreLocationTagsThenNeeded() throws NoSuchMethodException {
    final Method saveMethod = LocationService.class.getMethod("saveLocation", Location.class);
    final Location location = new Location();
    location.addTag(visitLocation);

    final DefaultLocationPropertiesAdvice advice = new DefaultLocationPropertiesAdvice();
    advice.before(saveMethod, new Object[]{location}, locationService);

    Mockito.verify(locationService).getLocationTagByName(LocationMetadata.LOGIN_LOCATION_TAG_NAME);
    Mockito.verify(locationService).getLocationTagByName(LocationMetadata.VISIT_LOCATION_TAG_NAME);
    Assert.assertEquals(2, location.getTags().size());
    Assert.assertTrue(location.getTags().contains(loginLocation));
    Assert.assertTrue(location.getTags().contains(visitLocation));
  }

  @Test
  public void before_shouldNotAddLocationTags() throws NoSuchMethodException {
    final Method saveMethod = LocationService.class.getMethod("saveLocation", Location.class);

    final Location location = new Location();
    location.setId(1);
    location.addTag(visitLocation);

    final DefaultLocationPropertiesAdvice advice = new DefaultLocationPropertiesAdvice();
    advice.before(saveMethod, new Object[]{location}, locationService);

    Assert.assertEquals(1, location.getTags().size());
  }

  @Test
  public void before_shouldNotExecuteOnMethodFromOtherService() throws NoSuchMethodException {
    final Method wrongMethod = PatientService.class.getMethod("savePatient", Patient.class);

    final Location location = new Location();

    final DefaultLocationPropertiesAdvice advice = new DefaultLocationPropertiesAdvice();
    advice.before(wrongMethod, new Object[]{location}, locationService);

    Mockito.verifyZeroInteractions(locationService);
  }
}
