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

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class ConfigurePatientDashboardAppsActivatorStepTest {

  @Mock private AdministrationService administrationService;

  @Mock private AppFrameworkService appFrameworkService;

  private Log log;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getAdministrationService()).thenReturn(administrationService);
    when(Context.getService(AppFrameworkService.class)).thenReturn(appFrameworkService);
  }

  @Test
  public void shouldEnableDefaultConfiguration() {
    when(administrationService.getGlobalProperty("cfl.shouldDisableAppsAndExtensions"))
        .thenReturn("false");

    new ConfigurePatientDashboardAppsActivatorStep().startup(log);

    verify(appFrameworkService).disableExtension(anyString());
    verify(appFrameworkService, times(3)).disableApp(anyString());
  }

  @Test
  public void shouldEnableAdditionalConfiguration() {
    when(administrationService.getGlobalProperty("cfl.shouldDisableAppsAndExtensions"))
        .thenReturn("true");

    new ConfigurePatientDashboardAppsActivatorStep().startup(log);

    verify(appFrameworkService, times(3)).enableApp(anyString());
    verify(appFrameworkService, times(11)).disableApp(anyString());
    verify(appFrameworkService, times(15)).disableExtension(anyString());
  }
}
