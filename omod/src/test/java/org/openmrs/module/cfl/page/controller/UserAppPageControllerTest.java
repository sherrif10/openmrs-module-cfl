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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class UserAppPageControllerTest {

  @Mock private PageModel pageModel;

  @Mock private AppFrameworkService appFrameworkService;

  @Mock private MockHttpSession httpSession;

  @Mock private UiUtils uiUtils;

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private UserAppPageController userAppPageController;

  @Test
  public void get_userAppPageController() {
    userAppPageController.get(pageModel, new UserApp());

    verify(pageModel).addAttribute(eq("userApp"), any(UserApp.class));
  }

  @Test
  public void post_shouldSetProperSessionAttributesWhenIdsDoNotMatch() throws IOException {
    UserApp userApp = buildTestUserApp("userAppTestId");
    AppDescriptor appDescriptor = buildTestAppDescriptor("otherTestId");
    when(objectMapper.readValue(userApp.getJson(), AppDescriptor.class)).thenReturn(appDescriptor);

    userAppPageController.post(
        pageModel, userApp, "test", appFrameworkService, httpSession, uiUtils);

    verify(httpSession).setAttribute(eq("emr.errorMessage"), anyString());
  }

  @Test
  public void post_shouldSetProperSessionAttributesWhenAddActionPerformed() throws IOException {
    UserApp userApp = buildTestUserApp("testId");
    AppDescriptor appDescriptor = buildTestAppDescriptor("testId");
    when(objectMapper.readValue(userApp.getJson(), AppDescriptor.class)).thenReturn(appDescriptor);
    when(appFrameworkService.getUserApp(anyString())).thenReturn(userApp);

    userAppPageController.post(
        pageModel, userApp, "add", appFrameworkService, httpSession, uiUtils);

    verify(httpSession).setAttribute(eq("emr.errorMessage"), anyString());
  }

  @Test
  public void post_shouldSaveUserApp() throws IOException {
    UserApp userApp = buildTestUserApp("testId");
    AppDescriptor appDescriptor = buildTestAppDescriptor("testId");
    when(objectMapper.readValue(userApp.getJson(), AppDescriptor.class)).thenReturn(appDescriptor);
    when(appFrameworkService.getUserApp(anyString())).thenReturn(userApp);

    userAppPageController.post(
        pageModel, userApp, "invalidAction", appFrameworkService, httpSession, uiUtils);

    verify(appFrameworkService).saveUserApp(any(UserApp.class));
  }

  @Test
  public void post_shouldSetProperSetAttributesWhenAppDescriptorIsNull() throws IOException {
    UserApp userApp = buildTestUserApp("testId");
    when(objectMapper.readValue(userApp.getJson(), AppDescriptor.class)).thenReturn(null);
    when(appFrameworkService.getUserApp(anyString())).thenReturn(userApp);

    userAppPageController.post(
        pageModel, userApp, "invalidAction", appFrameworkService, httpSession, uiUtils);

    verify(httpSession).setAttribute(eq("emr.errorMessage"), anyString());
  }

  @Test
  public void verifyJson_whenExceptionIsThrown() throws IOException {
    when(objectMapper.readValue(anyString(), eq(AppDescriptor.class)))
        .thenThrow(JsonParseException.class);

    SimpleObject actual = userAppPageController.verifyJson(anyString());

    assertFalse(actual.get("isValid"));
  }

  @Test
  public void verifyJson_whenStringIsNotValid() {
    String validJsonString = "[{}]";

    SimpleObject actual = userAppPageController.verifyJson(validJsonString);

    assertTrue(actual.get("isValid"));
  }

  private UserApp buildTestUserApp(String appId) {
    UserApp userApp = new UserApp();
    userApp.setAppId(appId);
    userApp.setJson("{}");
    return userApp;
  }

  private AppDescriptor buildTestAppDescriptor(String appId) {
    AppDescriptor appDescriptor = new AppDescriptor();
    appDescriptor.setId(appId);
    return appDescriptor;
  }
}
