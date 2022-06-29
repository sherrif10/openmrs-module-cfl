/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cfl.api.metadata.views;

import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.cfl.api.util.MetadataSQLScriptRunner;
import org.openmrs.module.metadatadeploy.bundle.VersionedMetadataBundle;

import java.io.IOException;

public class DbViewsMetadata extends VersionedMetadataBundle {

  private static final String DROP_VIEW_IF_EXISTS_STATEMENT = "DROP VIEW IF EXISTS ";

  private static final String DIGITS_VIEW_PATH = "mysql/views/digits/";

  private static final String DATES_LIST_TABLE_NAME = "DATES_LIST_10K_DAYS_TABLE";

  private final MetadataSQLScriptRunner metadataSQLScriptRunner;

  public DbViewsMetadata(DbSessionFactory dbSessionFactory) {
    this.metadataSQLScriptRunner = new MetadataSQLScriptRunner(dbSessionFactory);
  }

  @Override
  public int getVersion() {
    return 2;
  }

  @Override
  protected void installEveryTime() throws Exception {
    // nothing to do
  }

  @Override
  protected void installNewVersion() throws Exception {
    createDbViews();
  }

  private void createDbViews() throws IOException {
    createDigitsView();
    createDatesListTable();
  }

  private void createDigitsView() throws IOException {
    metadataSQLScriptRunner.executeQuery(DROP_VIEW_IF_EXISTS_STATEMENT + "DIGITS_0");
    metadataSQLScriptRunner.executeQueryFromResource(DIGITS_VIEW_PATH + "Digits_0.sql");
    metadataSQLScriptRunner.executeQuery(DROP_VIEW_IF_EXISTS_STATEMENT + "DIGITS_1");
    metadataSQLScriptRunner.executeQueryFromResource(DIGITS_VIEW_PATH + "Digits_1.sql");
    metadataSQLScriptRunner.executeQuery(DROP_VIEW_IF_EXISTS_STATEMENT + "DIGITS_2");
    metadataSQLScriptRunner.executeQueryFromResource(DIGITS_VIEW_PATH + "Digits_2.sql");
    metadataSQLScriptRunner.executeQuery(DROP_VIEW_IF_EXISTS_STATEMENT + "DIGITS_3");
    metadataSQLScriptRunner.executeQueryFromResource(DIGITS_VIEW_PATH + "Digits_3.sql");
    metadataSQLScriptRunner.executeQuery(DROP_VIEW_IF_EXISTS_STATEMENT + "DIGITS_4");
    metadataSQLScriptRunner.executeQueryFromResource(DIGITS_VIEW_PATH + "Digits_4.sql");
    metadataSQLScriptRunner.executeQuery(DROP_VIEW_IF_EXISTS_STATEMENT + "NUMBERS_100");
    metadataSQLScriptRunner.executeQueryFromResource(DIGITS_VIEW_PATH + "Numbers_100.sql");
  }

  private void createDatesListTable() throws IOException {
    metadataSQLScriptRunner.executeQuery("DROP TABLE IF EXISTS " + DATES_LIST_TABLE_NAME + ";");
    metadataSQLScriptRunner.executeQuery(
        "CREATE TABLE " + DATES_LIST_TABLE_NAME + " (selected_date date DEFAULT NULL);");
    metadataSQLScriptRunner.executeQuery(
        "CREATE INDEX selected_date_idx ON " + DATES_LIST_TABLE_NAME + " (selected_date);");
    metadataSQLScriptRunner.executeQueryFromResource("mysql/views/dates/DatesList_10kDays.sql");
  }
}
