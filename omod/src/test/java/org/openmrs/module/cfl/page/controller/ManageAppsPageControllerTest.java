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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({InfoErrorMessageUtil.class})
public class ManageAppsPageControllerTest {

  @Mock private AppFrameworkService appFrameworkService;

  @Mock private PageModel pageModel;

  @Mock private MockHttpSession httpSession;

  @Mock private UiUtils uiUtils;

  @InjectMocks private ManageAppsPageController controller;

  @Before
  public void setUp() {
    mockStatic(InfoErrorMessageUtil.class);
  }

  @Test
  public void get_shouldAddAttributesToModel() {
    when(appFrameworkService.getAllApps()).thenReturn(buildTestAppDescriptorList());
    when(appFrameworkService.getUserApps()).thenReturn(buildTestUserAppList());

    controller.get(pageModel, appFrameworkService);

    verify(appFrameworkService).getAllApps();
    verify(appFrameworkService).getAllEnabledApps();
    verify(appFrameworkService).getUserApps();
    verify(pageModel).addAttribute(eq("apps"), anyListOf(ManageAppsPageController.AppModel.class));
  }

  @Test
  public void post_shouldAddAttributesToModelWhenActionEqualsToEnable() {
    controller.post(pageModel, "testId", "enable", appFrameworkService, httpSession, uiUtils);

    verify(appFrameworkService).enableApp("testId");
    verifyStatic();
    InfoErrorMessageUtil.flashInfoMessage(eq(httpSession), anyString());
  }

  @Test
  public void post_shouldAddAttributesToModelWhenActionEqualsToDisable() {
    controller.post(pageModel, "testId", "disable", appFrameworkService, httpSession, uiUtils);

    verify(appFrameworkService).disableApp("testId");
    verifyStatic();
    InfoErrorMessageUtil.flashInfoMessage(eq(httpSession), anyString());
  }

  @Test
  public void post_shouldAddAttributesToModelWhenActionEqualsToDelete() {
    controller.post(pageModel, "testId", "delete", appFrameworkService, httpSession, uiUtils);

    verify(appFrameworkService).getUserApp("testId");
    verify(appFrameworkService).purgeUserApp(any(UserApp.class));
    verifyStatic();
    InfoErrorMessageUtil.flashInfoMessage(eq(httpSession), anyString());
  }

  @Test
  public void post_shouldSetSessionAndModelAttributesWhenExceptionIsThrown() {
    doThrow(new RuntimeException()).when(appFrameworkService).purgeUserApp(any(UserApp.class));

    controller.post(pageModel, "testId", "delete", appFrameworkService, httpSession, uiUtils);

    verify(appFrameworkService).getUserApp("testId");
    verify(appFrameworkService).purgeUserApp(any(UserApp.class));
    verify(httpSession).setAttribute(eq("emr.errorMessage"), anyString());
    verify(appFrameworkService).getAllApps();
    verify(appFrameworkService).getAllEnabledApps();
    verify(appFrameworkService).getUserApps();
    verify(pageModel).addAttribute(eq("apps"), anyListOf(ManageAppsPageController.AppModel.class));
  }

  @Test
  public void shouldTestAppModelGettersAndSetters() {
    ManageAppsPageController outerClassObj = new ManageAppsPageController();
    ManageAppsPageController.AppModel appModel = outerClassObj.new AppModel("testId", true, false);
    appModel.setId("updatedTestId");
    appModel.setEnabled(false);
    appModel.setBuiltIn(true);
    appModel.setCannotBeStopped(true);

    assertEquals("updatedTestId", appModel.getId());
    assertFalse(appModel.isEnabled());
    assertTrue(appModel.isBuiltIn());
    assertTrue(appModel.isCannotBeStopped());
  }

  private List<AppDescriptor> buildTestAppDescriptorList() {
    AppDescriptor appDescriptor = new AppDescriptor();
    appDescriptor.setId("testId");
    return Collections.singletonList(appDescriptor);
  }

  private List<UserApp> buildTestUserAppList() {
    UserApp userApp = new UserApp();
    userApp.setAppId("testId");
    userApp.setJson("{}");
    return Collections.singletonList(userApp);
  }
}
