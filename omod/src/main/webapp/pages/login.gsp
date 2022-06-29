<% /* This Source Code Form is subject to the terms of the Mozilla Public License, */ %>
<% /* v. 2.0. If a copy of the MPL was not distributed with this file, You can */ %>
<% /* obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under */ %>
<% /* the terms of the Healthcare Disclaimer located at http://openmrs.org/license. */ %>
<% /* <p> */ %>
<% /* Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS */ %>
<% /* graphic logo is a trademark of OpenMRS Inc. */ %>

<%
    ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeJavascript("appui", "jquery.min.js")
    ui.includeJavascript("appui", "popper.min.js")
    ui.includeJavascript("appui", "bootstrap.min.js")
%>

<!DOCTYPE html>
<html>
<head>
    <title>${ui.message("cfl.login.title")}</title>
    <link rel="shortcut icon" type="image/ico" href="/${ui.contextPath()}/images/openmrs-favicon.ico"/>
    <link rel="icon" type="image/png\" href="/${ui.contextPath()}/images/openmrs-favicon.png"/>
    <!-- Latest compiled and minified CSS -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <% ui.includeCss("appui", "bootstrap.min.css") %>
    <% ui.includeCss("cfl", "login.css") %>
    <% ui.includeCss("cfl", "cflLogin.css") %>
    ${ui.resourceLinks()}
</head>

<body>
<script type="text/javascript">
    var OPENMRS_CONTEXT_PATH = '${ ui.contextPath() }';
</script>

<% if (showSessionLocations) { %>
<script type="text/javascript">
    jQuery(function () {
        updateSelectedOption = function () {
            jQuery('#sessionLocation li').removeClass('selected');

            var sessionLocationVal = jQuery('#sessionLocationInput').val();
            if (sessionLocationVal != null && sessionLocationVal != "" && sessionLocationVal != 0) {
                jQuery('#sessionLocation li[value|=' + sessionLocationVal + ']').addClass('selected');
            }
        };

        updateSelectedOption();

        jQuery('#sessionLocation li').click(function () {
            jQuery('#sessionLocationInput').val(jQuery(this).attr("value"));
            updateSelectedOption();
        });
        jQuery('#sessionLocation li').focus(function () {
            jQuery('#sessionLocationInput').val(jQuery(this).attr("value"));
            updateSelectedOption();
        });

        // If <Enter> Key is pressed, submit the form
        jQuery('#sessionLocation').keyup(function (e) {
            var key = e.which || e.keyCode;
            if (key === 13) {
                jQuery('#login-form').submit();
            }
        });
        var listItem = Array.from(jQuery('#sessionLocation li'));
        for (var i in listItem) {
            listItem[i].setAttribute('data-key', i);
            listItem[i].addEventListener('keyup', function (event) {
                var keyCode = event.which || event.keyCode;
                switch (keyCode) {
                    case 37: // move left
                        jQuery(this).prev('#sessionLocation li').focus();
                        break;
                    case 39: // move right
                        jQuery(this).next('#sessionLocation li').focus();
                        break;
                    case 38: // move up
                        jQuery('#sessionLocation li[data-key=' + (Number(jQuery(document.activeElement).attr('data-key')) - 3) + ']').focus();
                        break;
                    case 40: //	move down
                        jQuery('#sessionLocation li[data-key=' + (Number(jQuery(document.activeElement).attr('data-key')) + 3) + ']').focus();
                        break;
                    default:
                        break;
                }
            });
        }

        jQuery('#loginButton').click(function (e) {
            var sessionLocationVal = jQuery('#sessionLocationInput').val();

            if (!sessionLocationVal) {
                jQuery('#sessionLocationError').show();
                e.preventDefault();
            }
        });
    });
</script>
<% } %>

<script type="text/javascript">
    jQuery(function () {
        var cannotLoginController = emr.setupConfirmationDialog({
            selector: '#cannotLoginPopup',
            actions: {
                confirm: function () {
                    cannotLoginController.close();
                }
            }
        });

        jQuery('#username').focus();
        jQuery('a#cantLogin').click(function () {
            cannotLoginController.show();
        });

        pageReady = true;
    });
</script>

