package org.openmrs.module.cfl.api.service;

import org.openmrs.User;

/**
 * This service is used to get the unauthorised user by username. For other functionalities related
 * to user use UserService from Openmrs
 */
public interface UserNotAuthorizedService {

  /**
   * Retrieves unauthorised user by username
   *
   * @param username
   * @return user
   */
  User getUser(String username);
}
