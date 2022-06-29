<% /* This Source Code Form is subject to the terms of the Mozilla Public License, */ %>
<% /* v. 2.0. If a copy of the MPL was not distributed with this file, You can */ %>
<% /* obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under */ %>
<% /* the terms of the Healthcare Disclaimer located at http://openmrs.org/license. */ %>
<% /* <p> */ %>
<% /* Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS */ %>
<% /* graphic logo is a trademark of OpenMRS Inc. */ %>

<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("cfl.app.manageApps.title") ])

    ui.includeJavascript("cfl", "manageApps.js");

    ui.includeCss("cfl", "manageApps.css");

    ui.includeJavascript("appui", "jquery-3.4.1.min.js")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}",
          link: "${ui.pageLink("coreapps", "systemadministration/systemAdministration")}"
        },
        { label: "${ ui.message("cfl.app.manageApps.title")}"}
    ];
</script>

<% apps.each { app -> %>
    ${ui.includeFragment("cfl", "deleteUserApp", [appId: app.id])}
<% } %>

<h2>${ ui.message("cfl.app.manageApps.heading")}</h2>

<button class="confirm" onclick="location.href='${ ui.pageLink("cfl", "userApp", [action: "add"]) }'">
    ${ ui.message("cfl.app.addAppDefinition") }
</button>
</br></br>

<table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl">
    <thead>
    <tr>
        <th>${ ui.message("cfl.app.appId.label")}</th>
        <th>${ ui.message("cfl.app.status.label")}</th>
        <th>${ ui.message("cfl.app.type.label")}</th>
        <th>${ ui.message("cfl.app.actions.label")}</th>
    </tr>
    </thead>
    <% apps.each { app -> %>
    <tbody>
    <tr>
        <td>${app.id}</td>
        <td>
            <% if(app.enabled) { %>
            ${ui.message("cfl.app.status.enabled")}
            <% } else { %>
            ${ui.message("cfl.app.status.disabled")}
            <% } %>
        </td>
        <td>
            <% if(app.builtIn) { %>
            ${ui.message("cfl.app.type.builtIn")}
            <% } else { %>
            ${ui.message("cfl.app.type.implementationDefined")}
            <% } %>
        </td>
        <td>
            <form id="form-${app.id}" method="POST">
            <% if(!app.cannotBeStopped) { %>
                <% if(app.enabled) { %>
                    <i class="icon-stop stop-action cfldistribution-action"
                       title="${ ui.message("cfl.app.action.disable") }"></i>
                    <input type="hidden" name="id" value="${app.id}"/>
                    <input type="hidden" name="action" value="disable" />
                <% } else { %>
                    <i class="icon-play play-action cfldistribution-action"
                       title="${ ui.message("cfl.app.action.enable") }"></i>
                    <input type="hidden" name="id" value="${app.id}"/>
                    <input type="hidden" name="action" value="enable" />
                <% } %>
                <% if(!app.builtIn) { %>
                    <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
                       onclick="location.href='${ui.pageLink("cfl", "userApp", [appId: app.id, action: "edit"])}';"></i>
                    <i class="icon-remove delete-action" title="${ ui.message("general.delete") }"
                       onclick="showDeleteUserAppDialog('${app.id}')"></i>
                <% } %>
            <% } %>
            </form>
            <% if(app.cannotBeStopped) { %>
                <i class="icon-lock lock-action cfldistribution-action"></i>
            <% } %>
        </td>
    </tr>
    </tbody>
    <% } %>
</table>
