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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.Sql;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AbstractSqlFileRepositoryTest {

  @Test
  public void testGetSqlFile(TestInfo testInfo) {
    SqlFileRepository repository = new MySqlFileRepository();
    Method method = testInfo.getTestMethod().get();
    String path = String.format("META-INF/%s.sql", getClass().getName().replace(".", "/"));
    Dialect dialect = new StandardDialect();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals("select * from employee", sqlFile.getSql());
  }

  @Test
  public void testGetSqlFile_illegalPath(TestInfo testInfo) {
    SqlFileRepository repository = new MySqlFileRepository();
    Method method = testInfo.getTestMethod().get();
    String path = method.getName();
    Dialect dialect = new StandardDialect();
    try {
      repository.getSqlFile(method, path, dialect);
      fail();
    } catch (DomaIllegalArgumentException ignored) {
    }
  }

  @Sql("select * from address")
  @Test
  public void testGetSqlFile_SqlAnnotation(TestInfo testInfo) {
    SqlFileRepository repository = new MySqlFileRepository();
    Method method = testInfo.getTestMethod().get();
    String path = method.getName();
    Dialect dialect = new StandardDialect();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals("select * from address", sqlFile.getSql());
  }

  private static class MySqlFileRepository extends AbstractSqlFileRepository {

    @Override
    protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
      return createSqlFile(method, path, dialect);
    }
  }
}
