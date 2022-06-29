/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.ui.framework.UiUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class CfldistributionRequireLoginLocationFilterTest {

  @Mock private UiUtils uiUtils;

  @Mock private MockHttpServletRequest servletRequest;

  @Mock private MockHttpServletResponse servletResponse;

  @Mock private FilterChain filterChain;

  @Mock private UserContext userContext;

  @InjectMocks private CfldistributionRequireLoginLocationFilter filter;

  @Before
  public void setUp() {
    mockStatic(Context.class);

    when(Context.getUserContext()).thenReturn(userContext);
    when(Context.getRegisteredComponent("uiUtils", UiUtils.class)).thenReturn(uiUtils);
  }

  @Test
  public void shouldFilterRequestsWhenUserIsAuthenticated() throws ServletException, IOException {
    when(Context.isAuthenticated()).thenReturn(true);
    when(userContext.getLocationId()).thenReturn(null);
    when(servletRequest.getRequestURI()).thenReturn("/home.page");

    filter.doFilter(servletRequest, servletResponse, filterChain);

    verify(userContext).getLocationId();
    verify(servletResponse).setStatus(HttpStatus.OK.value());
    verify(servletResponse).sendRedirect(anyString());
  }

  @Test
  public void shouldFilterRequestsWhenUserIsNotAuthenticated()
      throws ServletException, IOException {
    when(servletRequest.getRequestURI()).thenReturn("/home.page");
    when(Context.isAuthenticated()).thenReturn(false);

    filter.doFilter(servletRequest, servletResponse, filterChain);

    verify(filterChain).doFilter(servletRequest, servletResponse);
  }
}
