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

import java.lang.reflect.Method;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * A repository for {@link SqlFile}.
 *
 * <p>A SQL file corresponds to a DAO method. The path is built from the full qualified name of the
 * DAO and the method name. For example, when {@code org.example.ExampleDao} interface has {@code
 * selectAll} method, the corresponding SQL file path is {@code
 * META-INF/org/example/ExampleDao/selectAll.sql}.
 *
 * <p>The implementation class must be thread safe.
 */
public interface SqlFileRepository {

  /**
   * Returns the SQL file.
   *
   * @param method the DAO method
   * @param path the SQL file path
   * @param dialect the SQL dialect
   * @return the SQL file
   * @throws DomaNullPointerException if any arguments are {@code null}
   * @throws DomaIllegalArgumentException if the {@code method} is not annotated with {@link Sql}
   *     and the {@code path} does not match the Ant-style glob pattern "META-INF&#47;**&#47;*.sql"
   * @throws SqlFileNotFoundException if the SQL file is not found
   * @throws JdbcException if an error other than listed above occurs
   */
  SqlFile getSqlFile(Method method, String path, Dialect dialect);

  /** Clears cache. */
  default void clearCache() {}
}
