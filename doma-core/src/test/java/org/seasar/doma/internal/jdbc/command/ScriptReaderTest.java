package org.seasar.doma.internal.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;
import org.seasar.doma.jdbc.query.SqlFileScriptQuery;

public class ScriptReaderTest {

  private SqlFileScriptQuery query;

  @BeforeEach
  public void setUp() {
    MockConfig config = new MockConfig();
    config.dialect = new Mssql2008Dialect();
    query =
        new SqlFileScriptQuery() {

          @Override
          public void prepare() {}
        };
    query.setConfig(config);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setBlockDelimiter(config.dialect.getScriptBlockDelimiter());
    query.prepare();
  }

  @Test
  public void testReadSql_delimiter() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            String script = "aaa;\nbbb\ngo\nccc\nddd\n";
            StringReader reader = new StringReader(script);
            return new BufferedReader(reader);
          }
        };
    assertEquals("aaa", reader.readSql());
    assertEquals("bbb", reader.readSql());
    assertEquals("ccc ddd", reader.readSql());
    assertNull(reader.readSql());
  }

  @Test
  public void testReadSql_delimiterInLine() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            StringReader reader = new StringReader("aaa; bbb; ccc;\n");
            return new BufferedReader(reader);
          }
        };
    assertEquals("aaa", reader.readSql());
    assertEquals("bbb", reader.readSql());
    assertEquals("ccc", reader.readSql());
    assertNull(reader.readSql());
  }

  @Test
  public void testReadSql_sqlBlock() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            String script = "begin aaa; end\n" + "go\n";
            StringReader reader = new StringReader(script);
            return new BufferedReader(reader);
          }
        };
    assertEquals("begin aaa; end", reader.readSql());
    assertNull(reader.readSql());
  }

  @Test
  public void testReadSql_sqlBlock_createTrigger() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            String script = "create trigger hoge begin aaa; end\n" + "go\n";
            StringReader reader = new StringReader(script);
            return new BufferedReader(reader);
          }
        };
    assertEquals("create trigger hoge begin aaa; end", reader.readSql());
    assertNull(reader.readSql());
  }

  @Test
  public void testReadSql_notSqlBlock() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {
          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            String script = "start aaa; end\n" + "go\n";
            StringReader reader = new StringReader(script);
            return new BufferedReader(reader);
          }
        };
    assertEquals("start aaa", reader.readSql());
    assertEquals("end", reader.readSql());
  }

  @Test
  public void testReadSql_commentBlock() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            String script = "select 1 ; /* aaa\n" + "aaa */ select 2;";
            StringReader reader = new StringReader(script);
            return new BufferedReader(reader);
          }
        };
    assertEquals("select 1", reader.readSql());
    assertEquals("select 2", reader.readSql());
    assertNull(reader.readSql());
  }

  @Test
  public void testReadSql_lineNumber() throws Exception {
    ScriptReader reader =
        new ScriptReader(query) {

          @Override
          protected BufferedReader createBufferedReader() throws IOException {
            String script = "/*\n *\n */\nselect 1\nfrom \nhoge\n";
            StringReader reader = new StringReader(script);
            return new BufferedReader(reader);
          }
        };
    assertNotNull(reader.readSql());
    assertEquals(4, reader.getLineNumber());
  }
}
