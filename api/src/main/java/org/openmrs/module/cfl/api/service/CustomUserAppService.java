package org.openmrs.module.cfl.api.service;

import java.util.List;
import org.openmrs.Location;
import org.openmrs.module.appframework.domain.Extension;

public interface CustomUserAppService {

  /**
   * Sets specific app extensions based on user location. For example if registration form tile is
   * displayed on home page, this method checks if there is a specific registration form for current
   * user location (based on 'Project' location attribute) and replaces it.
   *
   * @param location   user location
   * @param extensions current user app extensions
   */
  void setSpecificAppExtensionsByLocation(Location location, List<Extension> extensions);
}
