/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Role;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class RolePrivilegeProfilesMetadataTest {

  @Mock private UserService userService;

  @Mock private MetadataDeployService metadataDeployService;

  @InjectMocks private RolePrivilegeProfilesMetadata rolePrivilegeProfilesMetadata;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getUserService()).thenReturn(userService);
  }

  @Test
  public void shouldInstallAnalystAndDoctorRoles() {
    rolePrivilegeProfilesMetadata.installNewVersion();

    verify(metadataDeployService, times(2)).installObject(any(Role.class));
  }
}
