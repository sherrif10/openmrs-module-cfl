package org.openmrs.module.cfl.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
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
      "default-src 'self' ws://localhost:* localhost:* 'unsafe-inline' 'unsafe-eval' https://*.gstatic.com; "
          + "script-src 'self' localhost:* 'unsafe-inline' 'unsafe-eval' https://www.google.com/recaptcha/api.js https://www.gstatic.com; "
          + "frame-src 'self' localhost:*  'unsafe-inline' 'unsafe-eval' https://www.google.com/; "
          + "font-src data: 'self' localhost:* https://*.gstatic.com; "
          + "img-src data: 'self' localhost:* https://*.github.io; "
          + "style-src 'self' localhost:* 'unsafe-inline' https://*.googleapis.com";

  private static final String STRICT_TRANSPORT_SECURITY_HEADER = "Strict-Transport-Security";
  private static final String STRICT_TRANSPORT_SECURITY_VALUE =
      "max-age=63072000; includeSubDomains; preload";

  private static final String X_CONTENT_TYPE_OPTIONS_HEADER = "X-Content-Type-Options";
  private static final String X_CONTENT_TYPE_OPTIONS_VALUE = "nosniff";

  private static final String X_FRAME_OPTIONS_HEADER = "X-Frame-Options";
  private static final String X_FRAME_OPTIONS_VALUE = "SAMEORIGIN";

  private static final String X_XSS_PROTECTION_HEADER = "X-XSS-Protection";
  private static final String X_XSS_PROTECTION_VALUE = "1; mode=block";

  @Override
  public void init(FilterConfig filterConfig) {
    // nothing to do
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (servletResponse instanceof HttpServletResponse) {
      filterChain.doFilter(
          servletRequest,
          new SafeSecurityHeaderHttpResponse((HttpServletResponse) servletResponse));
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void destroy() {
    // nothing to do
  }

  public static final class SafeSecurityHeaderHttpResponse extends HttpServletResponseWrapper {

    SafeSecurityHeaderHttpResponse(HttpServletResponse response) {
      super(response);
      addSecurityHeaders();
    }

    @Override
    public void reset() {
      super.reset();
      addSecurityHeaders();
    }

    private void addSecurityHeaders() {
      HttpServletResponse httpServletResponse = ((HttpServletResponse) getResponse());
      httpServletResponse.setHeader(REFERRER_POLICY_HEADER, REFERRER_STRICT_ORIGIN);
      httpServletResponse.setHeader(PERMISSIONS_POLICY_HEADER, CFL_PERMISSIONS_POLICY);
      httpServletResponse.setHeader(CONTENT_SECURITY_POLICY_HEADER, CFL_CONTENT_SECURITY_POLICY);
      // Browsers are going to ignore it for HTTP connections
      httpServletResponse.setHeader(
          STRICT_TRANSPORT_SECURITY_HEADER, STRICT_TRANSPORT_SECURITY_VALUE);
      httpServletResponse.setHeader(X_CONTENT_TYPE_OPTIONS_HEADER, X_CONTENT_TYPE_OPTIONS_VALUE);
      httpServletResponse.setHeader(X_FRAME_OPTIONS_HEADER, X_FRAME_OPTIONS_VALUE);
      httpServletResponse.setHeader(X_XSS_PROTECTION_HEADER, X_XSS_PROTECTION_VALUE);
    }
  }
}
