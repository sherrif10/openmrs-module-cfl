/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.util;

import org.junit.Test;
import org.openmrs.api.db.hibernate.DbSessionFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MetadataSQLScriptRunnerTest {

  private static final String RESOURCE_PATH = "testQuery.sql";

  private DbSessionFactory dbSessionFactory;

  @Test
  public void shouldGetSqlContentFromFileAsText() throws IOException {
    MetadataSQLScriptRunner runner = new MetadataSQLScriptRunner(dbSessionFactory);

    String actual = runner.getQueryFromResource(RESOURCE_PATH);

    assertNotNull(actual);
    assertEquals("SELECT 1;", actual);
  }
}
