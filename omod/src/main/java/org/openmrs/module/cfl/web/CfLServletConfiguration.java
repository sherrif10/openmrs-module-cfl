package org.openmrs.module.cfl.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.cfl.filter.CflSecurityHeadersFilter;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.EnumSet;

/**
 * CfLServletConfiguration configures web server servlet, bypassing OpenMRS Framework.
 *
 * <ul>
 *   <li>Copies cflUncaughtException.jsp to root WEB-INF.
 *   <li>Configures security Filter.
 * </ul>
 */
public class CfLServletConfiguration implements ServletContextAware {

  private static final Log LOGGER = LogFactory.getLog(CfLServletConfiguration.class);
  private static final Path MODULE_ROOT_DIR = Paths.get("WEB-INF", "view", "module", "cfl");
  private static final Path CFL_UNCAUGHT_EXCEPTION_FILE = Paths.get("cflUncaughtException.jsp");
  private static final Path WEB_INF_VIEW_DIR = Paths.get("WEB-INF", "view");

  @Override
  public void setServletContext(ServletContext servletContext) {
    copyCflUncaughtExceptionPage(servletContext);
    addSecurityFilter(servletContext);
  }

  private void copyCflUncaughtExceptionPage(ServletContext servletContext) {
    Path basePath = Paths.get(servletContext.getRealPath(""));
    try {
      Path srcFile = basePath.resolve(MODULE_ROOT_DIR).resolve(CFL_UNCAUGHT_EXCEPTION_FILE);
      Path destFile = basePath.resolve(WEB_INF_VIEW_DIR).resolve(CFL_UNCAUGHT_EXCEPTION_FILE);
      Files.copy(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      LOGGER.error("Failed to copy cflUncaughtException.jsp", e);
    }
  }

  private void addSecurityFilter(ServletContext servletContext) {
    try {
      servletContext
          .addFilter("CflSecurityHeadersFilter", new CflSecurityHeadersFilter())
          .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    } catch (Exception e) {
      LOGGER.error("Failed to add CflSecurityHeadersFilter", e);
    }
  }
}
