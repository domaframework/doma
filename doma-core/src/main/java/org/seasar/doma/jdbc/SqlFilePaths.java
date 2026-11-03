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

import java.util.List;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * Provides the paths of the SQL files of DAO methods.
 *
 * <p>The paths follow the same rules as the annotation processor and the runtime, so tools that
 * work with Doma projects, such as editor plugins and linters, can find SQL files without
 * reimplementing those rules.
 *
 * <p>The paths are relative to the root of the classpath and use {@code /} as the separator, for
 * example {@code META-INF/example/dao/EmployeeDao/selectById.sql}.
 *
 * @see ScriptFilePaths
 */
public final class SqlFilePaths {

  private SqlFilePaths() {}

  /**
   * Returns the path of the SQL file of a DAO method.
   *
   * <p>For example, {@code of("example.dao.EmployeeDao", "selectById")} returns {@code
   * META-INF/example/dao/EmployeeDao/selectById.sql}.
   *
   * @param daoClassName the canonical name of the DAO interface, as returned by {@link
   *     Class#getCanonicalName()}
   * @param methodName the name of the DAO method
   * @return the path of the SQL file
   * @throws NullPointerException if any argument is {@code null}
   */
  public static String of(String daoClassName, String methodName) {
    Objects.requireNonNull(daoClassName, "daoClassName");
    Objects.requireNonNull(methodName, "methodName");
    return SqlFileUtil.buildPath(daoClassName, methodName);
  }

  /**
   * Returns the paths of the SQL files of a DAO method in the order Doma looks them up.
   *
   * <p>The first path is specific to the RDBMS of the dialect, and the second one is the generic
   * path returned by {@link #of(String, String)}. For example, {@code
   * candidates("example.dao.EmployeeDao", "selectById", new PostgresDialect())} returns {@code
   * [META-INF/example/dao/EmployeeDao/selectById-postgres.sql,
   * META-INF/example/dao/EmployeeDao/selectById.sql]}.
   *
   * <p>Doma uses the first path that exists, so a tool that looks the files up in its own file
   * system, such as a source tree, can do the same.
   *
   * @param daoClassName the canonical name of the DAO interface, as returned by {@link
   *     Class#getCanonicalName()}
   * @param methodName the name of the DAO method
   * @param dialect the dialect whose {@link Dialect#getName() name} the RDBMS-specific path uses
   * @return the unmodifiable list of the paths
   * @throws NullPointerException if any argument is {@code null}
   */
  public static List<String> candidates(String daoClassName, String methodName, Dialect dialect) {
    Objects.requireNonNull(daoClassName, "daoClassName");
    Objects.requireNonNull(methodName, "methodName");
    Objects.requireNonNull(dialect, "dialect");
    return SqlFileUtil.buildCandidatePaths(
        SqlFileUtil.buildPath(daoClassName, methodName), dialect);
  }
}
