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

/**
 * The enumeration of all activator steps in order of execution.
 * <p>
 * The order in which enum constants appear in the source code directly reflects the order of steps execution.
 * </p>
 */
public enum ModuleActivatorStepOrderEnum {
    /**
     * @see CreateGlobalParametersActivatorStep
     */
    CREATE_GLOBAL_PARAMETERS_ACTIVATOR_STEP,
    /**
     * @see FixRiskFactorForHIVConceptsActivatorStep
     */
    FIX_RISK_FACTOR_FOR_HIV_CONCEPTS_ACTIVATOR_STEP,
    /**
     * @see InstallMetadataPackagesActivatorStep
     */
    INSTALL_METADATA_PACKAGES_ACTIVATOR_STEP,
    /**
     * @see ConfigurePatientDashboardAppsActivatorStep
     */
    CONFIGURE_PATIENT_DASHBOARD_APPS_ACTIVATOR_STEP,
    /**
     * @see InstallMetadataBundleActivatorStep
     */
    INSTALL_METADATA_BUNDLE_ACTIVATOR_STEP
}
