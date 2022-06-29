/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.cfl.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.cfl.CflWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Intercepts authenticated requests to check if the logged in user has selected a location and will
 * redirect them to the login page so they can select one in case they haven't yet.
 */
public class CfldistributionRequireLoginLocationFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(
      CfldistributionRequireLoginLocationFilter.class);
	
	private String loginRequestUri;
	
	private String logoutRequestUri;
	
	private String[] allowedRequestURIs;
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing RequireLoginLocationFilter...");
		}
	}
	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("findsecbugs:UNVALIDATED_REDIRECT")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
		
		if (loginRequestUri == null) {
			UiUtils uiUtils = Context.getRegisteredComponent("uiUtils", UiUtils.class);
			loginRequestUri = uiUtils.pageLink(CflWebConstants.MODULE_ID, "login");
		}
		if (logoutRequestUri == null) {
			logoutRequestUri = "/" + WebConstants.CONTEXT_PATH + "/logout";
		}
		
		if (allowedRequestURIs == null) {
			allowedRequestURIs = new String[] { loginRequestUri, logoutRequestUri };
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		if (!skipFilter(httpRequest.getRequestURI()) && Context.isAuthenticated() && Context.getUserContext().getLocationId() == null) {
			logger.debug("Redirecting user to location selection page");

			//The user needs to select a login location
			HttpServletResponse resp = ((HttpServletResponse) response);
			resp.setStatus(HttpStatus.OK.value());
			if (isRedirectURLTrusted(loginRequestUri)) {
				resp.sendRedirect(loginRequestUri);
			}

			return;
		}

		chain.doFilter(request, response);
	}

	private boolean isRedirectURLTrusted(String uri) {
		return Arrays.asList(allowedRequestURIs).contains(uri);
	}
	
	/**
	 * Determines if the filter should be skipped for the specified request uri, typically all requests
	 * for static content, non ref app pages, login and logout are be ignored.
	 * 
	 * @param requestUri the request uri to check
	 * @return true if the filter should be skipped otherwise false
	 */
	private boolean skipFilter(String requestUri) {
		if (requestUri.endsWith(".page")) {
			return ArrayUtils.indexOf(allowedRequestURIs, requestUri) > -1;
		}
		
		return true;
	}
	
	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		if (logger.isDebugEnabled()) {
			logger.debug("Destroying RequireLoginLocationFilter...");
		}
	}
	
}
