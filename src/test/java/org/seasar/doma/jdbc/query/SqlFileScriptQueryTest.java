package org.seasar.doma.jdbc.query;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import junit.framework.TestCase;
import org.seasar.doma.experimental.Sql;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;

public class SqlFileScriptQueryTest extends TestCase {

  private final MockConfig config = new MockConfig();

  private Method method;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    method = getClass().getMethod(getName());
  }

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
  public void testGetReaderSupplier() throws Exception {
    SqlFileScriptQuery query = new SqlFileScriptQuery();
    query.setConfig(config);
    query.setMethod(method);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setScriptFilePath(String.format("%s#%s", getClass().getName(), getName()));
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
