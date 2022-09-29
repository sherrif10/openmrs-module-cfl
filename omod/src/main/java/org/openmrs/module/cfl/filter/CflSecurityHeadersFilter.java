package org.openmrs.module.cfl.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** HTTP Filter which adds additional security HTTP headers. */
public class CflSecurityHeadersFilter implements Filter {
  private static final String REFERRER_POLICY_HEADER = "Referrer-Policy";

  /**
   * Send only the origin when the protocol security level stays the same (HTTPS→HTTPS). Don't send
   * the Referer header to less secure destinations (HTTPS→HTTP).
   */
  private static final String REFERRER_STRICT_ORIGIN = "strict-origin";

  private static final String PERMISSIONS_POLICY_HEADER = "Permissions-Policy";

  private static final String CFL_PERMISSIONS_POLICY =
      "accelerometer=(), ambient-light-sensor=(), autoplay=(), battery=(), camera=(), "
          + "cross-origin-isolated=(self), display-capture=(), document-domain=(self), encrypted-media=(self), "
          + "execution-while-not-rendered=(self), execution-while-out-of-viewport=(self), fullscreen=(self), geolocation="
          + "(self), gyroscope=(), keyboard-map=(self), magnetometer=(), microphone=(), midi=(self), navigation-override="
          + "(self), payment=(), picture-in-picture=(self), publickey-credentials-get=(self), screen-wake-lock=(self), "
          + "sync-xhr=(self), usb=(), web-share=(self), xr-spatial-tracking=()";

  private static final String CONTENT_SECURITY_POLICY_HEADER = "Content-Security-Policy";

  /**
   * We 'unsafe-inline', because there are OpenMRS pages which use script tags in many places. We
   * use 'unsafe-eval', because knockout.js library is used in FE code.
   */
  private static final String CFL_CONTENT_SECURITY_POLICY =
      "default-src 'self' 'unsafe-inline' 'unsafe-eval'; font-src data: 'self'; img-src data: 'self'";

  private static final String STRICT_TRANSPORT_SECURITY_HEADER = "Strict-Transport-Security";
  private static final String STRICT_TRANSPORT_SECURITY_VALUE =
      "max-age=63072000; includeSubDomains; preload";

  @Override
  public void init(FilterConfig filterConfig) {
    // nothing to do
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (servletResponse instanceof HttpServletResponse) {
      HttpServletResponse httpServletResponse = ((HttpServletResponse) servletResponse);
      httpServletResponse.addHeader(REFERRER_POLICY_HEADER, REFERRER_STRICT_ORIGIN);
      httpServletResponse.addHeader(PERMISSIONS_POLICY_HEADER, CFL_PERMISSIONS_POLICY);
      httpServletResponse.addHeader(CONTENT_SECURITY_POLICY_HEADER, CFL_CONTENT_SECURITY_POLICY);
      // Browsers are going to ignore it for HTTP connections
      httpServletResponse.addHeader(
          STRICT_TRANSPORT_SECURITY_HEADER, STRICT_TRANSPORT_SECURITY_VALUE);
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
    // nothing to do
  }
}
