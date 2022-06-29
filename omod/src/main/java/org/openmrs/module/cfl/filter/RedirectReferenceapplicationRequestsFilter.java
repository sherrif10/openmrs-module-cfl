/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.filter;

import org.apache.commons.lang.StringUtils;
import org.openmrs.ui.framework.WebConstants;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * The Request Filter which redirects requests for OpenMRS reference application module's resources,
 * endpoints and pages.
 *
 * <p>Some of the OpenMRS OWA modules require reference modules to be included, but these modules
 * add redundant data and the CfL doesn't use them, therefore we need to redirect some of the
 * requests to CfL resources.
 *
 * <p>Redirected paths:
 *
 * <ul>
 *   <li>resource/referenceapplication/styles/referenceapplication.css resource to
 *       referenceapplication.css located in this project. Added because of the Admin View OWA uses
 *       CSS from OpenMRS reference application.
 *   <li>referenceapplication/home.page to cfl/home.page. Added because of the Admin
 *       View OWA uses hard-codes URL in the "go home" button.
 * </ul>
 */
public class RedirectReferenceapplicationRequestsFilter implements Filter {
  private static final String REFERENCEAPPLICATION_CSS_RESOURCE =
      "resource/referenceapplication/styles/referenceapplication.css";
  private static final String CFL_STYLE_RESOURCE_CONTEXT_RELATIVE =
      "/ms/uiframework/resource/cfl/styles/referenceapplication.css";

  private static final String REFERENCEAPPLICATION_HOME_PAGE = "referenceapplication/home.page";
  private static final String CFL_HOME_PAGE_CONTEXT_RELATIVE = "/cfl/home.page";

  private static final List<RedirectMapping> REDIRECTS =
      asList(
          new RedirectMapping(
              REFERENCEAPPLICATION_CSS_RESOURCE, CFL_STYLE_RESOURCE_CONTEXT_RELATIVE),
          new RedirectMapping(REFERENCEAPPLICATION_HOME_PAGE, CFL_HOME_PAGE_CONTEXT_RELATIVE));

  @Override
  public void init(FilterConfig filterConfig) {
    // needs to be overwritten, do nothing
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    if (servletRequest instanceof HttpServletRequest) {
      final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

      for (RedirectMapping redirectMapping : REDIRECTS) {
        if (shouldRedirect(redirectMapping, httpServletRequest)) {
          redirect(redirectMapping, httpServletResponse);
          return;
        }
      }
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
    // needs to be overwritten, do nothing
  }

  private boolean shouldRedirect(
      RedirectMapping redirectMapping, HttpServletRequest httpServletRequest) {
    return URI.create(httpServletRequest.getRequestURI())
        .getPath()
        .endsWith(redirectMapping.resourceToRedirectPath);
  }

  @SuppressWarnings("findsecbugs:UNVALIDATED_REDIRECT")
  private void redirect(RedirectMapping redirectMapping, HttpServletResponse httpServletResponse)
      throws IOException {
    final String cflStyleLocation =
        "/" + WebConstants.CONTEXT_PATH + redirectMapping.contextRelativeRedirectPath;
    if (isRedirectURLTrusted(cflStyleLocation)) {
      httpServletResponse.sendRedirect(cflStyleLocation);
    }
  }

  private boolean isRedirectURLTrusted(String uri) {
    return REDIRECTS.stream()
        .anyMatch(
            redirectMapping ->
                StringUtils.equalsIgnoreCase(
                    "/" + WebConstants.CONTEXT_PATH + redirectMapping.contextRelativeRedirectPath,
                    uri));
  }

  private static class RedirectMapping {
    final String resourceToRedirectPath;
    final String contextRelativeRedirectPath;

    private RedirectMapping(String resourceToRedirectPath, String contextRelativeRedirectPath) {
      this.resourceToRedirectPath = resourceToRedirectPath;
      this.contextRelativeRedirectPath = contextRelativeRedirectPath;
    }
  }
}
