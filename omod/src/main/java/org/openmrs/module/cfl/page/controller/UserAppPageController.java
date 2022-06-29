/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

/**
 * The contents of this file are subject to the OpenMRS Public License Version 1.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://license.openmrs.org
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF
 * ANY KIND, either express or implied. See the License for the specific language governing rights
 * and limitations under the License.
 *
 * <p>Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.cfl.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.UserApp;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserAppPageController {

  protected final Log log = LogFactory.getLog(getClass());

  private ObjectMapper mapper = new ObjectMapper();

  public void get(
      PageModel model, @RequestParam(value = "appId", required = false) UserApp userApp) {
    model.addAttribute("userApp", userApp == null ? new UserApp() : userApp);
  }

  public String post(
      PageModel model,
      @ModelAttribute(value = "appId") @BindParams UserApp userApp,
      @RequestParam("action") String action,
      @SpringBean("appFrameworkService") AppFrameworkService service,
      HttpSession session,
      UiUtils ui) {

    try {
      AppDescriptor descriptor = mapper.readValue(userApp.getJson(), AppDescriptor.class);
      if (!userApp.getAppId().equals(descriptor.getId())) {
        session.setAttribute(
            UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
            ui.message("cfl.app.errors.IdsShouldMatch"));
      } else if ("add".equals(action) && service.getUserApp(userApp.getAppId()) != null) {
        session.setAttribute(
            UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
            ui.message("cfl.app.errors.duplicateAppId"));
      } else {
        service.saveUserApp(userApp);

        InfoErrorMessageUtil.flashInfoMessage(
            session, ui.message("cfl.app.userApp.save.success", userApp.getAppId()));

        return "redirect:/cfl/manageApps.page";
      }
    } catch (Exception e) {
      session.setAttribute(
          UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
          ui.message("cfl.app.userApp.save.fail", userApp.getAppId()));
    }

    model.addAttribute("userApp", userApp);

    return null;
  }

  @ResponseBody
  @RequestMapping(value = "/cfl/verifyJson", method = RequestMethod.POST)
  public SimpleObject verifyJson(@RequestParam("json") String json) {
    SimpleObject so = new SimpleObject();
    try {
      mapper.readValue(json, AppDescriptor.class);
      so.add("isValid", Boolean.TRUE);
    } catch (Exception e) {
      log.warn("Invalid json:", e);
      so.add("isValid", Boolean.FALSE);
    }

    return so;
  }
}
