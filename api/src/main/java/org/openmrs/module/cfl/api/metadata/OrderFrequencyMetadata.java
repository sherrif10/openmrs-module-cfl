/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.metadata;

import org.openmrs.Concept;
import org.openmrs.OrderFrequency;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

/** The metadata package which adds entries to OrderFrequency for CIEL Frequency Concepts. */
public class OrderFrequencyMetadata extends VersionedMetadataBundle {

  @Override
  public int getVersion() {
    return 2;
  }

  @Override
  protected void installEveryTime() {
    // needs to be overwritten, do nothing
  }

  @Override
  public void installNewVersion() {
    install(orderFrequency(CIELConceptsConstants.ONCE, OrderFrequenciesConstants.ONCE, 1.0));
    install(orderFrequency(CIELConceptsConstants.EVERY_30_MINS, OrderFrequenciesConstants.EVERY_30_MINS, 48.0));
    install(orderFrequency(CIELConceptsConstants.EVERY_HOUR, OrderFrequenciesConstants.EVERY_HOUR, 24.0));
    install(orderFrequency(CIELConceptsConstants.EVERY_TWO_HOURS, OrderFrequenciesConstants.EVERY_TWO_HOURS, 12.0));
    install(
        orderFrequency(CIELConceptsConstants.EVERY_THREE_HOURS, OrderFrequenciesConstants.EVERY_THREE_HOURS, 8.0));
    install(orderFrequency(CIELConceptsConstants.EVERY_FOUR_HOURS, OrderFrequenciesConstants.EVERY_FOUR_HOURS, 6.0));
    install(orderFrequency(CIELConceptsConstants.EVERY_FIVE_HOURS, OrderFrequenciesConstants.EVERY_FIVE_HOURS, 4.8));
    install(orderFrequency(CIELConceptsConstants.EVERY_SIX_HOURS, OrderFrequenciesConstants.EVERY_SIX_HOURS, 4.0));
    install(
        orderFrequency(CIELConceptsConstants.EVERY_EIGHT_HOURS, OrderFrequenciesConstants.EVERY_EIGHT_HOURS, 3.0));
    install(
        orderFrequency(CIELConceptsConstants.EVERY_TWELVE_HOURS, OrderFrequenciesConstants.EVERY_TWELVE_HOURS, 2.0));
    install(orderFrequency(CIELConceptsConstants.TWICE_DAILY, OrderFrequenciesConstants.TWICE_DAILY, 2.0));
    install(
        orderFrequency(
            CIELConceptsConstants.TWICE_DAILY_BEFORE_MEALS, OrderFrequenciesConstants.TWICE_DAILY_BEFORE_MEALS, 2.0));
    install(
        orderFrequency(
            CIELConceptsConstants.TWICE_DAILY_AFTER_MEALS, OrderFrequenciesConstants.TWICE_DAILY_AFTER_MEALS, 2.0));
    install(
        orderFrequency(
            CIELConceptsConstants.TWICE_DAILY_WITH_MEALS, OrderFrequenciesConstants.TWICE_DAILY_WITH_MEALS, 2.0));
    install(orderFrequency(CIELConceptsConstants.EVERY_24_HOURS, OrderFrequenciesConstants.EVERY_24_HOURS, 1.0));
    install(orderFrequency(CIELConceptsConstants.ONCE_DAILY, OrderFrequenciesConstants.ONCE_DAILY, 1.0));
    install(
        orderFrequency(CIELConceptsConstants.ONCE_DAILY_BEDTIME, OrderFrequenciesConstants.ONCE_DAILY_BEDTIME, 1.0));
    install(
        orderFrequency(CIELConceptsConstants.ONCE_DAILY_EVENING, OrderFrequenciesConstants.ONCE_DAILY_EVENING, 1.0));
    install(
        orderFrequency(CIELConceptsConstants.ONCE_DAILY_MORNING, OrderFrequenciesConstants.ONCE_DAILY_MORNING, 1.0));
    install(orderFrequency(CIELConceptsConstants.THRICE_DAILY, OrderFrequenciesConstants.THRICE_DAILY, 3.0));
    install(
        orderFrequency(
            CIELConceptsConstants.THRICE_DAILY_AFTER_MEALS, OrderFrequenciesConstants.THRICE_DAILY_AFTER_MEALS, 3.0));
    install(
        orderFrequency(
            CIELConceptsConstants.THRICE_DAILY_BEFORE_MEALS,
            OrderFrequenciesConstants.THRICE_DAILY_BEFORE_MEALS,
            3.0));
    install(
        orderFrequency(
            CIELConceptsConstants.THRICE_DAILY_WITH_MEALS, OrderFrequenciesConstants.THRICE_DAILY_WITH_MEALS, 3.0));
    install(
        orderFrequency(
            CIELConceptsConstants.FOUR_TIMES_DAILY_WITH_MEALS,
            OrderFrequenciesConstants.FOUR_TIMES_DAILY_WITH_MEALS,
            4.0));
    install(
        orderFrequency(
            CIELConceptsConstants.FOUR_TIMES_AFTER_WITH_MEALS_BEDTIME,
            OrderFrequenciesConstants.FOUR_TIMES_AFTER_WITH_MEALS_BEDTIME,
            4.0));
    install(
        orderFrequency(
            CIELConceptsConstants.FOUR_TIMES_BEFORE_WITH_MEALS_BEDTIME,
            OrderFrequenciesConstants.FOUR_TIMES_BEFORE_WITH_MEALS_BEDTIME,
            4.0));
    install(orderFrequency(CIELConceptsConstants.EVERY_48_HOURS, OrderFrequenciesConstants.EVERY_48_HOURS, 0.5));
    install(
        orderFrequency(
            CIELConceptsConstants.EVERY_36_HOURS, OrderFrequenciesConstants.EVERY_36_HOURS, (1 / 36.0) * 24.0));
    install(
        orderFrequency(
            CIELConceptsConstants.EVERY_72_HOURS, OrderFrequenciesConstants.EVERY_72_HOURS, (1 / 72.0) * 24.0));
    install(
        orderFrequency(
            CIELConceptsConstants.MONDAY_WEDNESDAY_FRIDAY,
            OrderFrequenciesConstants.MONDAY_WEDNESDAY_FRIDAY,
            (3 / 168.0) * 24.0));
  }

