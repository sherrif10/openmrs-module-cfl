/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.openmrs.api.context.Context;
import org.openmrs.module.cfl.CfldistributionGlobalParameterConstants;

import java.util.concurrent.TimeUnit;

public class ReCaptchaAttemptService {

  private LoadingCache<String, Integer> attemptsCache;

  public ReCaptchaAttemptService() {
    super();
    attemptsCache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(4, TimeUnit.HOURS)
            .build(
                new CacheLoader<String, Integer>() {
                  @Override
                  public Integer load(String key) {
                    return 0;
                  }
                });
  }

  public void reCaptchaSucceeded(String key) {
    attemptsCache.invalidate(key);
  }

  public void reCaptchaFailed(String key) {
    int attempts = attemptsCache.getUnchecked(key);
    attempts++;
    attemptsCache.put(key, attempts);
  }

  public boolean isBlocked(String key) {
    return attemptsCache.getUnchecked(key) >= getGoogleRecaptchaMaxFailedAttempts();
  }

  private int getGoogleRecaptchaMaxFailedAttempts() {
    return Integer.parseInt(
        Context.getAdministrationService()
            .getGlobalProperty(
                CfldistributionGlobalParameterConstants.GOOGLE_RECAPTCHA_MAX_FAILED_ATTEMPTS_KEY));
  }
}
