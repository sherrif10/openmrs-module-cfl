/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.activator.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cfl.api.metadata.UpdateGlobalParametersMetadata;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class UpdateGlobalParametersMetadataTest {

  @Mock private AdministrationService administrationService;

  @InjectMocks private UpdateGlobalParametersMetadata updateGlobalParametersMetadata;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getAdministrationService()).thenReturn(administrationService);
  }

  @Test
  public void shouldReturnProperVersion() {
    int actual = updateGlobalParametersMetadata.getVersion();

    assertEquals(5, actual);
  }

  @Test
  public void shouldUpdateGlobalProperties() {
    new UpdateGlobalParametersMetadata().installNewVersion();

    verify(administrationService, times(9)).getGlobalPropertyObject(anyString());
  }
}
