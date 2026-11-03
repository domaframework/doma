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
import org.seasar.doma.internal.jdbc.util.ScriptFileUtil;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;

public class ScriptFilePathsTest {

  @Test
  public void testOf() {
    String path = ScriptFilePaths.of("example.dao.EmployeeDao", "createTable");
    assertEquals("META-INF/example/dao/EmployeeDao/createTable.script", path);
  }

  @Test
  public void testOf_defaultPackage() {
    String path = ScriptFilePaths.of("EmployeeDao", "createTable");
    assertEquals("META-INF/EmployeeDao/createTable.script", path);
  }

  @Test
  public void testOf_nestedInterface() {
    String path = ScriptFilePaths.of("example.dao.Outer.EmployeeDao", "createTable");
    assertEquals("META-INF/example/dao/Outer/EmployeeDao/createTable.script", path);
  }

  @Test
  public void testOf_sameAsScriptFileUtil() {
    assertEquals(
        ScriptFileUtil.buildPath("example.dao.EmployeeDao", "createTable"),
        ScriptFilePaths.of("example.dao.EmployeeDao", "createTable"));
  }

  @Test
  public void testOf_null() {
    NullPointerException e1 =
        assertThrows(NullPointerException.class, () -> ScriptFilePaths.of(null, "createTable"));
    assertEquals("daoClassName", e1.getMessage());
    NullPointerException e2 =
        assertThrows(
            NullPointerException.class, () -> ScriptFilePaths.of("example.dao.EmployeeDao", null));
    assertEquals("methodName", e2.getMessage());
  }

  @Test
  public void testCandidates() {
    List<String> paths =
        ScriptFilePaths.candidates("example.dao.EmployeeDao", "createTable", new PostgresDialect());
    assertEquals(
        List.of(
            "META-INF/example/dao/EmployeeDao/createTable-postgres.script",
            "META-INF/example/dao/EmployeeDao/createTable.script"),
        paths);
  }

  @Test
  public void testCandidates_unmodifiable() {
    List<String> paths =
        ScriptFilePaths.candidates("example.dao.EmployeeDao", "createTable", new PostgresDialect());
    assertThrows(UnsupportedOperationException.class, () -> paths.add("foo.script"));
  }

  @Test
  public void testCandidates_null() {
    StandardDialect dialect = new StandardDialect();
    NullPointerException e1 =
        assertThrows(
            NullPointerException.class,
            () -> ScriptFilePaths.candidates(null, "createTable", dialect));
    assertEquals("daoClassName", e1.getMessage());
    NullPointerException e2 =
        assertThrows(
            NullPointerException.class,
            () -> ScriptFilePaths.candidates("example.dao.EmployeeDao", null, dialect));
    assertEquals("methodName", e2.getMessage());
    NullPointerException e3 =
        assertThrows(
            NullPointerException.class,
            () -> ScriptFilePaths.candidates("example.dao.EmployeeDao", "createTable", null));
    assertEquals("dialect", e3.getMessage());
  }
}
