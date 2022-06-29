/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

var jq = jQuery;
var timeout;
var action;

jq(function(){
    jq('.required').on("keyup",function() {
        if(timeout){
            clearTimeout(timeout);
        }
        timeout = setTimeout(function(){
            validate();
        }, 300);
    });

    validate();
});

jq(document).ajaxError(function() {
    jq('#server-error-msg').show();
});

function setAction(a){
    action = a;
}

function requireValues(){
    var isAppIdValid = action == 'edit' || jq.trim(jq('#appId-field').val()) != '';
    if(isAppIdValid && jq.trim(jq('#json-field').val()) != ''){
        return true;
    }
    return false;
}

function toggleFields(isJsonValid){
    if(isJsonValid && requireValues()){
        jq('#save-button').removeAttr('disabled');
        jq('#errorMsg').hide();
    }else if(isJsonValid) {
        jq('#save-button').attr('disabled','disabled');
        jq('#errorMsg').hide();
    }else if(!isJsonValid) {
        jq('#save-button').attr('disabled','disabled');
        jq('#errorMsg').show();
    }
}

function validate(){
    var json = jq('#json-field').val();
    if(jq.trim(json) == ''){
        toggleFields(true);
        return;
    }

    jq.post("verifyJson.htm",
        {"json": json},
        function(data){
            jq('#server-error-msg').hide();
            toggleFields(data.isValid);
        },
        "json"
    );
}
