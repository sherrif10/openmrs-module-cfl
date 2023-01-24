package org.openmrs.module.cfl.api.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.cfl.CfldistributionConstants;
import org.openmrs.module.cfl.api.service.CustomUserAppService;

public class CustomUserAppServiceImpl implements CustomUserAppService {

  @Override
  public void setSpecificAppExtensionsByLocation(Location location, List<Extension> extensions) {
    if (location != null) {
      String projectName = getProjectNameByLocation(location);
      if (StringUtils.isNotBlank(projectName)) {
        replaceExtensionsIfRequired(extensions, projectName);
      }
    }
  }

  private void replaceExtensionsIfRequired(List<Extension> extensions, String projectName) {
    List<Extension> allAvailableProjectExtensions = getAllProjectSpecificExtensions(projectName);
    for (int i = 0; i < extensions.size(); i++) {
      String projectSpecificExtensionName = getProjectExtensionName(extensions.get(i), projectName);
      Optional<Extension> projectSpecificExtension = findSpecificExtension(
          allAvailableProjectExtensions, projectSpecificExtensionName);
      if (projectSpecificExtension.isPresent()) {
        extensions.set(i, projectSpecificExtension.get());
      }
    }
  }

  private String getProjectNameByLocation(Location location) {
    String projectName = null;
    Optional<LocationAttribute> locationProjectAttribute = location.getActiveAttributes().stream()
        .filter(attribute -> StringUtils.equalsIgnoreCase(attribute.getAttributeType().getName(),
            CfldistributionConstants.PROJECT_LOCATION_ATTRIBUTE_TYPE_NAME))
        .findFirst();

    if (locationProjectAttribute.isPresent()) {
      projectName = locationProjectAttribute.get().getValueReference();
    }

    return projectName;
  }

  private List<Extension> getAllProjectSpecificExtensions(String projectName) {
    return Context.getService(AppFrameworkService.class).getAllApps().stream()
        .filter(app -> StringUtils.endsWith(app.getId(), projectName))
        .flatMap(app -> app.getExtensions().stream())
        .collect(Collectors.toList());
  }

  private String getProjectExtensionName(Extension extension, String projectName) {
    String extensionName = null;
    AppDescriptor extDescriptor = extension.getBelongsTo();
    if (extDescriptor != null) {
      extensionName = extDescriptor.getId().concat(".").concat(projectName);
    }

    return extensionName;
  }

  private Optional<Extension> findSpecificExtension(List<Extension> list, String appId) {
    return list.stream()
        .filter(ext -> ext.getBelongsTo() != null)
        .filter(ext -> StringUtils.equals(ext.getBelongsTo().getId(), appId))
        .findFirst();
  }
}
