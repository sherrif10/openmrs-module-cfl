/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.fragment.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class InfoAndErrorMessagesFragmentControllerTest {

  @Mock private MockHttpServletRequest request;

  @Mock private FragmentModel fragmentModel;

  @Mock private MockHttpSession httpSession;

  @InjectMocks private InfoAndErrorMessagesFragmentController controller;

  @Test
  public void shouldAddAttributesToModel() {
    when(request.getSession()).thenReturn(httpSession);

    controller.controller(request, fragmentModel);

    verify(httpSession).getAttribute("_REFERENCE_APPLICATION_ERROR_MESSAGE_");
    verify(httpSession).getAttribute("_REFERENCE_APPLICATION_INFO_MESSAGE_");
    verify(httpSession).setAttribute("_REFERENCE_APPLICATION_ERROR_MESSAGE_", null);
    verify(httpSession).setAttribute("_REFERENCE_APPLICATION_INFO_MESSAGE_", null);
    verify(fragmentModel).addAttribute(eq("errorMessage"), anyString());
    verify(fragmentModel).addAttribute(eq("infoMessage"), anyString());
  }
}
