/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.User;
import org.openmrs.module.cfl.api.service.UserNotAuthorizedService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserNotAuthorizedServiceImplTest extends BaseModuleContextSensitiveTest {

  private static final String USER_DATA_SET_FILE = "UserDataSet.xml";

  @Autowired
  @Qualifier("cflUserNotAuthorizedService")
  private UserNotAuthorizedService userNotAuthorizedService;

  @Before
  public void setUp() throws Exception {
    executeDataSet("datasets/" + USER_DATA_SET_FILE);
  }

  @Test
  public void shouldGetAdminUser() {
    User actual = userNotAuthorizedService.getUser("admin");

    assertNotNull(actual);
    assertEquals(Integer.valueOf(1), actual.getUserId());
    assertEquals("admin", actual.getUsername());
    assertEquals("admin", actual.getSystemId());
  }

  @Test
  public void shouldGetDaemonUser() {
    User actual = userNotAuthorizedService.getUser("daemon");

    assertNotNull(actual);
    assertEquals(Integer.valueOf(2), actual.getUserId());
    assertEquals("daemon", actual.getUsername());
    assertEquals("daemon", actual.getSystemId());
  }

  @Test
  public void shouldGetCfLDoctorUser() {
    User actual = userNotAuthorizedService.getUser("cfldoctor1");

    assertNotNull(actual);
    assertEquals(Integer.valueOf(3), actual.getUserId());
    assertEquals("cfldoctor1", actual.getUsername());
    assertEquals("cfldoctor1", actual.getSystemId());
  }
}
