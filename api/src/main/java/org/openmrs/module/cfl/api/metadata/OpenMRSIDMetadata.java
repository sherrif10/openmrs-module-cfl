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

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.cfl.CfldistributionGlobalParameterConstants;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.idgen.AutoGenerationOption;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.idgen.SequentialIdentifierGenerator;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.idgen.validator.LuhnMod30IdentifierValidator;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;
import org.openmrs.module.metadatamapping.MetadataTermMapping;
import org.openmrs.module.metadatamapping.api.MetadataMappingService;

import static org.openmrs.module.cflcore.api.util.GlobalPropertyUtils.createGlobalSettingIfNotExists;

public class OpenMRSIDMetadata extends VersionedMetadataBundle {
  private static final String OPENMRS_ID_UUID = "05a29f94-c0ed-11e2-94be-8c13b969e334";
  private static final String OPENMRS_ID_NAME = "OpenMRS ID";
  private static final String OPENMRS_ID_DESCRIPION =
      "OpenMRS patient identifier, with check-digit";
  private static final String OPENMRS_ID_GENERATOR_UUID = "691eed12-c0f1-11e2-94be-8c13b969e334";
  private static final String OPENMRS_ID_GENERATOR_NAME = "Generator for " + OPENMRS_ID_NAME;

  private IdentifierSourceService identifierSourceService;
  private MetadataMappingService metadataMappingService;

  @Override
  public int getVersion() {
    return 1;
  }

  public void setIdentifierSourceService(IdentifierSourceService identifierSourceService) {
    this.identifierSourceService = identifierSourceService;
  }

  public void setMetadataMappingService(MetadataMappingService metadataMappingService) {
    this.metadataMappingService = metadataMappingService;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  public void installNewVersion() throws Exception {
    final PatientIdentifierType openmrsIdType = installPatientIdentifierType();
    installSequentialIdentifierGenerator(openmrsIdType);
    final IdentifierSource openmrsIdGenerator = installSequentialIdentifierGenerator(openmrsIdType);
    installAutoGenerationOption(openmrsIdType, openmrsIdGenerator);

    // Configure the openmrsIdGenerator to be used
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.REGISTRATIONCORE_IDENTIFIER_SOURCE_ID_KEY,
        openmrsIdGenerator.getId().toString());
  }

  private PatientIdentifierType installPatientIdentifierType() {
    final PatientIdentifierType openmrsIdType = new PatientIdentifierType();
    openmrsIdType.setName(OPENMRS_ID_NAME);
    openmrsIdType.setDescription(OPENMRS_ID_DESCRIPION);
    openmrsIdType.setRequired(Boolean.TRUE);
    openmrsIdType.setValidator(LuhnMod30IdentifierValidator.class.getName());
    openmrsIdType.setUuid(OPENMRS_ID_UUID);

    final PatientIdentifierType installedPatientIdentifierType = install(openmrsIdType);
    setIdentifierTypeForMetadataTermMapping(installedPatientIdentifierType);
    return installedPatientIdentifierType;
  }

  private void setIdentifierTypeForMetadataTermMapping(
      PatientIdentifierType patientIdentifierType) {
    final MetadataTermMapping identifierTypeMapping =
        metadataMappingService.getMetadataTermMapping(
            EmrApiConstants.EMR_METADATA_SOURCE_NAME, EmrApiConstants.PRIMARY_IDENTIFIER_TYPE);
    // overwrite if not set yet
    if (!patientIdentifierType.getUuid().equals(identifierTypeMapping.getMetadataUuid())) {
      identifierTypeMapping.setMappedObject(patientIdentifierType);
      metadataMappingService.saveMetadataTermMapping(identifierTypeMapping);
    }
  }

  private IdentifierSource installSequentialIdentifierGenerator(
      PatientIdentifierType openmrsIdType) {
    IdentifierSource openmrsIdGenerator =
        identifierSourceService.getIdentifierSourceByUuid(OPENMRS_ID_GENERATOR_UUID);

    if (openmrsIdGenerator != null) {
      return openmrsIdGenerator;
    }

    final SequentialIdentifierGenerator sequentialIdentifierGenerator =
        new SequentialIdentifierGenerator();
    sequentialIdentifierGenerator.setIdentifierType(openmrsIdType);
    sequentialIdentifierGenerator.setName(OPENMRS_ID_GENERATOR_NAME);
    sequentialIdentifierGenerator.setUuid(OPENMRS_ID_GENERATOR_UUID);
    sequentialIdentifierGenerator.setBaseCharacterSet(
        new LuhnMod30IdentifierValidator().getBaseCharacters());
    sequentialIdentifierGenerator.setMinLength(5);
    sequentialIdentifierGenerator.setFirstIdentifierBase("10000");

    return identifierSourceService.saveIdentifierSource(sequentialIdentifierGenerator);
  }

  private void installAutoGenerationOption(
      PatientIdentifierType openmrsIdType, IdentifierSource openmrsIdGenerator) {
    AutoGenerationOption openmrsIdOptions =
        identifierSourceService.getAutoGenerationOption(openmrsIdType);
    if (openmrsIdOptions == null) {
      openmrsIdOptions = new AutoGenerationOption();
      openmrsIdOptions.setIdentifierType(openmrsIdType);
      openmrsIdOptions.setSource(openmrsIdGenerator);
      openmrsIdOptions.setManualEntryEnabled(false);
      openmrsIdOptions.setAutomaticGenerationEnabled(true);
      identifierSourceService.saveAutoGenerationOption(openmrsIdOptions);
    }
  }
}
