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

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cflcore.CFLConstants;
import org.openmrs.module.cflcore.api.util.GlobalPropertiesConstants;
import org.openmrs.module.cfl.CfldistributionGlobalParameterConstants;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;
import org.openmrs.util.OpenmrsConstants;

/**
 * Updates Global Parameter values, it's responsible for updating Global Properties to CFL
 * distribution defaults.
 *
 * <p>The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with
 * annotated beans.
 */
public class UpdateGlobalParametersMetadata extends VersionedMetadataBundle {
  @Override
  public int getVersion() {
    return 4;
  }

  @Override
  protected void installEveryTime() {
    // nothing to do
  }

  @Override
  public void installNewVersion() {
    updateGlobalProperties();
  }

  private void updateGlobalProperties() {
    updateCoreProperties();
    updateCFLProperties();
    updateOrderEntryProperties();
  }

  private void updateCoreProperties() {
    updateGlobalPropertyIfExists(
        OpenmrsConstants.GLOBAL_PROPERTY_PERSON_ATTRIBUTE_SEARCH_MATCH_MODE,
        OpenmrsConstants.GLOBAL_PROPERTY_PERSON_ATTRIBUTE_SEARCH_MATCH_ANYWHERE);
  }

  private void updateCFLProperties() {
    // Enable and keep enabled patient dashboard redirection
    updateGlobalPropertyIfExists(
        CFLConstants.PATIENT_DASHBOARD_REDIRECT_GLOBAL_PROPERTY_NAME, Boolean.TRUE.toString());
    updateGlobalPropertyIfExists(
        GlobalPropertiesConstants.TELEPHONE_NUMBER_PERSON_ATTRIBUTE_TYPE_UUID.getKey(),
        CfldistributionGlobalParameterConstants.CFL_TELEPHONE_NUMBER_PERSON_ATTRIBUTE_TYPE_UUID);
    updateGlobalPropertyIfExists(
        GlobalPropertiesConstants.EMAIL_ADDRESS_PERSON_ATTRIBUTE_TYPE_UUID.getKey(),
        CfldistributionGlobalParameterConstants.CFL_EMAIL_ADDRESS_PERSON_ATTRIBUTE_TYPE_UUID);
  }

  private void updateOrderEntryProperties() {
    // Updates to CIEL Concepts Sets as default values
    updateGlobalPropertyIfExists(
        OpenmrsConstants.GP_DRUG_DISPENSING_UNITS_CONCEPT_UUID,
        "162402AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    updateGlobalPropertyIfExists(
        OpenmrsConstants.GP_DRUG_DOSING_UNITS_CONCEPT_UUID, "162384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    updateGlobalPropertyIfExists(
        OpenmrsConstants.GP_DRUG_ROUTES_CONCEPT_UUID, "162394AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    updateGlobalPropertyIfExists(
        OpenmrsConstants.GP_DURATION_UNITS_CONCEPT_UUID, "1732AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
  }

  private void updateGlobalPropertyIfExists(String property, String value) {
    final AdministrationService administrationService = Context.getAdministrationService();
    GlobalProperty gp = administrationService.getGlobalPropertyObject(property);
    if (gp != null) {
      administrationService.setGlobalProperty(property, value);
    }
  }
}
