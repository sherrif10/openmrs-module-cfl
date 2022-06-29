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
import org.openmrs.module.cflcore.CFLConstants;
import org.openmrs.module.cfl.CfldistributionGlobalParameterConstants;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class CreateGlobalParametersActivatorStepTest {

  @Mock private AdministrationService administrationService;

  private Log log;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getAdministrationService()).thenReturn(administrationService);
  }

  @Test
  public void shouldCreateGlobalPropertiesIfNotExist() {
    new CreateGlobalParametersActivatorStep().startup(log);

    verify(administrationService)
        .getGlobalProperty(CfldistributionGlobalParameterConstants.CFL_DISTRO_BOOTSTRAPPED_KEY);
    verify(administrationService)
        .getGlobalProperty(
            CfldistributionGlobalParameterConstants.SHOULD_DISABLE_APPS_AND_EXTENSIONS_KEY);
    verify(administrationService)
        .getGlobalProperty(CFLConstants.LOCATION_ATTRIBUTE_GLOBAL_PROPERTY_NAME);
  }
}
