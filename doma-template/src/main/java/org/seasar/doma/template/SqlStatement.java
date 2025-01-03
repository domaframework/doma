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
package org.seasar.doma.template;

import java.util.List;
import java.util.Objects;

/** Represents a SQL statement. */
public class SqlStatement {
  private final String rawSql;
  private final String formattedSql;
  private final List<SqlArgument> arguments;

  /**
   * @param rawSql the raw SQL string. Must not be null.
   * @param formattedSql the formatted SQL string. Must not be null.
   * @param arguments the SQL arguments. Must not be null.
   */
  public SqlStatement(String rawSql, String formattedSql, List<SqlArgument> arguments) {
    this.rawSql = Objects.requireNonNull(rawSql);
    this.formattedSql = Objects.requireNonNull(formattedSql);
    this.arguments = Objects.requireNonNull(arguments);
  }

  /**
   * Returns the raw SQL string.
   *
   * <p>The bind variables are displayed as {@code ?}.
   *
   * @return the raw SQL string. Must not be null.
   */
  public String getRawSql() {
    return rawSql;
  }

  /**
   * Returns the formatted SQL string.
   *
   * <p>The bind variables are replaced with the string representations of the arguments.
   *
   * @return the formatted SQL string. Must not be null.
   */
  public String getFormattedSql() {
    return formattedSql;
  }

  /**
   * Returns the SQL arguments.
   *
   * @return the SQL arguments. Must not be null.
   */
  public List<SqlArgument> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return rawSql;
  }
}
