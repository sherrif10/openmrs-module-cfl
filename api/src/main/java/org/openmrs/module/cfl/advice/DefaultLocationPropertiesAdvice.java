/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.advice;

import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.cfl.api.metadata.LocationMetadata;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * The DefaultLocationPropertiesAdvice executes before Location#save methods and ensures the default
 * properties are set for new Location. That includes adding proper Location Tags.
 */
public class DefaultLocationPropertiesAdvice implements MethodBeforeAdvice {

  private static final int LOCATION_ARG = 0;

  @Override
  public void before(Method method, Object[] args, Object target) {
    if (LocationService.class.isAssignableFrom(method.getDeclaringClass())
        && "saveLocation".equals(method.getName())) {
      addDefaultLocationTags((LocationService) target, (Location) args[LOCATION_ARG]);
    }
  }

  private void addDefaultLocationTags(LocationService locationService, Location location) {
    if (isNew(location)) {
      getLocationTag(locationService, LocationMetadata.LOGIN_LOCATION_TAG_NAME)
          .ifPresent(location::addTag);
      getLocationTag(locationService, LocationMetadata.VISIT_LOCATION_TAG_NAME)
          .ifPresent(location::addTag);
    }
  }

  private boolean isNew(Location location) {
    return location.getId() == null;
  }

  private Optional<LocationTag> getLocationTag(LocationService locationService, String tagName) {
    return Optional.ofNullable(locationService.getLocationTagByName(tagName));
  }
}
