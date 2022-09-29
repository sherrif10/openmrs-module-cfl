/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.page.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.AppUiConstants;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.cfl.CflWebConstants;
import org.openmrs.module.cfl.api.service.UserNotAuthorizedService;
import org.openmrs.module.cflcore.CFLConstants;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.module.emrapi.utils.GeneralUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.web.user.CurrentUsers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Spring MVC controller that takes over /login.htm and processes requests to authenticate a user
 */
@Controller
public class LoginPageController {

  private static final String REDIRECT_PREFIX = "redirect:";

  private static final String HTTP_PREFIX = "http://";

  private static final String HTTPS_PREFIX = "https://";

  private static final String LOGIN_PAGE_NAME = "login";

  // see TRUNK-4536 for details why we need this
  private static final String GET_LOCATIONS = "Get Locations";

  // RA-592: don't use PrivilegeConstants.VIEW_LOCATIONS
  private static final String VIEW_LOCATIONS = "View Locations";

  private static final Log LOGGER = LogFactory.getLog(LoginPageController.class);

  private static final String STAGING = "Staging";

  @RequestMapping(value = "/login.htm", method = RequestMethod.GET)
  public String overrideLoginPageGet() {
    return "forward:/" + CflWebConstants.MODULE_ID + "/login.page";
  }

  @RequestMapping(value = "/login.htm", method = RequestMethod.POST)
  public String overrideLoginPagePost() {
    return "forward:/" + CflWebConstants.MODULE_ID + "/login.page";
  }

  /**
   * @should redirect the user to the home page if they are already authenticated
   * @should show the user the login page if they are not authenticated
   * @should set redirectUrl in the page model if any was specified in the request
   * @should set the referer as the redirectUrl in the page model if no redirect param exists
   * @should set redirectUrl in the page model if any was specified in the session
   * @should not set the referer as the redirectUrl in the page model if referer URL is outside
   *     context path
   * @should set the referer as the redirectUrl in the page model if referer URL is within context
   *     path
   */
  @SuppressWarnings({
    "checkstyle:ParameterNumber",
    "checkstyle:ParameterAssignment",
    "PMD.CyclomaticComplexity"
  })
  public String get(
      PageModel model,
      UiUtils ui,
      PageRequest pageRequest,
      @CookieValue(value = CflWebConstants.COOKIE_NAME_LAST_SESSION_LOCATION, required = false)
          String lastSessionLocationId,
      @SpringBean("locationService") LocationService locationService,
      @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService,
      @SpringBean("adminService") AdministrationService administrationService) {

    final String redirectUrl = getRedirectUrl(pageRequest);
    final String methodResult;

    if (Context.isAuthenticated() && Context.getUserContext().getLocationId() != null) {
      methodResult = getRedirectForAuthenticated(redirectUrl, pageRequest, ui);
    } else {
      initLoginPageModel(
          model,
          redirectUrl,
          pageRequest,
          lastSessionLocationId,
          locationService,
          appFrameworkService,
          administrationService);
      methodResult = null;
    }

    return methodResult;
  }

  @SuppressWarnings("findsecbugs:SPRING_UNVALIDATED_REDIRECT")
  private String getRedirectForAuthenticated(
      String redirectUrl, PageRequest pageRequest, UiUtils ui) {
    if (StringUtils.isNotBlank(redirectUrl)) {
      String url = getRelativeUrl(redirectUrl, pageRequest);
      if (StringUtils.isNotBlank(url) && isRedirectURLTrusted(url)) {
        return REDIRECT_PREFIX + url;
      }
    }
    String url = ui.pageLink(CflWebConstants.MODULE_ID, "home");
    if (StringUtils.isNotBlank(url) && isRedirectURLTrusted(url)) {
      return REDIRECT_PREFIX + url;
    }

    return "";
  }

