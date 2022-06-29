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

import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

import java.util.Collection;
import java.util.List;

/** Adds Provider account to the Super User (admin). */
public class AdminProviderMetadata extends VersionedMetadataBundle {
  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected void installEveryTime() {
    // needs to be overwritten, do nothing
  }

  @Override
  public void installNewVersion() {
    final ProviderService providerService = Context.getProviderService();
    final Person adminPerson = Context.getPersonService().getPerson(1);
    final Collection<Provider> possibleProviders =
        providerService.getProvidersByPerson(adminPerson);

    if (possibleProviders.isEmpty()) {
      final List<Provider> providers = providerService.getAllProviders(false);

      final Provider provider;
      if (providers.isEmpty()) {
        provider = new Provider();
        provider.setIdentifier("admin");
      } else {
        provider = providers.get(0);
      }
      provider.setPerson(adminPerson);
      providerService.saveProvider(provider);
    }
  }
}
