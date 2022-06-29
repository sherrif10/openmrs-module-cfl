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

import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cflcore.CFLConstants;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

import java.util.Optional;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.locationTag;

public class LocationMetadata extends VersionedMetadataBundle {
  public static final String LOGIN_LOCATION_TAG_NAME = "Login Location";
  public static final String VISIT_LOCATION_TAG_NAME = "Visit Location";
  private static final String LOCATION_UUID_PROPERTY_NAME = "locationUuid";
  private static final String UNKNOWN_LOCATION_UUID = "8d6c993e-c2cc-11de-8d13-0010c6dffd0f";
  private static final String VISIT_LOCATION_UUID = "37dd4458-dc9e-4ae6-a1f1-789c1162d37b";

  @Override
  public int getVersion() {
    return 1;
  }

  @Override
  protected void installEveryTime() {
    // needs to be overwritten, do nothing
  }

  @Override
  public void installNewVersion() {
    install(locationTag(VISIT_LOCATION_TAG_NAME, "", VISIT_LOCATION_UUID));

    updateAdminLocationToCFLClinic();
    updateCFLClinicLocation();
    addNewLocationTagToLocation(UNKNOWN_LOCATION_UUID, LOGIN_LOCATION_TAG_NAME);
    addNewLocationTagToLocation(UNKNOWN_LOCATION_UUID, VISIT_LOCATION_TAG_NAME);
  }

  private void updateAdminLocationToCFLClinic() {
    final UserService userService = Context.getUserService();
    final User user = userService.getUserByUsername(CFLConstants.ADMIN_USER_NAME);

    if (user != null) {
      user.setUserProperty(LocationMetadata.LOCATION_UUID_PROPERTY_NAME, LocationMetadata.UNKNOWN_LOCATION_UUID);
    } else {
      log.warn(String.format("User with username: %s not found", CFLConstants.ADMIN_USER_NAME));
    }
  }

  private void updateCFLClinicLocation() {
    final Location location =
        Context.getLocationService().getLocationByUuid(LocationMetadata.UNKNOWN_LOCATION_UUID);

    if (location == null) {
      log.warn(
          String.format(
              "Location with uuid: %s not found", LocationMetadata.UNKNOWN_LOCATION_UUID));
      return;
    }

    location.setName("CFL Clinic");

    setAttribute(location, "siteCode", "BEL1");
    setAttribute(location, "countryCode", "BEL");
    setAttribute(location, "cluster", "Flanders");
  }

  private void setAttribute(Location location, String attributeName, String attributeValue) {
    getAttributeType(attributeName)
        .ifPresent(
            attributeType ->
                location.setAttribute(locationAttribute(attributeType, location, attributeValue)));
  }

  private Optional<LocationAttributeType> getAttributeType(String name) {
    final LocationService locationService = Context.getLocationService();

    return Optional.ofNullable(locationService.getLocationAttributeTypeByName(name));
  }

  private LocationAttribute locationAttribute(
      LocationAttributeType locationAttributeType, Location location, String value) {
    final LocationAttribute locationAttribute = new LocationAttribute();
    locationAttribute.setAttributeType(locationAttributeType);
    locationAttribute.setLocation(location);
    locationAttribute.setValueReferenceInternal(value);
    return locationAttribute;
  }

  private void addNewLocationTagToLocation(String locationUuid, String locationTagName) {
    final LocationService locationService = Context.getLocationService();
    final Location location = locationService.getLocationByUuid(locationUuid);

    if (location != null) {
      location.addTag(locationService.getLocationTagByName(locationTagName));
    } else {
      log.warn(String.format("Location with uuid: %s not found", locationUuid));
    }
  }
}
