package org.openmrs.module.cfl.api.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.cfl.CfldistributionConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Arrays;
import java.util.List;

public class CustomUserAppServiceImplTest extends BaseModuleContextSensitiveTest {

  private static final String MY_PROJECT_NAME = "my";

  @Mock private AppFrameworkService appFrameworkService;

  @Before
  public void setup() {
    this.contextMockHelper.setService(AppFrameworkService.class, appFrameworkService);
  }

  @Test
  public void shouldReplaceCommonExtensionWithMyProjectExtension() {
    final AppDescriptor commonTestApp = createCommonTestApp();

    Mockito.when(appFrameworkService.getAllApps())
        .thenReturn(Arrays.asList(commonTestApp, createMyProjectTestApp()));

    final Location location = createLocationForMyProject();
    final List<Extension> extensionsToReplace = commonTestApp.getExtensions();
    final CustomUserAppServiceImpl service = new CustomUserAppServiceImpl();

    service.setSpecificAppExtensionsByLocation(location, extensionsToReplace);

    Assert.assertFalse(extensionsToReplace.isEmpty());
    Assert.assertEquals("extension.test.app.my", extensionsToReplace.get(0).getId());
  }

  private Location createLocationForMyProject() {
    final LocationAttributeType locationAttributeType = new LocationAttributeType();
    locationAttributeType.setName(CfldistributionConstants.PROJECT_LOCATION_ATTRIBUTE_TYPE_NAME);

    final LocationAttribute locationAttribute = new LocationAttribute();
    locationAttribute.setValueReferenceInternal(MY_PROJECT_NAME);
    locationAttribute.setAttributeType(locationAttributeType);

    final Location location = new Location();
    location.addAttribute(locationAttribute);
    return location;
  }

  private AppDescriptor createCommonTestApp() {
    final Extension appExtension =
        new Extension("extension.test.app", "test.app", "", "", "", "", 0);
    final AppDescriptor app = new AppDescriptor("test.app", "", "", "", "", "", 0);
    app.addExtension(appExtension);
    appExtension.setBelongsTo(app);
    return app;
  }

  private AppDescriptor createMyProjectTestApp() {
    final Extension appExtension =
        new Extension("extension.test.app.my", "test.app.my", "", "", "", "", 0);
    final AppDescriptor app = new AppDescriptor("test.app.my", "", "", "", "", "", 0);
    app.addExtension(appExtension);
    appExtension.setBelongsTo(app);
    return app;
  }
}
