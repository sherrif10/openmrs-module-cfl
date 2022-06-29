/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.activator.impl;

import org.apache.commons.logging.Log;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.cfl.api.activator.ModuleActivatorStep;

import static org.openmrs.module.cfl.api.activator.impl.ModuleActivatorStepOrderEnum.FIX_RISK_FACTOR_FOR_HIV_CONCEPTS_ACTIVATOR_STEP;

/**
 * The bean defined in moduleApplicationContext.xml because OpenMRS performance issues with annotated beans.
 */
public class FixRiskFactorForHIVConceptsActivatorStep implements ModuleActivatorStep {
    private Log log;

    @Override
    public int getOrder() {
        return FIX_RISK_FACTOR_FOR_HIV_CONCEPTS_ACTIVATOR_STEP.ordinal();
    }

    @Override
    public void startup(Log log) {
        this.log = log;

        final String riskFactorConceptUuid = "f7c6f4f7-89d2-48f9-8844-c1a6564231c0";
        fixMissingConceptAnswer("Risk factor for HIV", riskFactorConceptUuid, "1067AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        final String highRiskGroupsConceptUuid = "9994a4aa-981b-4605-8f4f-23e02e64106e";
        fixMissingConceptAnswer("High Risk Groups", highRiskGroupsConceptUuid, "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    private void fixMissingConceptAnswer(final String conceptName, final String conceptUuid,
                                         final String missingAnswerUuid) {
        final ConceptService conceptService = Context.getConceptService();
        final Concept concept = conceptService.getConceptByUuid(conceptUuid);

        if (concept == null) {
            log.error(new StringBuilder("Concept '")
                    .append(conceptName)
                    .append("' for which to fix an Answer was not found by UUID: ")
                    .append(conceptUuid)
                    .append(". System may be unstable!")
                    .toString());
        } else {
            // We expect only one Answer to be missing
            for (final ConceptAnswer conceptAnswer : concept.getAnswers()) {
                if (conceptAnswer.getAnswerConcept() == null) {
                    conceptAnswer.setAnswerConcept(conceptService.getConceptByUuid(missingAnswerUuid));
                    return;
                }
            }
        }
    }
}
