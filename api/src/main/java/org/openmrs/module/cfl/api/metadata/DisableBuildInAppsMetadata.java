/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.metadata;

import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

/**
 * Disables Apps from CfL dependencies, which are not used by CfL or are replaced by other app.
 */
public class DisableBuildInAppsMetadata extends VersionedMetadataBundle {
    /**
     * Replaced by cflui.findPatient
     */
    private final static String CORE_FIND_PATIENT_APP = "coreapps.findPatient";

    private AppFrameworkService appFrameworkService;

    @Override
    public int getVersion() {
        return 1;
    }

    public void setAppFrameworkService(AppFrameworkService appFrameworkService) {
        this.appFrameworkService = appFrameworkService;
    }

    @Override
    protected void installEveryTime() {
        // nothing to do
    }

    @Override
    protected void installNewVersion() {
        appFrameworkService.disableApp(CORE_FIND_PATIENT_APP);
    }
}