  private void initLoginPageModel(
      PageModel model,
      String redirectUrl,
      PageRequest pageRequest,
      String lastSessionLocationId,
      LocationService locationService,
      AppFrameworkService appFrameworkService,
      AdministrationService administrationService) {
    model.addAttribute(
        CflWebConstants.REQUEST_PARAMETER_NAME_REDIRECT_URL,
        getRelativeUrl(redirectUrl, pageRequest));

    boolean isLocationUserPropertyAvailable =
        isLocationUserPropertyAvailable(administrationService);
    Object showLocation = pageRequest.getAttribute("showSessionLocations");
    if (showLocation != null && showLocation.toString().equals("true")) {
      // if the request contains a attribute as showSessionLocations,
      // then ignore isLocationUserPropertyAvailable
      isLocationUserPropertyAvailable = false;
    }

    Location lastSessionLocation = null;

    try {
      Context.addProxyPrivilege(VIEW_LOCATIONS);
      Context.addProxyPrivilege(GET_LOCATIONS);
      if (!isLocationUserPropertyAvailable) {
        model.addAttribute("locations", appFrameworkService.getLoginLocations());
      }
      lastSessionLocation = locationService.getLocation(Integer.valueOf(lastSessionLocationId));
    } catch (NumberFormatException ex) {
      // pass
      LOGGER.error(ex.getMessage());
    } finally {
      Context.removeProxyPrivilege(VIEW_LOCATIONS);
      Context.removeProxyPrivilege(GET_LOCATIONS);
    }

    boolean showSessionLocations = !isLocationUserPropertyAvailable;
    boolean selectLocation = false;
    if (isSelectLocationRequest(isLocationUserPropertyAvailable)) {
      selectLocation = true;
      showSessionLocations = true;
      List<Location> locations = getUserLocations(administrationService, locationService);
      if (locations.isEmpty()) {
        locations = appFrameworkService.getLoginLocations();
      }
      model.addAttribute("locations", locations);
    }

    model.addAttribute("showSessionLocations", showSessionLocations);
    model.addAttribute("selectLocation", selectLocation);
    model.addAttribute("lastSessionLocation", lastSessionLocation);
    model.addAttribute("isStagingEnvironment", isStagingEnvironment());
  }

  private boolean isStagingEnvironment() {
    String cflEnvironment =
        Context.getAdministrationService().getGlobalProperty(CFLConstants.ENVIRONMENT_KEY);
    return cflEnvironment != null && StringUtils.equalsIgnoreCase(cflEnvironment, STAGING);
  }

  private boolean isLocationUserPropertyAvailable(AdministrationService administrationService) {
    String locationUserPropertyName =
        administrationService.getGlobalProperty(CflWebConstants.LOCATION_USER_PROPERTY_NAME);

    return StringUtils.isNotBlank(locationUserPropertyName);
  }

  private boolean isUrlWithinOpenmrs(PageRequest pageRequest, String redirectUrl) {
    if (StringUtils.isNotBlank(redirectUrl)) {
      if (redirectUrl.startsWith(HTTP_PREFIX) || redirectUrl.startsWith(HTTPS_PREFIX)) {
        try {
          final URL url = new URL(redirectUrl);
          final String urlContextPath = getContextPath(url);

          if (StringUtils.equals(pageRequest.getRequest().getContextPath(), urlContextPath)) {
            return true;
          }
        } catch (MalformedURLException e) {
          LOGGER.error(e.getMessage());
        }
      } else {
        return redirectUrl.startsWith(pageRequest.getRequest().getContextPath());
      }
    }

    return false;
  }

  private String getContextPath(URL url) {
    final String urlPath = url.getFile();
    final int contextEndIndex = urlPath.indexOf('/', 1);

    return urlPath.substring(
        0, contextEndIndex != -1 ? Math.min(contextEndIndex, urlPath.length()) : urlPath.length());
  }

  private String getRedirectUrlFromReferer(PageRequest pageRequest) {
    String manualLogout =
        pageRequest
            .getSession()
            .getAttribute(AppUiConstants.SESSION_ATTRIBUTE_MANUAL_LOGOUT, String.class);
    String redirectUrl = "";
    if (!Boolean.parseBoolean(manualLogout)) {
      redirectUrl = pageRequest.getRequest().getHeader("Referer");
    } else {
      Cookie cookie = new Cookie(CflWebConstants.COOKIE_NAME_LAST_USER, null);
      cookie.setMaxAge(0);
      cookie.setSecure(true);
      cookie.setHttpOnly(true);
      pageRequest.getResponse().addCookie(cookie);
    }
    pageRequest.getSession().setAttribute(AppUiConstants.SESSION_ATTRIBUTE_MANUAL_LOGOUT, null);
    return redirectUrl;
  }

