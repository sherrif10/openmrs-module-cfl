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

import static org.junit.Assert.assertEquals;

public class HtmlFormsMetadataTest {

  @Test
  public void shouldReturnProperVersion() {
    HtmlFormsMetadata htmlFormsMetadata = new HtmlFormsMetadata();

    int actual = htmlFormsMetadata.getVersion();

    assertEquals(8, actual);
  }
}
