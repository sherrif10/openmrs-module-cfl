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

import org.openmrs.api.FormService;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.module.htmlformentryui.HtmlFormUtil;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;
import org.openmrs.ui.framework.resource.ResourceFactory;

import java.util.Arrays;
import java.util.List;

/** Loads all HTML forms from `omod/src/main/resources/htmlforms`. */
public class HtmlFormsMetadata extends VersionedMetadataBundle {

  @Override
  public int getVersion() {
    return 7;
  }

  @Override
  protected void installEveryTime() {
    // nothing to do
  }

  @Override
  protected void installNewVersion() throws Exception {
    final ResourceFactory resourceFactory = ResourceFactory.getInstance();
    final FormService formService = Context.getFormService();
    final HtmlFormEntryService htmlFormEntryService =
        Context.getService(HtmlFormEntryService.class);

    final List<String> htmlforms =
        Arrays.asList(
            "cfl:htmlforms/discontinue-program.xml",
            "cfl:htmlforms/program-form.xml");

    for (String htmlform : htmlforms) {
      HtmlFormUtil.getHtmlFormFromUiResource(
          resourceFactory, formService, htmlFormEntryService, htmlform);
    }
  }
}
