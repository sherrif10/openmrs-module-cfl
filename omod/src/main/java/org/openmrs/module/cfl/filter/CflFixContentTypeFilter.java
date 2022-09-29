package org.openmrs.module.cfl.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Filter which sets correct content-type for special OpenMRS resources: openmrsmessages.js and
 * drugOrder.js. These resources are JavaScript files with JSP-logic applied onto them, see details
 * in openmrs-module-legacyui module.
 */
public class CflFixContentTypeFilter implements Filter {
  private static final String JAVASCRIPT_CONTENT_TYPE = "text/javascript;charset=UTF-8";
  private static final String[] SPACIAL_RESOURCES = {"openmrsmessages.js", "drugOrder.js"};

  @Override
  public void init(FilterConfig filterConfig) {
    // Nothing to do
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (servletRequest instanceof HttpServletRequest
        && isSpecialResourceRequest((HttpServletRequest) servletRequest)) {

      filterChain.doFilter(
          servletRequest,
          new StaticContentTypeHttpServletResponseWrapper(
              JAVASCRIPT_CONTENT_TYPE, (HttpServletResponse) servletResponse));
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  private boolean isSpecialResourceRequest(HttpServletRequest servletHttpRequest) {
    final String requestURI = servletHttpRequest.getRequestURI();

    for (String resource : SPACIAL_RESOURCES) {
      if (requestURI.contains(resource)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void destroy() {
    // Nothing to do
  }

  /** HTTP Response wrapper which replaces any Content-type with preconfigured static one. */
  static class StaticContentTypeHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private final String staticContentType;

    StaticContentTypeHttpServletResponseWrapper(
        String staticContentType, HttpServletResponse response) {
      super(response);
      this.staticContentType = staticContentType;
    }

    @Override
    public void setHeader(String name, String value) {
      if (CONTENT_TYPE_HEADER.equals(name)) {
        super.setHeader(CONTENT_TYPE_HEADER, staticContentType);
      } else {
        super.setHeader(name, value);
      }
    }

    @Override
    public void addHeader(String name, String value) {
      if (CONTENT_TYPE_HEADER.equals(name)) {
        super.addHeader(CONTENT_TYPE_HEADER, staticContentType);
      } else {
        super.setHeader(name, value);
      }
    }

    @Override
    public void setContentType(String type) {
      super.setContentType(staticContentType);
    }
  }
}
