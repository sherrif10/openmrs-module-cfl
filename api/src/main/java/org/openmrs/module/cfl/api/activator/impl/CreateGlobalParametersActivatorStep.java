/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.activator.impl;

import org.apache.commons.logging.Log;
import org.openmrs.module.cfl.CfldistributionGlobalParameterConstants;
import org.openmrs.module.cfl.api.activator.ModuleActivatorStep;
import org.openmrs.module.cflcore.CFLConstants;

import static org.openmrs.module.cfl.api.activator.impl.ModuleActivatorStepOrderEnum.CREATE_GLOBAL_PARAMETERS_ACTIVATOR_STEP;
import static org.openmrs.module.cflcore.api.util.GlobalPropertyUtils.createGlobalSettingIfNotExists;

/**
 * The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with
 * annotated beans.
 */
public class CreateGlobalParametersActivatorStep implements ModuleActivatorStep {
  @Override
  public int getOrder() {
    return CREATE_GLOBAL_PARAMETERS_ACTIVATOR_STEP.ordinal();
  }

  @Override
  public void startup(Log log) {
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.CFL_DISTRO_BOOTSTRAPPED_KEY,
        CfldistributionGlobalParameterConstants.CFL_DISTRO_BOOTSTRAPPED_DEFAULT_VALUE,
        CfldistributionGlobalParameterConstants.CFL_DISTRO_BOOTSTRAPPED_DEFAULT_DESCRIPTION);
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.SHOULD_DISABLE_APPS_AND_EXTENSIONS_KEY,
        CfldistributionGlobalParameterConstants.SHOULD_DISABLE_APPS_AND_EXTENSIONS_DEFAULT_VALUE,
        CfldistributionGlobalParameterConstants.SHOULD_DISABLE_APPS_AND_EXTENSIONS_DESCRIPTION);
    createGlobalSettingIfNotExists(
        CFLConstants.LOCATION_ATTRIBUTE_GLOBAL_PROPERTY_NAME,
        CfldistributionGlobalParameterConstants.CFL_LOCATION_ATTRIBUTE_TYPE_UUID);
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.CFL_SHOW_STACKTRACE_IN_ERROR_PAGE_KEY,
        CfldistributionGlobalParameterConstants.CFL_SHOW_STACKTRACE_IN_ERROR_PAGE_DEFAULT_VALUE,
        CfldistributionGlobalParameterConstants.CFL_SHOW_STACKTRACE_IN_ERROR_PAGE_DESCRIPTION);
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SECRET_KEY,
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SECRET_VALUE,
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SECRET_DESCRIPTION);
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SITE_KEY,
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SITE_VALUE,
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_SITE_DESCRIPTION);
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.CAPTCHA_ENABLE_KEY,
        CfldistributionGlobalParameterConstants.CAPTCHA_ENABLED_DEFAULT_VALUE,
        CfldistributionGlobalParameterConstants.CAPTCHA_ENABLED_DESCRIPTION);
    createGlobalSettingIfNotExists(
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_MAX_FAILED_ATTEMPTS_KEY,
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_MAX_FAILED_ATTEMPTS_DEFAULT_VALUE,
        CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_MAX_FAILED_ATTEMPTS_DESCRIPTION);
  }
}
