<% /* This Source Code Form is subject to the terms of the Mozilla Public License, */ %>
<% /* v. 2.0. If a copy of the MPL was not distributed with this file, You can */ %>
<% /* obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under */ %>
<% /* the terms of the Healthcare Disclaimer located at http://openmrs.org/license. */ %>
<% /* <p> */ %>
<% /* Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS */ %>
<% /* graphic logo is a trademark of OpenMRS Inc. */ %>

<%
    config.require("appId");
%>

<div id="cfldistribution-delete-userApp-dialog-${config.appId}" class="dialog" style="display: none">
    <div class="dialog-header">
        <h3>${ ui.message("cfl.app.deleteUserAppDefinition") }</h3>
    </div>
    <div class="dialog-content">
        <ul>
            <li class="info">
                ${ ui.message("cfl.app.deleteUserApp.message", "<b>"+config.appId+"</b>") }
            </li>
        </ul>
        <form method="POST" action="manageApps.page">
            <input type="hidden" name="id" value="${config.appId}"/>
            <input type="hidden" name="action" value="delete"/>
            <button class="confirm right" type="submit">${ ui.message("general.yes") }</button>
            <button class="cancel">${ ui.message("general.no") }</button>
        </form>
    </div>
</div>
