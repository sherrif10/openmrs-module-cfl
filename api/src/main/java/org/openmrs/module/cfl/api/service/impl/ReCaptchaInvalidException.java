/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.service.impl;

import org.openmrs.api.APIException;

public final class ReCaptchaInvalidException extends APIException {

  private static final long serialVersionUID = 5861310537366287163L;

  public ReCaptchaInvalidException() {
    super();
  }

  public ReCaptchaInvalidException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ReCaptchaInvalidException(final String message) {
    super(message);
  }

  public ReCaptchaInvalidException(final Throwable cause) {
    super(cause);
  }
}
