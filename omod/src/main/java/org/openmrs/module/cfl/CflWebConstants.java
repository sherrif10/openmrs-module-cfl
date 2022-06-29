/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl;

public final class CflWebConstants {

  public static final String MODULE_ID = "cfl";

  /**
   * The name of Global Property with a name of User Property where user's location UUID is saved.
   * This property is created by Location Based Access Control module.
   */
  public static final String LOCATION_USER_PROPERTY_NAME =
      "referenceapplication.locationUserPropertyName";

  public static final String HOME_PAGE_EXTENSION_POINT_ID =
      "org.openmrs.cfl.homepageLink";

  public static final String SESSION_ATTRIBUTE_INFO_MESSAGE =
      "_REFERENCE_APPLICATION_INFO_MESSAGE_";

  public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE =
      "_REFERENCE_APPLICATION_ERROR_MESSAGE_";

  public static final String SESSION_ATTRIBUTE_REDIRECT_URL =
      "_REFERENCE_APPLICATION_REDIRECT_URL_";

  public static final String REQUEST_PARAMETER_NAME_REDIRECT_URL = "redirectUrl";

  public static final String COOKIE_NAME_LAST_SESSION_LOCATION =
      "cfl.lastSessionLocation";

  public static final String COOKIE_NAME_LAST_USER = "_REFERENCE_APPLICATION_LAST_USER_";
}
