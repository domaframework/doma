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

import java.sql.Connection;
import java.sql.SQLException;
import org.seasar.doma.internal.jdbc.util.JdbcUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.query.CreateQuery;
import org.seasar.doma.message.Message;

public class CreateCommand<RESULT> implements Command<RESULT> {

  protected final CreateQuery<RESULT> query;

  public CreateCommand(CreateQuery<RESULT> query) {
    this.query = query;
  }

  @Override
  public CreateQuery<RESULT> getQuery() {
    return query;
  }

  @Override
  public RESULT execute() {
    Connection connection = JdbcUtil.getConnection(query.getConfig().getDataSource());
    try {
      return query.create(connection);
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2008, e, e);
    } finally {
      JdbcUtil.close(connection, query.getConfig().getJdbcLogger());
    }
  }
}
