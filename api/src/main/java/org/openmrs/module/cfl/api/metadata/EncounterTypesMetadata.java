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

import org.openmrs.EncounterType;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

/** Adds Encounter Types. */
public class EncounterTypesMetadata extends VersionedMetadataBundle {
  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  protected void installNewVersion() throws Exception {
    install(newEncounterType("CFL Discontinue program", "f1c23f25-20e1-4503-b09e-116aba0a6063"));
  }

  private EncounterType newEncounterType(String name, String uuid) {
    final EncounterType encounterType = new EncounterType();
    encounterType.setName(name);
    encounterType.setUuid(uuid);
    return encounterType;
  }
}
