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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GreedyCacheSqlFileRepositoryTest {

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testGetSqlFile() {
    StandardDialect dialect = new StandardDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertNotNull(sqlFile);
    SqlFile sqlFile2 = repository.getSqlFile(method, path, dialect);
    assertSame(sqlFile, sqlFile2);
    assertEquals(path, sqlFile.getPath());
  }

  @Test
  public void testGetSqlFile_oracle() {
    OracleDialect dialect = new OracleDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(
        "META-INF/" + getClass().getName().replace(".", "/") + "-oracle.sql", sqlFile.getPath());
  }

  @Test
  public void testGetSqlFile_postgres() {
    PostgresDialect dialect = new PostgresDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertEquals(path, sqlFile.getPath());
  }

  @Test
  public void testClearCache() {
    StandardDialect dialect = new StandardDialect();
    String path = "META-INF/" + getClass().getName().replace(".", "/") + ".sql";
    GreedyCacheSqlFileRepository repository = new GreedyCacheSqlFileRepository();
    SqlFile sqlFile = repository.getSqlFile(method, path, dialect);
    assertNotNull(sqlFile);
    repository.clearCache();
    SqlFile sqlFile2 = repository.getSqlFile(method, path, dialect);
    assertNotSame(sqlFile, sqlFile2);
    assertEquals(path, sqlFile.getPath());
  }
}
