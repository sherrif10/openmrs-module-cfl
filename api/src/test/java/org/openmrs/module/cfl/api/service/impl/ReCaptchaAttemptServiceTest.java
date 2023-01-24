package org.openmrs.module.cfl.api.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.cfl.CfldistributionGlobalParameterConstants;
import org.openmrs.test.BaseContextMockTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ReCaptchaAttemptServiceTest extends BaseContextMockTest {

  @Mock private AdministrationService administrationServiceMock;

  @Before
  public void setup() {
    Mockito.when(
            administrationServiceMock.getGlobalProperty(
                CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_MAX_FAILED_ATTEMPTS_KEY))
        .thenReturn("2");
    contextMockHelper.setAdministrationService(administrationServiceMock);
  }

  @Test
  public void shouldNotBlockIPAfterSingleFail() {
    final String ipToBlock = "127.0.0.1";
    final ReCaptchaAttemptService service = new ReCaptchaAttemptService();
    service.reCaptchaFailed(ipToBlock);

    assertFalse(service.isBlocked(ipToBlock));
  }

  @Test
  public void shouldBlockIPAfterMultipleFails() {
    final String ipToBlock = "127.0.0.1";
    final ReCaptchaAttemptService service = new ReCaptchaAttemptService();
    service.reCaptchaFailed(ipToBlock);
    service.reCaptchaFailed(ipToBlock);

    assertTrue(service.isBlocked(ipToBlock));
  }
}
