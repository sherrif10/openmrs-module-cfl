<% /* This Source Code Form is subject to the terms of the Mozilla Public License, */ %>
<% /* v. 2.0. If a copy of the MPL was not distributed with this file, You can */ %>
<% /* obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under */ %>
<% /* the terms of the Healthcare Disclaimer located at http://openmrs.org/license. */ %>
<% /* <p> */ %>
<% /* Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS */ %>
<% /* graphic logo is a trademark of OpenMRS Inc. */ %>

<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("cfl.app.userApp."+param.action[0]) ])

    ui.includeJavascript("cfl", "userApp.js");
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}",
            link: "${ui.pageLink("coreapps", "systemadministration/systemAdministration")}"
        },
        { label: "${ ui.message("cfl.app.manageApps.title")}",
            link: "${ui.pageLink("cfl", "manageApps")}"
        },
        { label: "${ ui.message("cfl.app.userApp."+param.action[0])}"}
    ];

    jq(function(){
        setAction('${param.action[0]}');
    });
</script>

<h2>${ ui.message("cfl.app.userApp."+param.action[0])}</h2>

<form class="simple-form-ui" method="POST" action="userApp.page">
    <span id="errorMsg" class="field-error" style="display: none">
        ${ui.message("cfl.app.errors.invalidJson")}
    </span>
    <span id="server-error-msg" class="field-error" style="display: none">
        ${ui.message("cfl.app.errors.serverError")}
    </span>
    <input type="hidden" name="action" value="${param.action[0]}" />
    <p>
        <%if(param.action[0] == 'edit'){%>
        <span class="title">
        ${ui.message("cfl.app.appId.label")}:
        </span>&nbsp;${ui.escapeHtml(userApp.appId)}
        <input class="form-control form-control-sm form-control-lg form-control-md" id="appId-field-hidden" type="hidden" name="appId" value="${userApp.appId ? userApp.appId : ""}" />
        <%} else{%>
        <label for="appId-field">
            <span class="title">
                ${ui.message("cfl.app.appId.label")} (${ ui.message("emr.formValidation.messages.requiredField.label") })
            </span>
        </label>
        <input class="form-control form-control-sm form-control-lg form-control-md required" id="appId-field" type="text" name="appId" value="${userApp.appId ? ui.escapeJs(ui.escapeHtml(userApp.appId)) : ""}" size="80" placeholder="${ ui.message("cfl.app.definition.placeholder") }" />
        <%}%>
    </p>
    <p>
        <label for="json-field">
            <span class="title">
            ${ui.message("cfl.app.definition.label")} (${ ui.message("emr.formValidation.messages.requiredField.label") })
            </span>
        </label>
        <textarea class="form-control form-control-sm form-control-lg form-control-md required" id="json-field" name="json" rows="15" cols="80">${userApp.json ? userApp.json : ""}</textarea>
    </p>

    <input type="button" class="cancel" value="${ ui.message("general.cancel") }" onclick="javascript:window.location='/${ contextPath }/cfl/manageApps.page'" />
    <input type="submit" class="confirm right" id="save-button" value="${ ui.message("general.save") }" disabled="disabled" />
</form>
