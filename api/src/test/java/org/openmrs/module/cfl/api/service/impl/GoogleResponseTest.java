package org.openmrs.module.cfl.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class GoogleResponseTest {
  private static final List<String> EXPECTED_ERROR_CODES =
      Arrays.asList(
          "missing-input-secret",
          "invalid-input-secret",
          "missing-input-response",
          "bad-request",
          "invalid-input-response",
          "timeout-or-duplicate");

  @Test
  public void shouldHandleAllErrorCodes() {
    for (String errorCodeValue : EXPECTED_ERROR_CODES) {
      Assert.assertNotNull(GoogleResponse.ErrorCode.forValue(errorCodeValue));
    }
  }

  @Test
  public void shouldDeserializeFromGoogleResponse() throws IOException {
    final URL restResponseURL =
        GoogleResponseTest.class.getResource("/GoogleResponseTest/google-response.json");

    final ObjectMapper objectMapper = new ObjectMapper();
    final GoogleResponse testResponse = objectMapper.readValue(restResponseURL, GoogleResponse.class);

    Assert.assertFalse(testResponse.isSuccess());
    Assert.assertTrue(testResponse.getScore() - 1.23 < 0.01);
    Assert.assertEquals("test", testResponse.getAction());
    Assert.assertEquals("2023-01-13'T'13:00:00Z", testResponse.getChallengeTs());
    Assert.assertEquals(1, testResponse.getErrorCodes().length);
    Assert.assertEquals(GoogleResponse.ErrorCode.BadRequest, testResponse.getErrorCodes()[0]);
    Assert.assertTrue(testResponse.hasClientError());
  }
}
