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

public final class MessageTemplateBuilder {

    private MessageTemplateBuilder() {
    }

    public static Template buildMessageTemplate(String serviceQuery, String serviceQueryType, String calendarServiceQuery,
                                                String name, boolean shouldUseOptimizedQuery, String uuid) {
        Template template = new Template();
        template.setServiceQuery(serviceQuery);
        template.setServiceQueryType(serviceQueryType);
        template.setCalendarServiceQuery(calendarServiceQuery);
        template.setName(name);
        template.setShouldUseOptimizedQuery(shouldUseOptimizedQuery);
        template.setUuid(uuid);
        return template;
    }
}
