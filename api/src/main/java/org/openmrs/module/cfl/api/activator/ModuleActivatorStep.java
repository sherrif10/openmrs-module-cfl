/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.activator;

import org.apache.commons.logging.Log;
import org.openmrs.module.cfl.api.activator.impl.ModuleActivatorStepOrderEnum;

/**
 * The ModuleActivatorStep Class.
 * <p>
 * The ModuleActivatorSteps are beans which performs single logical step during module startup. E.g.: fix concepts, import
 * necessary data, validate db state.
 * </p>
 * <p>
 * Each implementation has its order value which determines the order in which these steps are executed, the lower the
 * order value the sooner it gets executed.
 * </p>
 * <p>
 * Each step is executed only once during the module startup.
 * </p>
 */
public interface ModuleActivatorStep {
    /**
     * <p>
     * <b>Impl note:</b> Use {@link ModuleActivatorStepOrderEnum} to generate an actual value to return by this method.
     * </p>
     *
     * @return the integer used to determine when this step has to execute, lower value goes before higher
     */
    int getOrder();

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    void startup(Log log) throws Exception;
}
