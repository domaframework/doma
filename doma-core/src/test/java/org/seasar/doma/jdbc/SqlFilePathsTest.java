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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class SqlFilePathsTest {

  @Test
  public void testOf() {
    String path = SqlFilePaths.of("example.dao.EmployeeDao", "selectById");
    assertEquals("META-INF/example/dao/EmployeeDao/selectById.sql", path);
  }

  @Test
  public void testOf_defaultPackage() {
    String path = SqlFilePaths.of("EmployeeDao", "selectById");
    assertEquals("META-INF/EmployeeDao/selectById.sql", path);
  }

  @Test
  public void testOf_nestedInterface() {
    String path = SqlFilePaths.of("example.dao.Outer.EmployeeDao", "selectById");
    assertEquals("META-INF/example/dao/Outer/EmployeeDao/selectById.sql", path);
  }

  @Test
  public void testOf_sameAsSqlFileUtil() {
    assertEquals(
        SqlFileUtil.buildPath("example.dao.EmployeeDao", "selectById"),
        SqlFilePaths.of("example.dao.EmployeeDao", "selectById"));
  }

  @Test
  public void testOf_null() {
    NullPointerException e1 =
        assertThrows(NullPointerException.class, () -> SqlFilePaths.of(null, "selectById"));
    assertEquals("daoClassName", e1.getMessage());
    NullPointerException e2 =
        assertThrows(
            NullPointerException.class, () -> SqlFilePaths.of("example.dao.EmployeeDao", null));
    assertEquals("methodName", e2.getMessage());
  }

  @Test
  public void testCandidates() {
    List<String> paths =
        SqlFilePaths.candidates("example.dao.EmployeeDao", "selectById", new PostgresDialect());
    assertEquals(
        List.of(
            "META-INF/example/dao/EmployeeDao/selectById-postgres.sql",
            "META-INF/example/dao/EmployeeDao/selectById.sql"),
        paths);
  }

  @Test
  public void testCandidates_unmodifiable() {
    List<String> paths =
        SqlFilePaths.candidates("example.dao.EmployeeDao", "selectById", new PostgresDialect());
    assertThrows(UnsupportedOperationException.class, () -> paths.add("foo.sql"));
  }

  @Test
  public void testCandidates_null() {
    StandardDialect dialect = new StandardDialect();
    NullPointerException e1 =
        assertThrows(
            NullPointerException.class, () -> SqlFilePaths.candidates(null, "selectById", dialect));
    assertEquals("daoClassName", e1.getMessage());
    NullPointerException e2 =
        assertThrows(
            NullPointerException.class,
            () -> SqlFilePaths.candidates("example.dao.EmployeeDao", null, dialect));
    assertEquals("methodName", e2.getMessage());
    NullPointerException e3 =
        assertThrows(
            NullPointerException.class,
            () -> SqlFilePaths.candidates("example.dao.EmployeeDao", "selectById", null));
    assertEquals("dialect", e3.getMessage());
  }
}
