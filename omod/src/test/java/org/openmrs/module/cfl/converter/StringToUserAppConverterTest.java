/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.converter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class StringToUserAppConverterTest {

  @Mock private AppFrameworkService appFrameworkService;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getService(AppFrameworkService.class)).thenReturn(appFrameworkService);
  }

  @Test
  public void shouldConvertAppIdToUserAppWhenAppIdIsNotNull() {
    new StringToUserAppConverter().convert("testAppId");

    verify(appFrameworkService).getUserApp("testAppId");
  }

  @Test
  public void shouldNotConvertAppIdToUserAppWhenAppIdIsNull() {
    UserApp actual = new StringToUserAppConverter().convert(null);

    assertNull(actual);
  }
}
