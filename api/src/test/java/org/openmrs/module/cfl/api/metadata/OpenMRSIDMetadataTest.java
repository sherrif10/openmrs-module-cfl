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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.AutoGenerationOption;
import org.openmrs.module.idgen.SequentialIdentifierGenerator;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatamapping.MetadataTermMapping;
import org.openmrs.module.metadatamapping.api.MetadataMappingService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class OpenMRSIDMetadataTest {

  private static final String TEST_UUID_1 = "72945397-f13f-11ec-8dfd-0242ac130002";

  private static final String TEST_UUID_2 = "7d840f0b-f13f-11ec-8dfd-0242ac130002";

  @Mock private MetadataDeployService metadataDeployService;

  @Mock private MetadataMappingService metadataMappingService;

  @Mock private IdentifierSourceService identifierSourceService;

  @Mock private AdministrationService administrationService;

  @InjectMocks private OpenMRSIDMetadata openMRSIDMetadata;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getAdministrationService()).thenReturn(administrationService);
  }

  @Test
  public void shouldInstallOpenMRSIDConfig() throws Exception {
    when(metadataDeployService.installObject(any(PatientIdentifierType.class)))
        .thenReturn(createTestPatientIdentifierType());
    when(metadataMappingService.getMetadataTermMapping(
            "org.openmrs.module.emrapi", "emr.primaryIdentifierType"))
        .thenReturn(createTestMetadataTermMapping());
    when(identifierSourceService.saveIdentifierSource(any(SequentialIdentifierGenerator.class)))
        .thenReturn(createTestSequentialIdentifierGenerator());

    openMRSIDMetadata.installNewVersion();

    verify(metadataDeployService).installObject(any(PatientIdentifierType.class));
    verify(metadataMappingService)
        .getMetadataTermMapping("org.openmrs.module.emrapi", "emr.primaryIdentifierType");
    verify(metadataMappingService).saveMetadataTermMapping(any(MetadataTermMapping.class));
    verify(identifierSourceService, times(2))
        .getIdentifierSourceByUuid("691eed12-c0f1-11e2-94be-8c13b969e334");
    verify(identifierSourceService, times(2))
        .saveIdentifierSource(any(SequentialIdentifierGenerator.class));
    verify(identifierSourceService).getAutoGenerationOption(any(PatientIdentifierType.class));
    verify(identifierSourceService).saveAutoGenerationOption(any(AutoGenerationOption.class));
  }

  private PatientIdentifierType createTestPatientIdentifierType() {
    PatientIdentifierType patientIdentifierType = new PatientIdentifierType();
    patientIdentifierType.setName("testName");
    patientIdentifierType.setUuid(TEST_UUID_1);

    return patientIdentifierType;
  }

  private MetadataTermMapping createTestMetadataTermMapping() {
    MetadataTermMapping metadataTermMapping = new MetadataTermMapping();
    metadataTermMapping.setUuid(TEST_UUID_2);
    return metadataTermMapping;
  }

  private SequentialIdentifierGenerator createTestSequentialIdentifierGenerator() {
    SequentialIdentifierGenerator generator = new SequentialIdentifierGenerator();
    generator.setId(100);
    return generator;
  }
}