<div id="content" class="container-fluid">
    <div class="row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            ${ui.includeFragment("cfl", "infoAndErrorMessages")}
        </div>
    </div>

    <div class="row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <header class="header">
                <div class="logo">
                <% if (!isStagingEnvironment) { %>
                    <a href="${ui.pageLink("cfl", "home")}">
                        <div>
                            <img src="${ui.resourceLink("cfl", "images/logo/connect_for_life.png")}"/>
                        </div>
                    </a>
                <% } %>
                </div>
            </header>
        </div>
    </div>

    <div class="row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <div>
                <div id="body-wrapper" class="custom-body-wrapper">
                    <div id="content">
                        <form id="login-form" method="post" autocomplete="off">
                            <fieldset class="p-2 field-set-wrapper">
                                <div id="title">
                                <% if (isStagingEnvironment) { %>
                                    <span>${ui.message("cfl.staging.loginPage.title")}</span>
                                <% } else { %>
                                    <span>${ui.message("cfl.loginPage.title")}</span>
                                <% } %>
                                </div>

                                <div id="log-in-wrapper">
                                    <span class="log-in-header-text">${ui.message("cfl.loginPage.logIn.header")}</span><br>
                                </div>

                                <div id="main-content">
                                    <% if (!selectLocation) { %>
                                    <p class="left username-block">
                                        <label for="username" class="credential-label">
                                            ${ui.message("cfl.login.username")}:
                                        </label>
                                        <input class="form-control form-control-sm form-control-lg form-control-md credential-input"
                                               id="username" type="text" name="username"/>
                                    </p>

                                    <p class="left">
                                        <label for="password" class="credential-label">
                                            ${ui.message("cfl.login.password")}:
                                        </label>
                                        <input class="form-control form-control-sm form-control-lg form-control-md credential-input"
                                               id="password" type="password" name="password"/>
                                    </p>
                                    <% } %>

                                    <% if (showSessionLocations) { %>
                                    <p class="clear">
                                        <label for="sessionLocation">
                                            <% if (!selectLocation) { %>${
                                                    ui.message("cfl.login.sessionLocation")}: <% } %><span
                                                class="location-error" id="sessionLocationError" style="display: none">${
                                                    ui.message("cfl.login.error.locationRequired")}</span>
                                        </label>
                                    <ul id="sessionLocation" class="select">
                                        <% locations.sort { ui.format(it) }.each { %>
                                        <li id="${ui.encodeHtml(it.name)}" tabindex="0"
                                            value="${it.id}">${ui.encodeHtmlContent(ui.format(it))}</li>
                                        <% } %>
                                    </ul>
                                </p>

                                    <input type="hidden" id="sessionLocationInput" name="sessionLocation"
                                        <% if (lastSessionLocation != null) { %> value="${lastSessionLocation.id}" <% } %>/>

                                    <p></p>
                                    <% } %>
                                    <p>
                                        <% if (selectLocation) { %>
                                        <input id="cancelButton" class="btn cancel" type="button"
                                               onclick="javascript:window.location = '/${ contextPath }/logout'"
                                               value="${ui.message("general.cancel")}"/>&nbsp;&nbsp;
                                    <% } %>
                                        <input id="loginButton"
                                               class="btn ${ui.message(selectLocation ? "confirm" : "btn-success")} login-input"
                                               type="submit"
                                               value="${ui.message(selectLocation ? "general.done" : "cfl.login.button")}"/>
                                    </p>
                                    <% if (!selectLocation) { %>
                                    <p>
                                        <a id="cantLogin" href="javascript:void(0)">
                                            ${ui.message("cfl.login.cannotLogin")}
                                        </a>
                                    </p>
                                    <% } %>
                                </div>
                            </fieldset>
                            <input type="hidden" name="redirectUrl" value="${ui.encodeHtmlAttribute(redirectUrl)}"/>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12 col-sm-12 col-md-12 col-lg-12">
            <div id="cannotLoginPopup" class="dialog" style="display: none">
                <div class="dialog-header">
                    <i class="icon-info-sign"></i>

                    <h3>${ui.message("cfl.login.cannotLogin")}</h3>
                </div>

                <div class="dialog-content">
                    <p class="dialog-instructions">${ui.message("cfl.login.cannotLoginInstructions")}</p>

                    <button class="confirm">${ui.message("cfl.okay")}</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
