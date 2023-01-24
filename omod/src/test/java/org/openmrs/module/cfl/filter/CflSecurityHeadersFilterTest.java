package org.openmrs.module.cfl.filter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class CflSecurityHeadersFilterTest {
  private static final List<String> REQUIRED_HEADERS =
      Arrays.asList(
          "Referrer-Policy",
          "Permissions-Policy",
          "Content-Security-Policy",
          "Strict-Transport-Security",
          "X-Content-Type-Options",
          "X-Frame-Options",
          "X-XSS-Protection");

  @Test
  public void shouldSetSecurityHeaders() throws IOException, ServletException {
    final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    final HttpServletResponse servletResponse = mock(HttpServletResponse.class);
    final FilterChain filterChain = mock(FilterChain.class);

    final CflSecurityHeadersFilter filter = new CflSecurityHeadersFilter();
    filter.doFilter(servletRequest, servletResponse, filterChain);

    for (String header : REQUIRED_HEADERS) {
      verify(servletResponse, times(1)).setHeader(eq(header), anyString());
    }
  }

  @Test
  public void shouldPreventSecurityHeaderReset() throws IOException, ServletException {
    final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    final HttpServletResponse servletResponse = mock(HttpServletResponse.class);
    final FilterChain filterChain = mock(FilterChain.class);

    final CflSecurityHeadersFilter filter = new CflSecurityHeadersFilter();
    filter.doFilter(servletRequest, servletResponse, filterChain);

    final ArgumentCaptor<HttpServletResponse> servletResponseCaptor =
        ArgumentCaptor.forClass(HttpServletResponse.class);
    verify(filterChain).doFilter(eq(servletRequest), servletResponseCaptor.capture());

    final HttpServletResponse capturedResponse = servletResponseCaptor.getValue();
    capturedResponse.reset();

    for (String header : REQUIRED_HEADERS) {
      verify(servletResponse, times(2)).setHeader(eq(header), anyString());
    }
  }
}
