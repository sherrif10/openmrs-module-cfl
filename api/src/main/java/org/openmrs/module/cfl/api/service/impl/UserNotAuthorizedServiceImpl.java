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
