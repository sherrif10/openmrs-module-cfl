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
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.cfl.api.activator.ModuleActivatorStep;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static org.openmrs.module.cfl.CfldistributionGlobalParameterConstants.SHOULD_DISABLE_APPS_AND_EXTENSIONS_KEY;
import static org.openmrs.module.cfl.api.activator.impl.ModuleActivatorStepOrderEnum.CONFIGURE_PATIENT_DASHBOARD_APPS_ACTIVATOR_STEP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.ACTIVE_VISIT_STATUS_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.ALLERGYUI_PATIENT_DASHBOARD_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.APPOINTMENTSCHEDULINGUI_HOME_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.APPOINTMENTSCHEDULINGUI_TAB_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.ATTACHMENTS_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.ATTACHMENTS_OVERALL_ACTION_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.CFL_LATESTOBSFORCONCEPTLIS_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.CFL_PATIENT_DASHBOARD_IMPROVEMENTS_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.CFL_RELATIONSHIPS_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_ACTIVE_VISITS_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_CONDITIONLIST_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_CREATE_RETROSPECTIVE_VISIT_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_CREATE_VISIT_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_DATA_MANAGEMENT_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_LATEST_OBS_FOR_CONCEPT_LIST_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_MARK_PATIENT_DECEASED_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_MERGE_VISITS_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_MOST_RECENT_VITALS_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_OBS_ACROSS_ENCOUNTERS_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_OBS_GRAPH_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.COREAPPS_RELATIONSHIPS_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.LOCATIONBASEDACCESS_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.LOCATIONBASEDACCES_PATIENT_HEADER_LOCATION_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.PATIENTFLAGS_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.PATIENT_DASHBOARD_APPOINTMENTS_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.REGISTRATION_APP_EDIT_PATIENT_DASHBOARD_EXT;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.REQUEST_APPOINTMENT_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.SCHEDULING_APPOINTMENT_APP;
import static org.openmrs.module.cfl.api.util.PatientDashboardAppsConstants.STICKY_NOTE_EXT;

/**
 * The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with
 * annotated beans.
 */
public class ConfigurePatientDashboardAppsActivatorStep implements ModuleActivatorStep {

  /** The list of app which should be disabled */
  private static final List<String> APP_IDS =
      Arrays.asList(
          APPOINTMENTSCHEDULINGUI_HOME_APP,
          SCHEDULING_APPOINTMENT_APP,
          REQUEST_APPOINTMENT_APP,
          COREAPPS_CONDITIONLIST_APP,
          COREAPPS_LATEST_OBS_FOR_CONCEPT_LIST_APP,
          COREAPPS_OBS_ACROSS_ENCOUNTERS_APP,
          COREAPPS_OBS_GRAPH_APP,
          COREAPPS_RELATIONSHIPS_APP,
          COREAPPS_MOST_RECENT_VITALS_APP,
          COREAPPS_ACTIVE_VISITS_APP,
          COREAPPS_DATA_MANAGEMENT_APP);

  private static final List<String> CFL_ADDITIONAL_MODIFICATION_APP_IDS =
      Arrays.asList(
          CFL_PATIENT_DASHBOARD_IMPROVEMENTS_APP,
          CFL_RELATIONSHIPS_APP,
          CFL_LATESTOBSFORCONCEPTLIS_APP);

  /** The list of extensions which should be disabled */
  private static final List<String> EXTENSION_IDS =
      Arrays.asList(
          APPOINTMENTSCHEDULINGUI_TAB_EXT,
          PATIENT_DASHBOARD_APPOINTMENTS_EXT,
          STICKY_NOTE_EXT,
          ACTIVE_VISIT_STATUS_EXT,
          COREAPPS_CREATE_VISIT_EXT,
          COREAPPS_CREATE_RETROSPECTIVE_VISIT_EXT,
          COREAPPS_MERGE_VISITS_EXT,
          ALLERGYUI_PATIENT_DASHBOARD_EXT,
          PATIENTFLAGS_EXT,
          ATTACHMENTS_EXT,
          ATTACHMENTS_OVERALL_ACTION_EXT,
          LOCATIONBASEDACCESS_EXT,
          COREAPPS_MARK_PATIENT_DECEASED_EXT,
          LOCATIONBASEDACCES_PATIENT_HEADER_LOCATION_EXT);

  private static final List<String> DISABLE_EXTENSIONS_IDS =
      Collections.singletonList(REGISTRATION_APP_EDIT_PATIENT_DASHBOARD_EXT);

  @Override
  public int getOrder() {
    return CONFIGURE_PATIENT_DASHBOARD_APPS_ACTIVATOR_STEP.ordinal();
  }

  @Override
  public void startup(Log log) {
    final AdministrationService administrationService = Context.getAdministrationService();
    final AppFrameworkService appFrameworkService = Context.getService(AppFrameworkService.class);

    disableUnusedExtensions(appFrameworkService);

    if (parseBoolean(
        administrationService.getGlobalProperty(SHOULD_DISABLE_APPS_AND_EXTENSIONS_KEY))) {
      enableAdditionalConfiguration(appFrameworkService);
    } else {
      enableDefaultConfiguration(appFrameworkService);
    }
  }

  private void disableUnusedExtensions(AppFrameworkService appFrameworkService) {
    disableExtensions(appFrameworkService, DISABLE_EXTENSIONS_IDS);
  }

  private void enableAdditionalConfiguration(AppFrameworkService appFrameworkService) {
    enableApps(appFrameworkService);
    disableApps(appFrameworkService, APP_IDS);
    disableExtensions(appFrameworkService, EXTENSION_IDS);
  }

  private void enableDefaultConfiguration(AppFrameworkService appFrameworkService) {
    disableApps(appFrameworkService, CFL_ADDITIONAL_MODIFICATION_APP_IDS);
  }

  private void enableApps(AppFrameworkService service) {
    for (String app :
        ConfigurePatientDashboardAppsActivatorStep.CFL_ADDITIONAL_MODIFICATION_APP_IDS) {
      service.enableApp(app);
    }
  }

  private void disableApps(AppFrameworkService service, List<String> appIds) {
    for (String app : appIds) {
      service.disableApp(app);
    }
  }

  private void disableExtensions(AppFrameworkService service, List<String> extensions) {
    for (String ext : extensions) {
      service.disableExtension(ext);
    }
  }
}
