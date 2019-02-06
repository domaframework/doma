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
import org.seasar.doma.experimental.Sql;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;

public class SqlFileScriptQueryTest {

  private final MockConfig config = new MockConfig();

  private Method method;

  @BeforeEach
  protected void setUp(TestInfo testInfo) throws Exception {
    method = testInfo.getTestMethod().get();
  }

  @Test
  public void testPrepare() throws Exception {
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
  public void testPrepare_dbmsSpecific() throws Exception {
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
  public void testPrepare_ScriptFileNotFoundException() throws Exception {
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
