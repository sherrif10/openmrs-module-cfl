/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

const CFL_UI_ROOT = '/openmrs/owa/cfl/';

const head = document.head || document.getElementsByTagName('head')[0];

const style = document.createElement('link');
style.rel = 'stylesheet preload';
style.href = CFL_UI_ROOT + 'overrides.css';
style.as = 'style';

head.appendChild(style);

const SCRIPT_HREF = CFL_UI_ROOT + 'overrides.js';

const scriptPreload = document.createElement('link');
scriptPreload.rel = 'script preload';
scriptPreload.href = SCRIPT_HREF;
scriptPreload.as = 'script';

head.appendChild(scriptPreload);

const script = document.createElement('script');
script.src = SCRIPT_HREF;

head.appendChild(script);
