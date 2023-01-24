/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.service;

import javax.servlet.http.HttpServletRequest;

/** This service is used to get user's response to the reCAPTCHA challenge and validate response. */
public interface CaptchaService {

  /**
   * Retrieves and validates response
   *
   * @param request
   */
  void processResponse(HttpServletRequest request);
}
