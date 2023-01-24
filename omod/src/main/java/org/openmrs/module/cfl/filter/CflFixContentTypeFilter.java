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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Filter which sets correct content-type for special OpenMRS resources:
 *
 * <ul>
 *   <li>JavaScript for: openmrsmessages.js and rugOrder.js. These resources are JavaScript files
 *       with JSP-logic applied onto them, see details in openmrs-module-legacyui module.
 *   <li>
 *   <li>JSON for: uicommons/messages/get.action. These resource reads translations.
 * </ul>
 *
 * and d.
 */
public class CflFixContentTypeFilter implements Filter {
  private static final String JAVASCRIPT_CONTENT_TYPE = "text/javascript;charset=UTF-8";
  private static final String[] JAVASCRIPT_RESOURCES = {
    "openmrsmessages.js", "drugOrder.js", "dwr/interface/"
  };

  private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";
  private static final String[] JSON_RESOURCES = {"uicommons/messages/get.action"};

  private static final List<ContentTypeOverrideData> CONTENT_TYPES_OVERRIDES =
      Arrays.asList(
          new ContentTypeOverrideData(JAVASCRIPT_CONTENT_TYPE, JAVASCRIPT_RESOURCES),
          new ContentTypeOverrideData(JSON_CONTENT_TYPE, JSON_RESOURCES));

  @Override
  public void init(FilterConfig filterConfig) {
    // Nothing to do
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    final Optional<String> staticContentType =
        servletRequest instanceof HttpServletRequest
            ? getContentTypeOverride((HttpServletRequest) servletRequest)
            : Optional.empty();

    if (staticContentType.isPresent()) {
      filterChain.doFilter(
          servletRequest,
          new StaticContentTypeHttpServletResponseWrapper(
              staticContentType.get(), (HttpServletResponse) servletResponse));
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  private Optional<String> getContentTypeOverride(HttpServletRequest servletRequest) {
    final String requestURI = servletRequest.getRequestURI();

    for (ContentTypeOverrideData contentTypeOverride : CONTENT_TYPES_OVERRIDES) {
      for (String resourceToOverride : contentTypeOverride.resourceURLs) {
        if (requestURI.contains(resourceToOverride)) {
          return Optional.of(contentTypeOverride.contentType);
        }
      }
    }

    return Optional.empty();
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

  private static class ContentTypeOverrideData {
    final String contentType;
    final String[] resourceURLs;

    private ContentTypeOverrideData(String contentType, String[] resourceURLs) {
      this.contentType = contentType;
      this.resourceURLs = resourceURLs.clone();
    }
  }
}
