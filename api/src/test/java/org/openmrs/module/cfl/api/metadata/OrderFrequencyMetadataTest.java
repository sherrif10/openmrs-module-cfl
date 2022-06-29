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
import org.openmrs.OrderFrequency;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class OrderFrequencyMetadataTest {

  @Mock private ConceptService conceptService;

  @Mock private MetadataDeployService metadataDeployService;

  @InjectMocks private OrderFrequencyMetadata orderFrequencyMetadata;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getConceptService()).thenReturn(conceptService);
  }

  @Test
  public void shouldReturnProperVersion() {
    int actual = orderFrequencyMetadata.getVersion();

    assertEquals(2, actual);
  }

  @Test
  public void shouldCreateOrderFrequencyConfig() {
    orderFrequencyMetadata.installNewVersion();

    verify(conceptService, times(30)).getConceptByUuid(anyString());
    verify(metadataDeployService, times(30)).installObject(any(OrderFrequency.class));
  }
}
