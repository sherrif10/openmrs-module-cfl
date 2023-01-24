package org.openmrs.module.cfl.filter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class CflFixContentTypeFilterTest {
  private static final String CONTENT_TYPE_HEADER = "Content-Type";

  @Test
  public void shouldNotFilterAnyURL() throws IOException, ServletException {
    final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    final HttpServletResponse servletResponse = mock(HttpServletResponse.class);
    final FilterChain filterChain = mock(FilterChain.class);

    when(servletRequest.getRequestURI()).thenReturn("test-host:8080/openmrs/any/url");

    final CflFixContentTypeFilter filter = new CflFixContentTypeFilter();
    filter.doFilter(servletRequest, servletResponse, filterChain);

    verify(filterChain, times(1)).doFilter(servletRequest, servletResponse);
  }

  @Test
  public void shouldFilterOmrsmessagesJS() throws IOException, ServletException {
    doContentTypeTestForURI("openmrsmessages.js", "text/javascript;charset=UTF-8");
  }

  @Test
  public void shouldFilterDrugorderJS() throws IOException, ServletException {
    doContentTypeTestForURI("drugOrder.js", "text/javascript;charset=UTF-8");
  }

  @Test
  public void shouldFilterAlldwrinterfaceJS() throws IOException, ServletException {
    doContentTypeTestForURI("dwr/interface/", "text/javascript;charset=UTF-8");
  }

  @Test
  public void shouldFilterMessagesGetJSON() throws IOException, ServletException {
    doContentTypeTestForURI("uicommons/messages/get.action", "application/json;charset=UTF-8");
  }

  private void doContentTypeTestForURI(String uri, String expectedContentType)
      throws IOException, ServletException {
    final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    final HttpServletResponse servletResponse = mock(HttpServletResponse.class);
    final FilterChain filterChain = mock(FilterChain.class);

    when(servletRequest.getRequestURI()).thenReturn("test-host:8080/openmrs" + uri);

    final CflFixContentTypeFilter filter = new CflFixContentTypeFilter();
    filter.doFilter(servletRequest, servletResponse, filterChain);

    final ArgumentCaptor<HttpServletResponse> servletResponseCaptor =
        ArgumentCaptor.forClass(HttpServletResponse.class);
    verify(filterChain).doFilter(Mockito.eq(servletRequest), servletResponseCaptor.capture());

    final HttpServletResponse capturedResponse = servletResponseCaptor.getValue();

    capturedResponse.setHeader(CONTENT_TYPE_HEADER, "any/content");
    verify(servletResponse).setHeader(CONTENT_TYPE_HEADER, expectedContentType);
  }
}
