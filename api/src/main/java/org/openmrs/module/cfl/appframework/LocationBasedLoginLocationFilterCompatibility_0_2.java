/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.appframework;

import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.annotation.OpenmrsProfile;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.LoginLocationFilter;
import org.openmrs.module.locationbasedaccess.utils.LocationUtils;

import java.util.Collections;

/**
 * The LocationBasedLoginLocationFilterCompatibility_0_2 Class. The bean is loaded only when locationbasedaccess module
 * 0.2.* to 0.3.* is installed.
 * <p>
 * This is callback filter used by appframework module to determine which Login Locations (e.g.: the page's header) should be
 * visible for current user.
 * </p>
 * <p>
 * The LocationBasedLoginLocationFilter accepts only the locations allowed by locationbasedaccess module functionality or
 * all locations if there is no authenticated user or current user is a system admin user.
 * </p>
 */
@OpenmrsProfile(modules = "locationbasedaccess:0.2.* - 0.3.*")
@SuppressWarnings("PMD.ClassNamingConventions")
public class LocationBasedLoginLocationFilterCompatibility_0_2 implements LoginLocationFilter {
    @Override
    public boolean accept(Location location) {
        final User authenticatedUser = Context.getUserContext().getAuthenticatedUser();

        if (authenticatedUser == null || authenticatedUser.isSuperUser()) {
            return true;
        }

        return LocationUtils.doesUserBelongToGivenLocations(authenticatedUser,
                Collections.singletonList(location.getUuid()));
    }
}
