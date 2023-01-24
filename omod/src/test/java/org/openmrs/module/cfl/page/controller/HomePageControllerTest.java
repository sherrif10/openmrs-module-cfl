/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.page.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.cfl.api.service.CustomUserAppService;
import org.openmrs.ui.framework.page.PageModel;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class HomePageControllerTest {

  @Mock private PageModel pageModel;

  @Mock private AppFrameworkService appFrameworkService;

  @Mock private UiSessionContext uiSessionContext;

  @Mock private UserContext userContext;

  @Mock private CustomUserAppService customUserAppService;

  @InjectMocks private HomePageController controller = new HomePageController();

  @Before
  public void setUp() {
    mockStatic(Context.class);

    when(Context.getAuthenticatedUser()).thenReturn(new User(1));
    when(Context.getUserContext()).thenReturn(userContext);
    when(Context.getService(CustomUserAppService.class)).thenReturn(customUserAppService);
  }

  @Test
  public void shouldAddAttributesToModel() {
    controller.controller(pageModel, appFrameworkService, uiSessionContext);

    verify(pageModel).addAttribute(eq("extensions"), anyListOf(Extension.class));
    verify(pageModel).addAttribute(eq("authenticatedUser"), any(User.class));
  }

  @Test
  public void shouldReturnHomePageForwardURL() {
    String actual = controller.overrideHomepage();

    assertNotNull(actual);
    assertEquals("forward:/cfl/home.page", actual);
  }
}
