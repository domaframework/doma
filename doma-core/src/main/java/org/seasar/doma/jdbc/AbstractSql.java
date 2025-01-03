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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.seasar.doma.DomaNullPointerException;

/** A skeletal implementation of the {@link Sql} interface. */
public abstract class AbstractSql<P extends SqlParameter> implements Sql<P> {

  protected final SqlKind kind;

  protected final String rawSql;

  protected final String formattedSql;

  protected final String sqlFilePath;

  protected final List<P> parameters;

  protected final SqlLogType sqlLogType;

  protected AbstractSql(
      SqlKind kind,
      CharSequence rawSql,
      CharSequence formattedSql,
      String sqlFilePath,
      List<? extends P> parameters,
      SqlLogType sqlLogType,
      Function<String, String> commenter) {
    if (kind == null) {
      throw new DomaNullPointerException("kind");
    }
    if (rawSql == null) {
      throw new DomaNullPointerException("rawSql");
    }
    if (formattedSql == null) {
      throw new DomaNullPointerException("formattedSql");
    }
    if (parameters == null) {
      throw new DomaNullPointerException("parameters");
    }
    if (sqlLogType == null) {
      throw new DomaNullPointerException("sqlLogType");
    }
    if (commenter == null) {
      throw new DomaNullPointerException("commenter");
    }
    this.kind = kind;
    this.rawSql = commenter.apply(rawSql.toString().trim());
    this.formattedSql = commenter.apply(formattedSql.toString().trim());
    this.sqlFilePath = sqlFilePath;
    this.parameters = Collections.unmodifiableList(parameters);
    this.sqlLogType = sqlLogType;
  }

  @Override
  public SqlKind getKind() {
    return kind;
  }

  @Override
  public String getRawSql() {
    return rawSql;
  }

  @Override
  public String getFormattedSql() {
    return formattedSql;
  }

  @Override
  public String getSqlFilePath() {
    return sqlFilePath;
  }

  @Override
  public List<P> getParameters() {
    return parameters;
  }

  @Override
  public SqlLogType getSqlLogType() {
    return sqlLogType;
  }

  @Override
  public String toString() {
    return rawSql;
  }
}
