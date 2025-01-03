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
package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.seasar.doma.Sql;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class SqlFileScriptQueryTest {

  private final MockConfig config = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() {
    SqlFileScriptQuery query = new SqlFileScriptQuery();
    query.setConfig(config);
    query.setMethod(method);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setScriptFilePath(
        "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare.script");
    query.setBlockDelimiter("");
    query.prepare();

    assertEquals(config, query.getConfig());
    assertEquals("aaa", query.getClassName());
    assertEquals("bbb", query.getMethodName());
    assertEquals(
        "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare.script",
        query.getScriptFilePath());
    assertNotNull(query.getScriptFileUrl());
    assertNull(query.getBlockDelimiter());
  }

  @Test
  public void testPrepare_dbmsSpecific() {
    config.dialect = new Mssql2008Dialect();
    SqlFileScriptQuery query = new SqlFileScriptQuery();
    query.setConfig(config);
    query.setMethod(method);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setScriptFilePath(
        "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare_dbmsSpecific.script");
    query.setBlockDelimiter("");
    query.prepare();

    assertEquals(config, query.getConfig());
    assertEquals("aaa", query.getClassName());
    assertEquals("bbb", query.getMethodName());
    assertEquals(
        "META-INF/org/seasar/doma/jdbc/query/SqlFileScriptQueryTest/testPrepare_dbmsSpecific-mssql.script",
        query.getScriptFilePath());
    assertNotNull(query.getScriptFileUrl());
    assertEquals("GO", query.getBlockDelimiter());
  }

  @Test
  public void testPrepare_ScriptFileNotFoundException() {
    SqlFileScriptQuery query = new SqlFileScriptQuery();
    query.setConfig(config);
    query.setMethod(method);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setScriptFilePath("META-INF/ccc.script");
    query.setBlockDelimiter("ddd");
    try {
      query.prepare();
      fail();
    } catch (ScriptFileNotFoundException expected) {
      System.out.println(expected.getMessage());
    }
  }

  @Sql("drop table employee;\ndrop table department;")
  @Test
  public void testGetReaderSupplier() throws Exception {
    SqlFileScriptQuery query = new SqlFileScriptQuery();
    query.setConfig(config);
    query.setMethod(method);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setScriptFilePath(String.format("%s#%s", getClass().getName(), method.getName()));
    query.setBlockDelimiter("ddd");
    query.prepare();

    Supplier<Reader> supplier = query.getReaderSupplier();
    try (BufferedReader reader = new BufferedReader(supplier.get())) {
      assertEquals("drop table employee;", reader.readLine());
      assertEquals("drop table department;", reader.readLine());
      assertNull(reader.readLine());
    }
  }
}
