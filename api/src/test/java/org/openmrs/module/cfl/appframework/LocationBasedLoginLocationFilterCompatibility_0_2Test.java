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

import org.junit.Test;
import org.mockito.Mockito;
import org.openmrs.Location;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.junit.Assert.assertTrue;

public class LocationBasedLoginLocationFilterCompatibility_0_2Test
    extends BaseModuleContextSensitiveTest {

  @Test
  public void shouldAcceptSuperUser() throws Exception {
    final Location locationMock = Mockito.mock(Location.class);
    final LocationBasedLoginLocationFilterCompatibility_0_2 compatibility =
        new LocationBasedLoginLocationFilterCompatibility_0_2();

    this.authenticate();

    assertTrue(compatibility.accept(locationMock));
  }
}
