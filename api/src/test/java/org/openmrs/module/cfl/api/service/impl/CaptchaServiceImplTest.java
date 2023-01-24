package org.openmrs.module.cfl.api.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.cfl.CfldistributionGlobalParameterConstants;
import org.openmrs.test.BaseContextMockTest;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaptchaServiceImplTest extends BaseContextMockTest {
  @Mock private AdministrationService administrationServiceMock;
  @Mock private HttpServletRequest httpServletRequestMock;
  @Mock private ReCaptchaAttemptService reCaptchaAttemptServiceMock;
  @Mock private RestTemplate restTemplate;

  @Test(expected = ReCaptchaInvalidException.class)
  public void shouldFailIfRecaptchaResponseParameterContainsNonAlphanumericCharacters() {
    when(httpServletRequestMock.getParameter("g-recaptcha-response")).thenReturn("@/*");

    final CaptchaServiceImpl service = new CaptchaServiceImpl();

    service.processResponse(httpServletRequestMock);
  }

  @Test(expected = ReCaptchaInvalidException.class)
  public void shouldFailIfThereWasTooManyAttemptsForIPAddress() {
    final String blockedIp = "127.0.0.2";

    when(httpServletRequestMock.getParameter("g-recaptcha-response")).thenReturn("1234");
    when(httpServletRequestMock.getRemoteAddr()).thenReturn(blockedIp);
    when(reCaptchaAttemptServiceMock.isBlocked(blockedIp)).thenReturn(Boolean.TRUE);

    final CaptchaServiceImpl service = new CaptchaServiceImpl();
    service.setReCaptchaAttemptService(reCaptchaAttemptServiceMock);

    service.processResponse(httpServletRequestMock);
  }

  @Test(expected = ReCaptchaInvalidException.class)
  public void shouldFailIfResponseIsNotSuccess() {
    final String anyIp = "127.0.0.2";

    when(administrationServiceMock.getGlobalProperty(
            CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SECRET_KEY))
        .thenReturn("ac3282db-146b-4d42-87ef-45a483d07b9e");
    contextMockHelper.setAdministrationService(administrationServiceMock);

    when(httpServletRequestMock.getParameter("g-recaptcha-response")).thenReturn("1234");
    when(httpServletRequestMock.getRemoteAddr()).thenReturn(anyIp);
    when(reCaptchaAttemptServiceMock.isBlocked(anyIp)).thenReturn(Boolean.FALSE);

    final GoogleResponse googleResponse = new GoogleResponse();
    googleResponse.setSuccess(false);
    googleResponse.setErrorCodes(new GoogleResponse.ErrorCode[0]);

    when(restTemplate.getForObject(any(URI.class), any(Class.class))).thenReturn(googleResponse);

    final CaptchaServiceImpl service = new CaptchaServiceImpl(restTemplate);
    service.setReCaptchaAttemptService(reCaptchaAttemptServiceMock);

    service.processResponse(httpServletRequestMock);
  }

  @Test
  public void shouldMarkValidationAsSuccess() {
    final String anyIp = "127.0.0.2";

    when(administrationServiceMock.getGlobalProperty(
            CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SECRET_KEY))
        .thenReturn("ac3282db-146b-4d42-87ef-45a483d07b9e");
    contextMockHelper.setAdministrationService(administrationServiceMock);

    when(httpServletRequestMock.getParameter("g-recaptcha-response")).thenReturn("1234");
    when(httpServletRequestMock.getRemoteAddr()).thenReturn(anyIp);
    when(reCaptchaAttemptServiceMock.isBlocked(anyIp)).thenReturn(Boolean.FALSE);

    final GoogleResponse googleResponse = new GoogleResponse();
    googleResponse.setSuccess(true);
    googleResponse.setErrorCodes(new GoogleResponse.ErrorCode[0]);

    when(restTemplate.getForObject(any(URI.class), any(Class.class))).thenReturn(googleResponse);

    final CaptchaServiceImpl service = new CaptchaServiceImpl(restTemplate);
    service.setReCaptchaAttemptService(reCaptchaAttemptServiceMock);

    service.processResponse(httpServletRequestMock);

    verify(reCaptchaAttemptServiceMock).reCaptchaSucceeded(anyIp);
  }
}
