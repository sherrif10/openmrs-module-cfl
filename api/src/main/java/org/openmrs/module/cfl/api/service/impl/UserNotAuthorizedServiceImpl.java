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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.User;
import org.openmrs.module.cfl.api.service.UserNotAuthorizedService;
import org.springframework.transaction.annotation.Transactional;

public class UserNotAuthorizedServiceImpl implements UserNotAuthorizedService {

  private SessionFactory sessionFactory;

  @Transactional(readOnly = true)
  @Override
  public User getUser(String username) {
    Criteria criteria = getSession().createCriteria(User.class);
    criteria.add(Restrictions.eq("username", username));
    return (User) criteria.uniqueResult();
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  private Session getSession() {
    return sessionFactory.getCurrentSession();
  }
}
