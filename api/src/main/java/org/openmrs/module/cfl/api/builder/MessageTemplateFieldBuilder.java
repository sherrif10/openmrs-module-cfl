/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.builder;

import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;

public final class MessageTemplateFieldBuilder {

    private MessageTemplateFieldBuilder() {
    }

    public static TemplateField buildMessageTemplateField(String name, boolean isMandatory, String defaultValue,
                                                          Template template, TemplateFieldType fieldType,
                                                          String possibleValues, String uuid) {
        TemplateField templateField = new TemplateField();
        templateField.setName(name);
        templateField.setMandatory(isMandatory);
        templateField.setDefaultValue(defaultValue);
        templateField.setTemplate(template);
        templateField.setTemplateFieldType(fieldType);
        templateField.setPossibleValues(possibleValues);
        templateField.setUuid(uuid);
        return templateField;
    }
}
