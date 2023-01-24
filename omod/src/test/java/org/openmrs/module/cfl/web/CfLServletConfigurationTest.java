package org.openmrs.module.cfl.web;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.openmrs.module.cfl.filter.CflSecurityHeadersFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

public class CfLServletConfigurationTest {
  @Rule public TemporaryFolder testFolder = new TemporaryFolder();

  private static final String CFL_UNCAUGHT_EXCEPTION_PAGE_LOCATION =
      "WEB-INF/view/module/cfl/cflUncaughtException.jsp";

  private static final String CFL_UNCAUGHT_EXCEPTION_PAGE_DESTINATION_LOCATION =
      "WEB-INF/view/cflUncaughtException.jsp";

  @Test
  public void shouldCopyCflUncaughtExceptionFromModuleToViewWEBINF() throws IOException {
    prepareCflUncaughtExceptionFile();

    final ServletContext servletContext = Mockito.mock(ServletContext.class);
    Mockito.when(servletContext.getRealPath("")).thenReturn(testFolder.getRoot().getAbsolutePath());

    final CfLServletConfiguration configuration = new CfLServletConfiguration();
    configuration.setServletContext(servletContext);

    final Path expectedLocationAfterCopy =
        testFolder.getRoot().toPath().resolve(CFL_UNCAUGHT_EXCEPTION_PAGE_DESTINATION_LOCATION);
    Assert.assertTrue(Files.exists(expectedLocationAfterCopy));
  }

  @Test
  public void shouldAddSecurityFilter() {
    final FilterRegistration.Dynamic filterRegistrationMock =
        Mockito.mock(FilterRegistration.Dynamic.class);

    final ServletContext servletContextMock = Mockito.mock(ServletContext.class);
    Mockito.when(servletContextMock.getRealPath(""))
        .thenReturn(testFolder.getRoot().getAbsolutePath());
    Mockito.when(
            servletContextMock.addFilter(
                Mockito.eq("CflSecurityHeadersFilter"),
                Mockito.any(CflSecurityHeadersFilter.class)))
        .thenReturn(filterRegistrationMock);

    final CfLServletConfiguration configuration = new CfLServletConfiguration();
    configuration.setServletContext(servletContextMock);

    Mockito.verify(filterRegistrationMock)
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
  }

  private void prepareCflUncaughtExceptionFile() throws IOException {
    final Path fileToCopyPath =
        testFolder.getRoot().toPath().resolve(CFL_UNCAUGHT_EXCEPTION_PAGE_LOCATION);
    Files.createDirectories(fileToCopyPath.getParent());
    Files.createFile(fileToCopyPath);
  }
}