  private static OrderFrequency orderFrequency(
      String conceptUuid, String uuid, Double frequencyPerDay) {
    final Concept concept = Context.getConceptService().getConceptByUuid(conceptUuid);
    final OrderFrequency obj = new OrderFrequency();

    obj.setConcept(concept);
    obj.setUuid(uuid);
    obj.setFrequencyPerDay(frequencyPerDay);

    return obj;
  }

  private static class OrderFrequenciesConstants {
    static final String ONCE = "162135OFAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY = "160858OFAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY_BEFORE_MEALS = "160859OFAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY_AFTER_MEALS = "160860OFAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY_WITH_MEALS = "160861OFAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY = "160862OFAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY_BEDTIME = "160863OFAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY_EVENING = "160864OFAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY_MORNING = "160865OFAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY = "160866OFAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY_AFTER_MEALS = "160867OFAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY_BEFORE_MEALS = "160868OFAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY_WITH_MEALS = "160869OFAAAAAAAAAAAAAAA";
    static final String FOUR_TIMES_DAILY_WITH_MEALS = "160870OFAAAAAAAAAAAAAAA";
    static final String FOUR_TIMES_AFTER_WITH_MEALS_BEDTIME = "160871OFAAAAAAAAAAAAAAA";
    static final String FOUR_TIMES_BEFORE_WITH_MEALS_BEDTIME = "160872OFAAAAAAAAAAAAAAA";
    static final String EVERY_30_MINS = "162243OFAAAAAAAAAAAAAAA";
    static final String EVERY_HOUR = "162244OFAAAAAAAAAAAAAAA";
    static final String EVERY_TWO_HOURS = "162245OFAAAAAAAAAAAAAAA";
    static final String EVERY_THREE_HOURS = "162246OFAAAAAAAAAAAAAAA";
    static final String EVERY_FOUR_HOURS = "162247OFAAAAAAAAAAAAAAA";
    static final String EVERY_FIVE_HOURS = "162248OFAAAAAAAAAAAAAAA";
    static final String EVERY_SIX_HOURS = "162249OFAAAAAAAAAAAAAAA";
    static final String EVERY_EIGHT_HOURS = "162250OFAAAAAAAAAAAAAAA";
    static final String EVERY_TWELVE_HOURS = "162251OFAAAAAAAAAAAAAAA";
    static final String EVERY_24_HOURS = "162252OFAAAAAAAAAAAAAAA";
    static final String EVERY_48_HOURS = "162253OFAAAAAAAAAAAAAAA";
    static final String EVERY_36_HOURS = "162254OFAAAAAAAAAAAAAAA";
    static final String EVERY_72_HOURS = "162255OFAAAAAAAAAAAAAAA";
    static final String MONDAY_WEDNESDAY_FRIDAY = "162256OFAAAAAAAAAAAAAAA";
  }

  private static class CIELConceptsConstants {
    static final String ONCE = "162135AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY = "160858AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY_BEFORE_MEALS = "160859AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY_AFTER_MEALS = "160860AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String TWICE_DAILY_WITH_MEALS = "160861AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY = "160862AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY_BEDTIME = "160863AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY_EVENING = "160864AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String ONCE_DAILY_MORNING = "160865AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY = "160866AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY_AFTER_MEALS = "160867AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY_BEFORE_MEALS = "160868AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String THRICE_DAILY_WITH_MEALS = "160869AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String FOUR_TIMES_DAILY_WITH_MEALS = "160870AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String FOUR_TIMES_AFTER_WITH_MEALS_BEDTIME = "160871AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String FOUR_TIMES_BEFORE_WITH_MEALS_BEDTIME = "160872AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_30_MINS = "162243AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_HOUR = "162244AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_TWO_HOURS = "162245AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_THREE_HOURS = "162246AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_FOUR_HOURS = "162247AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_FIVE_HOURS = "162248AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_SIX_HOURS = "162249AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_EIGHT_HOURS = "162250AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_TWELVE_HOURS = "162251AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_24_HOURS = "162252AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_48_HOURS = "162253AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_36_HOURS = "162254AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String EVERY_72_HOURS = "162255AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    static final String MONDAY_WEDNESDAY_FRIDAY = "162256AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
  }
}
