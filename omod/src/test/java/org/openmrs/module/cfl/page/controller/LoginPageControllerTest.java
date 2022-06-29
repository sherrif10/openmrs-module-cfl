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
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.cfl.CflWebConstants;
import org.openmrs.module.cfl.api.service.UserNotAuthorizedService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.session.Session;
import org.openmrs.web.user.CurrentUsers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, CurrentUsers.class})
public class LoginPageControllerTest {

  @Mock private User user;

  @Mock private UserContext userContext;

  @Mock private PageModel pageModel;

  @Mock private PageRequest pageRequest;

  @Mock private UiUtils uiUtils;

  @Mock private LocationService locationService;

  @Mock private AppFrameworkService appFrameworkService;

  @Mock private AdministrationService administrationService;

  @Mock private HttpServletRequest httpServletRequest;

  @Mock private Session session;

  @Mock private HttpSession httpSession;

  @Mock private HttpServletResponse httpServletResponse;

  @Mock private UiSessionContext uiSessionContext;

  @Mock private UserNotAuthorizedService userNotAuthorizedService;

  @InjectMocks private LoginPageController controller;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    mockStatic(CurrentUsers.class);
    when(Context.getUserContext()).thenReturn(userContext);
    when(Context.getAdministrationService()).thenReturn(administrationService);
    when(pageRequest.getRequest()).thenReturn(httpServletRequest);
    when(pageRequest.getSession()).thenReturn(session);
    when(httpServletRequest.getSession()).thenReturn(httpSession);
    when(pageRequest.getResponse()).thenReturn(httpServletResponse);
    when(Context.getAuthenticatedUser()).thenReturn(user);
    when(Context.getService(UserNotAuthorizedService.class)).thenReturn(userNotAuthorizedService);

    when(administrationService.getGlobalProperty("referenceapplication.locationUserPropertyName"))
        .thenReturn("11aab4c5-f203-11ec-b5e1-0242ac130002");
    when(user.getUserProperty("11aab4c5-f203-11ec-b5e1-0242ac130002"))
        .thenReturn("testUuid1,testUuid2");
    when(locationService.getLocationByUuid("testUuid1")).thenReturn(new Location(1));
    when(locationService.getLocationByUuid("testUuid2")).thenReturn(new Location(2));
    when(appFrameworkService.getLoginLocations())
        .thenReturn(Collections.singletonList(buildTestLocation()));
    when(Context.isAuthenticated()).thenReturn(false).thenReturn(true);
  }

  @Test
  public void get_shouldOverrideLoginPage() {
    String actual = controller.overrideLoginPageGet();

    assertEquals("forward:/cfl/login.page", actual);
  }

  @Test
  public void post_shouldOverrideLoginPage() {
    String actual = controller.overrideLoginPagePost();

    assertEquals("forward:/cfl/login.page", actual);
  }

  @Test
  public void get_shouldReturnNullMethodUrlAndAddAttributesToModelWhenUserIsNotAuthenticated() {
    when(Context.isAuthenticated()).thenReturn(false);
    when(session.getAttribute("manual-logout", String.class)).thenReturn("true");

    String actual =
        controller.get(
            pageModel,
            uiUtils,
            pageRequest,
            "1",
            locationService,
            appFrameworkService,
            administrationService);

    assertNull(actual);
    verifyBasicAssertions();
    verify(pageModel).addAttribute(eq("redirectUrl"), anyString());
    verify(pageModel).addAttribute(eq("showSessionLocations"), any(Boolean.class));
    verify(pageModel).addAttribute(eq("selectLocation"), any(Boolean.class));
    verify(pageModel).addAttribute(eq("lastSessionLocation"), any(Location.class));
    verify(pageModel).addAttribute(eq("isStagingEnvironment"), any(Boolean.class));
  }

  @Test
  public void get_shouldReturnEmptyMethodUrlWhenUserIsAuthenticated() {
    when(httpSession.getAttribute(any())).thenReturn("https://host/openmrs/logi.html");
    when(httpServletRequest.getContextPath()).thenReturn("/openmrs");
    when(Context.isAuthenticated()).thenReturn(true);
    when(httpServletRequest.getParameter(
            CflWebConstants.REQUEST_PARAMETER_NAME_REDIRECT_URL))
        .thenReturn("http://host/openmrs/login.htm");

    String actual =
        controller.get(
            pageModel,
            uiUtils,
            pageRequest,
            "1",
            locationService,
            appFrameworkService,
            administrationService);

    assertEquals("redirect:/openmrs/login.htm", actual);
    verifyStatic();
    Context.isAuthenticated();
    verify(userContext).getLocationId();
  }

  @Test
  public void post_shouldProcessRequestWhenUserIsInitiallyNotAuthenticated() {
    when(httpSession.getAttribute(any())).thenReturn("https://host/openmrs/logi.html");
    when(httpServletRequest.getContextPath()).thenReturn("/openmrs");

    controller.post(
        "abc",
        "xyz",
        1,
        locationService,
        administrationService,
        uiUtils,
        appFrameworkService,
        pageRequest,
        uiSessionContext);

    verifyBasicAssertions();
    verifyStatic(times(2));
    Context.isAuthenticated();
    verifyStatic();
    Context.authenticate("abc", "xyz");
    verify(administrationService, times(2))
        .getGlobalProperty("referenceapplication.locationUserPropertyName");
    verifyStatic(times(3));
    Context.getAuthenticatedUser();
    verify(locationService, times(4)).getLocationByUuid(anyString());
    verify(appFrameworkService).getLoginLocations();
  }

  @Test
  public void post_shouldProcessRequestWhenExceptionIsThrownDuringAuthentication() {
    when(userNotAuthorizedService.getUser(anyString())).thenReturn(new User(1));

    PowerMockito.doThrow(new ContextAuthenticationException()).when(Context.class);
    Context.authenticate(anyString(), anyString());

    controller.post(
        "abc",
        "xyz",
        1,
        locationService,
        administrationService,
        uiUtils,
        appFrameworkService,
        pageRequest,
        uiSessionContext);

    verifyBasicAssertions();
    verify(locationService).getLocation(1);
    verifyStatic();
    Context.isAuthenticated();
    verifyStatic();
    Context.authenticate("abc", "xyz");
    verify(session).setAttribute(eq("_REFERENCE_APPLICATION_ERROR_MESSAGE_"), anyString());
    verify(userNotAuthorizedService).getUser(anyString());
    verify(session).setAttribute(eq("_REFERENCE_APPLICATION_REDIRECT_URL_"), anyString());
    verifyStatic();
    Context.logout();
  }

  private void verifyBasicAssertions() {
    verifyStatic();
    Context.addProxyPrivilege("View Locations");
    verifyStatic();
    Context.addProxyPrivilege("Get Locations");
    verifyStatic();
    Context.removeProxyPrivilege("Get Locations");
    verifyStatic();
    Context.removeProxyPrivilege("Get Locations");
  }

  private Location buildTestLocation() {
    Location location = new Location();
    location.setLocationId(100);
    location.setName("testName");
    LocationTag locationTag = new LocationTag();
    locationTag.setName("Login Location");
    location.addTag(locationTag);
    return location;
  }
}
