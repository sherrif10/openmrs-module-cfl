<% /* This Source Code Form is subject to the terms of the Mozilla Public License, */ %>
<% /* v. 2.0. If a copy of the MPL was not distributed with this file, You can */ %>
<% /* obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under */ %>
<% /* the terms of the Healthcare Disclaimer located at http://openmrs.org/license. */ %>
<% /* <p> */ %>
<% /* Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS */ %>
<% /* graphic logo is a trademark of OpenMRS Inc. */ %>

<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("cfl.home.title") ])
    ui.includeCss("cfl", "home.css")

    def htmlSafeId = { extension ->
        "${ extension.id.replace(".", "-") }-${ extension.id.replace(".", "-") }-extension"
    }
%>

<div class="row">
    <div class="col-12 col-sm-12 col-md-12 col-lg-12 homeNotification">
        ${ ui.includeFragment("coreapps", "administrativenotification/notifications") }
    </div>
</div>
<div class="row">
    <div class="col-12 col-sm-12 col-md-12 col-lg-12">
        <% if (authenticatedUser) { %>
            <h4>
                ${ ui.encodeHtmlContent(ui.message("cfl.home.currentUser", ui.format(authenticatedUser), ui.format(sessionContext.sessionLocation))) }
            </h4>
        <% } else { %>
            <h4>
                <a href="login.htm">${ ui.message("cfl.home.logIn") }</a>
            </h4>
        <% } %>
    </div>
</div>
<div class="row">
    <div  class="col-12 col-sm-12 col-md-12 col-lg-12 homeList" id="apps">
            <% extensions.each { ext -> %>
                <a id="${ htmlSafeId(ext) }" href="/${ contextPath }/${ ext.url }" class="btn btn-default btn-lg button app big align-self-center" type="button">
                    <% if (ext.icon) { %>
                    <i class="${ ext.icon }"></i>
                    <% } %>
                    ${ ui.message(ext.label) }
                </a>
            <% } %>
    </div>
</div>
