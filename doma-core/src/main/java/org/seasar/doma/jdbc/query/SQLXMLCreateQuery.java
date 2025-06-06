/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;

/**
 * A query that creates a SQLXML object in the database.
 *
 * <p>This class extends {@link AbstractCreateQuery} to provide functionality for creating SQLXML
 * objects. It uses the JDBC connection's createSQLXML method to create a new empty SQLXML instance
 * that can store XML data.
 */
public class SQLXMLCreateQuery extends AbstractCreateQuery<SQLXML> {

  /** {@inheritDoc} */
  @Override
  public SQLXML create(Connection connection) throws SQLException {
    return connection.createSQLXML();
  }
}
