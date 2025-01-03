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
package org.seasar.doma.jdbc;

import java.sql.CallableStatement;
import java.util.List;
import java.util.function.Function;

/**
 * A callable SQL.
 *
 * <p>This is related to {@link CallableStatement}.
 */
public class CallableSql extends AbstractSql<SqlParameter> {

  public CallableSql(
      SqlKind kind,
      CharSequence rawSql,
      CharSequence formattedSql,
      List<? extends SqlParameter> parameters,
      SqlLogType sqlLogType,
      Function<String, String> converter) {
    super(kind, rawSql, formattedSql, null, parameters, sqlLogType, converter);
  }
}
