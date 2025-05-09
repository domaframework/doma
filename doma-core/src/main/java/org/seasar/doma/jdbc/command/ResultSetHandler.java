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
package org.seasar.doma.jdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * An interface for handling JDBC ResultSet objects and converting them into application-specific
 * result types.
 *
 * <p>This interface is responsible for processing database query results and transforming the raw
 * ResultSet data into the desired Java objects. It's a key component in the data access layer that
 * bridges the gap between database records and domain objects.
 *
 * @param <RESULT> the type of result that this handler produces
 */
public interface ResultSetHandler<RESULT> {

  /**
   * Processes a ResultSet and converts it into the specified result type.
   *
   * <p>This method is called after executing a database query to process the returned ResultSet. It
   * should iterate through the ResultSet and transform the data according to the implementation's
   * logic. The consumer parameter can be used to track the current row position during processing.
   *
   * @param resultSet the JDBC ResultSet to process
   * @param query the query that produced this ResultSet
   * @param consumer a consumer that receives information about the current row being processed
   * @return a supplier that provides the result of processing the ResultSet
   * @throws SQLException if a database access error occurs during ResultSet processing
   */
  Supplier<RESULT> handle(
      ResultSet resultSet, SelectQuery query, ResultSetRowIndexConsumer consumer)
      throws SQLException;
}
