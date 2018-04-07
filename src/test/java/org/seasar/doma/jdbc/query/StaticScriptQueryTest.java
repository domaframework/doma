package org.seasar.doma.jdbc.query;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.ScriptFileNotFoundException;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;

public class StaticScriptQueryTest extends TestCase {

  private final MockConfig config = new MockConfig();

  public void testPrepare() throws Exception {
    var query = new StaticScriptQuery();
    query.setConfig(config);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setBlockDelimiter("");
    query.prepare();

    assertEquals(config, query.getConfig());
    assertEquals("aaa", query.getClassName());
    assertEquals("bbb", query.getMethodName());
    assertEquals(
        "META-INF/org/seasar/doma/jdbc/query/StaticScriptQueryTest/testPrepare.script",
        query.getSqlFilePath());
    assertNull(query.getBlockDelimiter());
  }

  public void testPrepare_dbmsSpecific() throws Exception {
    config.dialect = new Mssql2008Dialect();
    var query = new StaticScriptQuery();
    query.setConfig(config);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setBlockDelimiter("");
    query.prepare();

    assertEquals(config, query.getConfig());
    assertEquals("aaa", query.getClassName());
    assertEquals("bbb", query.getMethodName());
    assertEquals(
        "META-INF/org/seasar/doma/jdbc/query/StaticScriptQueryTest/testPrepare_dbmsSpecific-mssql.script",
        query.getSqlFilePath());
    assertEquals("GO", query.getBlockDelimiter());
  }

  public void testPrepare_ScriptFileNotFoundException() throws Exception {
    var query = new StaticScriptQuery();
    query.setConfig(config);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setMethod(getClass().getDeclaredMethod(getName()));
    query.setBlockDelimiter("ddd");
    try {
      query.prepare();
      fail();
    } catch (ScriptFileNotFoundException expected) {
      System.out.println(expected.getMessage());
    }
  }
}
