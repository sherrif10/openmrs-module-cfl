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

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.api.UserService;
import org.openmrs.test.BaseContextMockTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SuperAdminRoleMetadataTest extends BaseContextMockTest {

  @Test
  public void install_shouldAssignAllPrivilegesToNewRole() {
    final List<Privilege> allPrivileges = Arrays.asList(newPrivilege(), newPrivilege());

    final UserService userService = mock(UserService.class);
    when(userService.getAllPrivileges()).thenReturn(allPrivileges);
    when(userService.saveRole(any(Role.class)))
        .thenAnswer(
            invocation -> {
              final Role newRole = (Role) invocation.getArguments()[0];
              newRole.setPrivileges(new HashSet<>());
              return newRole;
            });

    final SuperAdminRoleMetadata metadata = new SuperAdminRoleMetadata();
    metadata.setUserService(userService);
    metadata.install();

    final ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
    verify(userService).saveRole(roleArgumentCaptor.capture());

    final Role newRole = roleArgumentCaptor.getValue();
    assertEquals(new HashSet<>(allPrivileges), newRole.getPrivileges());
  }

  @Test
  public void install_shouldAssignAllPrivilegesToExistingRole() {
    final List<Privilege> allPrivileges = Arrays.asList(newPrivilege(), newPrivilege());
    final Role existingRole = new Role();
    existingRole.addPrivilege(allPrivileges.get(0));

    final UserService userService = mock(UserService.class);
    when(userService.getAllPrivileges()).thenReturn(allPrivileges);
    when(userService.getRole(anyString())).thenReturn(existingRole);

    final SuperAdminRoleMetadata metadata = new SuperAdminRoleMetadata();
    metadata.setUserService(userService);
    metadata.install();

    assertEquals(new HashSet<>(allPrivileges), existingRole.getPrivileges());
  }

  private Privilege newPrivilege() {
    final Privilege newPrivilege = new Privilege();
    newPrivilege.setPrivilege(newPrivilege.getUuid());
    return newPrivilege;
  }
}
