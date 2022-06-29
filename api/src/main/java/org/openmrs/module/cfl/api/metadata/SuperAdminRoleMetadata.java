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

import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.api.UserService;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.role;

/**
 * The SuperAdminRoleMetadata adds a Super Admin role and makes sure it contains all privileges
 * available in the system.
 *
 * @implNote This metadata bundle runs when cfl module is loaded. Usually it will be the
 *     last module loaded or second to implementation specific module (e.g.: vxnaid).
 *     That means if there are permissions added to the system in a module which is loaded after
 *     cfl, then that module must add that roles to the Super Admin role by itself.
 */
public class SuperAdminRoleMetadata extends AbstractMetadataBundle {
  private static final String SUPER_ADMIN_ROLE_NAME = "Super Admin";
  private static final String SUPER_ADMIN_ROLE_DESC = "A role that contains all permissions.";

  private UserService userService;

  @Override
  public void install() {
    final Role superAdminRole = getOrCreateSuperAdminRole();
    assignAllPrivileges(superAdminRole);
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  private Role getOrCreateSuperAdminRole() {
    Role superAdminRole = userService.getRole(SUPER_ADMIN_ROLE_NAME);

    if (superAdminRole == null) {
      superAdminRole = createSuperAdminRole();
    }

    return superAdminRole;
  }

  private Role createSuperAdminRole() {
    final Role role =
        role(
            SUPER_ADMIN_ROLE_NAME,
            SUPER_ADMIN_ROLE_DESC,
            emptySet(),
            emptySet(),
            UUID.randomUUID().toString());
    role.setPrivileges(new HashSet<>());

    return userService.saveRole(role);
  }

  private void assignAllPrivileges(Role superAdminRole) {
    final Set<Privilege> alreadyAssigned = new HashSet<>(superAdminRole.getPrivileges());
    final List<Privilege> allPrivileges = userService.getAllPrivileges();

    for (Privilege privilege : allPrivileges) {
      if (!alreadyAssigned.remove(privilege)) {
        superAdminRole.addPrivilege(privilege);
      }
    }
  }
}
