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
import org.openmrs.module.cfl.api.activator.ModuleActivatorStep;
import org.openmrs.module.emrapi.utils.MetadataUtil;

import static org.openmrs.module.cfl.api.activator.impl.ModuleActivatorStepOrderEnum.INSTALL_METADATA_PACKAGES_ACTIVATOR_STEP;

/**
 * The Metadata Package are ZIP files in the classpath, the import mode defined by packages.xml
 * <p>
 * The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with annotated beans.
 * </p>
 */
public class InstallMetadataPackagesActivatorStep implements ModuleActivatorStep {
    @Override
    public int getOrder() {
        return INSTALL_METADATA_PACKAGES_ACTIVATOR_STEP.ordinal();
    }

    @Override
    public void startup(Log log) throws Exception {
        MetadataUtil.setupStandardMetadata(getClass().getClassLoader());
    }
}
