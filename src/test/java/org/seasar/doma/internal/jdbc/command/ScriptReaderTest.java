package org.seasar.doma.internal.jdbc.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;
import org.seasar.doma.jdbc.query.StaticScriptQuery;

public class ScriptReaderTest extends TestCase {

  private StaticScriptQuery query;

  @Override
  public void setUp() {
    var config = new MockConfig();
    config.dialect = new Mssql2008Dialect();
    query =
        new StaticScriptQuery() {

          @Override
          public void prepare() {}
        };
    query.setConfig(config);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setBlockDelimiter(config.dialect.getScriptBlockDelimiter());
    query.prepare();
  }

  public void testReadSql_delimiter() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            var buf = new StringBuilder();
            buf.append("aaa;\n");
            buf.append("bbb\n");
            buf.append("go\n");
            buf.append("ccc\n");
            buf.append("ddd\n");
            var reader = new StringReader(buf.toString());
            return new BufferedReader(reader);
          }
        };
    assertEquals("aaa", reader.readSql());
    assertEquals("bbb", reader.readSql());
    assertEquals("ccc ddd", reader.readSql());
    assertNull(reader.readSql());
  }

  public void testReadSql_delimiterInLine() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            var buf = new StringBuilder();
            buf.append("aaa; bbb; ccc;\n");
            var reader = new StringReader(buf.toString());
            return new BufferedReader(reader);
          }
        };
    assertEquals("aaa", reader.readSql());
    assertEquals("bbb", reader.readSql());
    assertEquals("ccc", reader.readSql());
    assertNull(reader.readSql());
  }

  public void testReadSql_sqlBlock() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            var buf = new StringBuilder();
            buf.append("begin aaa; end\n");
            buf.append("go\n");
            var reader = new StringReader(buf.toString());
            return new BufferedReader(reader);
          }
        };
    assertEquals("begin aaa; end", reader.readSql());
    assertNull(reader.readSql());
  }

  public void testReadSql_sqlBlock_createTrigger() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            var buf = new StringBuilder();
            buf.append("create trigger hoge begin aaa; end\n");
            buf.append("go\n");
            var reader = new StringReader(buf.toString());
            return new BufferedReader(reader);
          }
        };
    assertEquals("create trigger hoge begin aaa; end", reader.readSql());
    assertNull(reader.readSql());
  }

  public void testReadSql_notSqlBlock() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {
          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            var buf = new StringBuilder();
            buf.append("start aaa; end\n");
            buf.append("go\n");
            var reader = new StringReader(buf.toString());
            return new BufferedReader(reader);
          }
        };
    assertEquals("start aaa", reader.readSql());
    assertEquals("end", reader.readSql());
  }

  public void testReadSql_commentBlock() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            var buf = new StringBuilder();
            buf.append("select 1 ; /* aaa\n");
            buf.append("aaa */ select 2;");
            var reader = new StringReader(buf.toString());
            return new BufferedReader(reader);
          }
        };
    assertEquals("select 1", reader.readSql());
    assertEquals("select 2", reader.readSql());
    assertNull(reader.readSql());
  }

  public void testReadSql_lineNumber() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            var buf = new StringBuilder();
            buf.append("/*\n");
            buf.append(" *\n");
            buf.append(" */\n");
            buf.append("select 1\n");
            buf.append("from \n");
            buf.append("hoge\n");
            var reader = new StringReader(buf.toString());
            return new BufferedReader(reader);
          }
        };
    assertNotNull(reader.readSql());
    assertEquals(4, reader.getLineNumber());
  }
}
