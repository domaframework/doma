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
package org.seasar.doma.jdbc.criteria.statement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;

public class EmptySql implements Sql<InParameter<?>> {

  private static final String MESSAGE = "This SQL is empty because target entities are empty.";
  private final SqlKind sqlKind;

  public EmptySql(SqlKind sqlKind) {
    this.sqlKind = Objects.requireNonNull(sqlKind);
  }

  @Override
  public SqlKind getKind() {
    return sqlKind;
  }

  @Override
  public String getRawSql() {
    return MESSAGE;
  }

  @Override
  public String getFormattedSql() {
    return MESSAGE;
  }

  @Override
  public String getSqlFilePath() {
    return null;
  }

  @Override
  public List<InParameter<?>> getParameters() {
    return Collections.emptyList();
  }

  @Override
  public SqlLogType getSqlLogType() {
    return SqlLogType.FORMATTED;
  }
}