  private String getRedirectUrlFromRequest(PageRequest pageRequest) {
    return pageRequest
        .getRequest()
        .getParameter(CflWebConstants.REQUEST_PARAMETER_NAME_REDIRECT_URL);
  }

  private String getRedirectUrl(PageRequest pageRequest) {
    String redirectUrl = findRedirectUrlFromDifferentSources(pageRequest);

    if (StringUtils.isNotBlank(redirectUrl) && isUrlWithinOpenmrs(pageRequest, redirectUrl)) {
      return redirectUrl;
    }
    return "";
  }

  private String findRedirectUrlFromDifferentSources(PageRequest pageRequest) {
    String redirectUrl = getRedirectUrlFromRequest(pageRequest);
    if (StringUtils.isNotBlank(redirectUrl)) {
      return redirectUrl;
    }

    redirectUrl =
        getStringSessionAttribute(
            CflWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL, pageRequest.getRequest());
    if (StringUtils.isNotBlank(redirectUrl)) {
      return redirectUrl;
    }

    redirectUrl = getRedirectUrlFromReferer(pageRequest);
    if (StringUtils.isNotBlank(redirectUrl)) {
      return redirectUrl;
    }

    return "";
  }

  /**
   * Processes requests to authenticate a user
   *
   * @param username
   * @param password
   * @param sessionLocationId
   * @param locationService
   * @param ui {@link UiUtils} object
   * @param pageRequest {@link PageRequest} object
   * @param sessionContext
   * @return
   * @should redirect the user back to the redirectUrl if any
   * @should redirect the user to the home page if the redirectUrl is the login page
   * @should send the user back to the login page if an invalid location is selected
   * @should send the user back to the login page when authentication fails
   */
  @SuppressWarnings({
    "checkstyle:ParameterNumber",
    "checkstyle:ParameterAssignment",
    "checkstyle:CyclomaticComplexity",
    "PMD.ExcessiveParameterList",
    "PMD.ExcessiveMethodLength",
    "PMD.NcssCount",
    "PMD.CyclomaticComplexity",
    "PMD.NPathComplexity",
    "PMD.AvoidReassigningParameters",
    "PMD.CollapsibleIfStatements",
    "findsecbugs:SPRING_UNVALIDATED_REDIRECT",
    "java:S3776"
  })
  public String post(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "password", required = false) String password,
      @RequestParam(value = "sessionLocation", required = false) Integer sessionLocationId,
      @SpringBean("locationService") LocationService locationService,
      @SpringBean("adminService") AdministrationService administrationService,
      UiUtils ui,
      @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService,
      PageRequest pageRequest,
      UiSessionContext sessionContext) {

    String redirectUrl = getRedirectUrl(pageRequest);
    redirectUrl = getRelativeUrl(redirectUrl, pageRequest);
    Location sessionLocation = null;
    if (sessionLocationId != null) {
      try {
        Context.addProxyPrivilege(VIEW_LOCATIONS);
        Context.addProxyPrivilege(GET_LOCATIONS);
        sessionLocation = locationService.getLocation(sessionLocationId);
      } finally {
        Context.removeProxyPrivilege(VIEW_LOCATIONS);
        Context.removeProxyPrivilege(GET_LOCATIONS);
      }
    }

    try {
      if (!Context.isAuthenticated()) {
        Context.authenticate(username, password);

        if (isLocationUserPropertyAvailable(administrationService)) {
          List<Location> accessibleLocations =
              getUserLocations(administrationService, locationService);
          if (accessibleLocations.size() == 1) {
            sessionLocation = accessibleLocations.get(0);
          } else if (accessibleLocations.size() > 1) {
            String url = ui.pageLink(CflWebConstants.MODULE_ID, LOGIN_PAGE_NAME);
            if (StringUtils.isNotBlank(url) && isRedirectURLTrusted(url)) {
              return REDIRECT_PREFIX + url;
            }
          }

          // If there is a single login location, default to that
          if (sessionLocation == null) {
            List<Location> loginLocations = appFrameworkService.getLoginLocations();
            if (loginLocations.size() == 1) {
              sessionLocation = loginLocations.get(0);
            }
          }

          if (sessionLocation != null) {
            sessionLocationId = sessionLocation.getLocationId();
          } else {
            String url = ui.pageLink(CflWebConstants.MODULE_ID, LOGIN_PAGE_NAME);
            if (StringUtils.isNotBlank(url) && isRedirectURLTrusted(url)) {
              return REDIRECT_PREFIX + url;
            }
          }
        }
      }

      if (sessionLocation != null
          && sessionLocation.hasTag(EmrApiConstants.LOCATION_TAG_SUPPORTS_LOGIN)) {
        // Set a cookie, so next time someone logs in on this machine, we can default to that same
        // location
        Cookie cookie =
            new Cookie(
                CflWebConstants.COOKIE_NAME_LAST_SESSION_LOCATION, sessionLocationId.toString());
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        pageRequest.getResponse().addCookie(cookie);
        if (Context.isAuthenticated()) {
          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("User has successfully authenticated");
          }
          CurrentUsers.addUser(
              pageRequest.getRequest().getSession(), Context.getAuthenticatedUser());

          sessionContext.setSessionLocation(sessionLocation);
          // we set the username value to check it new or old user is trying to log in
          cookie =
              new Cookie(
                  CflWebConstants.COOKIE_NAME_LAST_USER, String.valueOf(username.hashCode()));
          cookie.setSecure(true);
          cookie.setHttpOnly(true);
          pageRequest.getResponse().addCookie(cookie);

          // set the locale based on the user's default locale
          Locale userLocale = GeneralUtils.getDefaultLocale(Context.getAuthenticatedUser());
          if (userLocale != null) {
            Context.getUserContext().setLocale(userLocale);
            pageRequest.getResponse().setLocale(userLocale);
            new CookieLocaleResolver().setDefaultLocale(userLocale);
          }

          if (StringUtils.isNotBlank(redirectUrl)) {
            // don't redirect back to the login page on success nor an external url
            if (isUrlWithinOpenmrs(pageRequest, redirectUrl)) {
              if (!redirectUrl.contains("login.") && isSameUser(pageRequest, username)) {
                if (LOGGER.isDebugEnabled()) {
                  LOGGER.debug("Redirecting user to " + redirectUrl.replaceAll("[\r\n]", ""));
                }
                if (StringUtils.isNotBlank(redirectUrl) && isRedirectURLTrusted(redirectUrl)) {
                  return REDIRECT_PREFIX + redirectUrl;
                }
              } else {
                if (LOGGER.isDebugEnabled()) {
                  LOGGER.debug("Redirect contains 'login.', redirecting to home page");
                }
              }
            }
          }
          String url = ui.pageLink(CflWebConstants.MODULE_ID, "home");
          if (StringUtils.isNotBlank(url) && isRedirectURLTrusted(url)) {
            return REDIRECT_PREFIX + url;
          }
        }
      } else if (sessionLocation == null) {
        pageRequest
            .getSession()
            .setAttribute(
                CflWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                ui.message("cfl.login.error.locationRequired"));
      } else {
        // the UI shouldn't allow this, but protect against it just in case
        pageRequest
            .getSession()
            .setAttribute(
                CflWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                ui.message("cfl.login.error.invalidLocation", sessionLocation.getName()));
      }
    } catch (ContextAuthenticationException ex) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Failed to authenticate user");
      }
      pageRequest
          .getSession()
          .setAttribute(
              CflWebConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
              ui.message(getAuthenticationErrorMessage(username)));
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Sending user back to login page");
    }

    pageRequest
        .getSession()
        .setAttribute(CflWebConstants.SESSION_ATTRIBUTE_REDIRECT_URL, redirectUrl);
    // Since the user is already authenticated without location, need to logout before redirecting
    Context.logout();
    String url = ui.pageLink(CflWebConstants.MODULE_ID, LOGIN_PAGE_NAME);
    if (StringUtils.isNotBlank(url) && isRedirectURLTrusted(url)) {
      return REDIRECT_PREFIX + url;
    }
    return "";
  }

  /**
   * Checks if the request should be treated as a submission of a session location selection.
   *
   * @param isUserLocPropEnabled boolean value to specify if location usr property is set or not
   * @return
   */
  private boolean isSelectLocationRequest(boolean isUserLocPropEnabled) {
    return isUserLocPropEnabled
        && Context.isAuthenticated()
        && Context.getUserContext().getLocationId() == null;
  }

  private boolean isSameUser(PageRequest pageRequest, String username) {
    String cookieValue = pageRequest.getCookieValue(CflWebConstants.COOKIE_NAME_LAST_USER);
    int storedUsername = 0;
    if (StringUtils.isNotBlank(cookieValue)) {
      storedUsername = Integer.parseInt(cookieValue);
    }
    return cookieValue == null || storedUsername == username.hashCode();
  }

  private List<Location> getUserLocations(
      AdministrationService adminService, LocationService locationService) {
    String locationUserPropertyName =
        adminService.getGlobalProperty(CflWebConstants.LOCATION_USER_PROPERTY_NAME);
    List<Location> locations = new ArrayList<>();
    String locationUuids = Context.getAuthenticatedUser().getUserProperty(locationUserPropertyName);
    if (StringUtils.isNotBlank(locationUuids)) {
      for (String uuid : StringUtils.split(locationUuids, ",")) {
        String trimmedUuid = uuid.trim();
        Location loc = locationService.getLocationByUuid(trimmedUuid);
        if (loc == null) {
          throw new APIException("No location with uuid: " + trimmedUuid);
        }
        locations.add(locationService.getLocationByUuid(trimmedUuid));
      }
    }

    return locations;
  }

  private String getStringSessionAttribute(String attributeName, HttpServletRequest request) {
    Object attributeValue = request.getSession().getAttribute(attributeName);
    request.getSession().removeAttribute(attributeName);
    return attributeValue != null ? attributeValue.toString() : "";
  }

  private String getRelativeUrl(String url, PageRequest pageRequest) {
    String aUrl = url;
    if (aUrl == null) {
      return "";
    }

    if ((!aUrl.isEmpty() && aUrl.charAt(0) == '/')
        || (!aUrl.startsWith(HTTP_PREFIX) && !aUrl.startsWith(HTTPS_PREFIX))) {
      return aUrl;
    }

    // This is an absolute url, discard the protocal, domain name/host and port section
    if (aUrl.startsWith(HTTP_PREFIX)) {
      aUrl = StringUtils.removeStart(aUrl, HTTP_PREFIX);
    } else if (aUrl.startsWith(HTTPS_PREFIX)) {
      aUrl = StringUtils.removeStart(aUrl, HTTPS_PREFIX);
    }
    int indexOfContextPath = aUrl.indexOf(pageRequest.getRequest().getContextPath());
    if (indexOfContextPath >= 0) {
      aUrl = aUrl.substring(indexOfContextPath);
      LOGGER.debug("Relative redirect:" + aUrl.replaceAll("[\r\n]", ""));

      return aUrl;
    }
    return "";
  }

  public String getAuthenticationErrorMessage(String username) {
    if (isLockoutTimestampSet(username)) {
      return CflWebConstants.MODULE_ID + ".user.lockout.message";
    }
    return CflWebConstants.MODULE_ID + ".error.login.fail";
  }

  public boolean isLockoutTimestampSet(String username) {
    User user = Context.getService(UserNotAuthorizedService.class).getUser(username);
    if (user != null) {
      return StringUtils.isNotBlank(user.getUserProperty("lockoutTimestamp"));
    }
    return false;
  }

  private boolean isRedirectURLTrusted(String url) {
    try {
      URI uri = new URI(url);
      return uri.getPath().startsWith("/openmrs");
    } catch (URISyntaxException ex) {
      LOGGER.error("URL string cannot be parsed as a URI reference");
    }

    return false;
  }
}
