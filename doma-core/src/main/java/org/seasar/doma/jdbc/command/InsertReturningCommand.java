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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.seasar.doma.jdbc.query.InsertQuery;
import org.seasar.doma.jdbc.statistic.StatisticManager;

public class InsertReturningCommand<RESULT> extends ModifyReturningCommand<InsertQuery, RESULT> {

  public InsertReturningCommand(InsertQuery query, ResultSetHandler<RESULT> resultSetHandler) {
    super(query, resultSetHandler);
  }

  @Override
  protected RESULT executeInternal(PreparedStatement preparedStatement) throws SQLException {
    StatisticManager statisticManager = query.getConfig().getStatisticManager();
    return statisticManager.executeSql(query.getSql(), () -> executeQuery(preparedStatement));
  }
}
