package org.seasar.doma.internal.jdbc.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.AND_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.BIND_VARIABLE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.DELIMITER;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.END_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EOF;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EOL;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EXCEPT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EXPAND_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.FOR_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.FOR_UPDATE_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.FROM_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.GROUP_BY_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.HAVING_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.IF_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.INTERSECT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.IN_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.LINE_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.LITERAL_VARIABLE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.MINUS_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OPENED_PARENS;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OPTION_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.ORDER_BY_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OR_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.OTHER;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.PARSER_LEVEL_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.POPULATE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.QUOTE;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.SELECT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.SET_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.UNION_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.UPDATE_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.WHERE_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.WHITESPACE;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.WORD;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

public class SqlTokenizerTest {

  private String lineSeparator;

  @BeforeEach
  protected void setUp() {
    lineSeparator = System.lineSeparator();
    System.setProperty("line.separator", "\r\n");
  }

  @AfterEach
  protected void tearDown() {
    System.setProperty("line.separator", lineSeparator);
  }

  @Test
  public void testEof() {
    SqlTokenizer tokenizer = new SqlTokenizer("where");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testDelimiter() {
    SqlTokenizer tokenizer = new SqlTokenizer("where;");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(DELIMITER, tokenizer.next());
    assertEquals(";", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLineComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where--aaa\r\nbbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(LINE_COMMENT, tokenizer.next());
    assertEquals("--aaa", tokenizer.getToken());
    assertEquals(EOL, tokenizer.next());
    assertEquals("\r\n", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*+aaa*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*+aaa*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBlockComment_empty() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /**/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BLOCK_COMMENT, tokenizer.next());
    assertEquals("/**/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testParserLevelBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*%!aaa*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(PARSER_LEVEL_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%!aaa*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testQuote() {
    SqlTokenizer tokenizer = new SqlTokenizer("where 'aaa'");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(QUOTE, tokenizer.next());
    assertEquals("'aaa'", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testQuote_escaped() {
    SqlTokenizer tokenizer = new SqlTokenizer("where 'aaa'''");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(QUOTE, tokenizer.next());
    assertEquals("'aaa'''", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testQuote_notClosed() {
    SqlTokenizer tokenizer = new SqlTokenizer("where 'aaa");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    try {
      tokenizer.next();
      fail();
    } catch (JdbcException expected) {
    }
  }

  @Test
  public void testQuote_escaped_notClosed() {
    SqlTokenizer tokenizer = new SqlTokenizer("where 'aaa''bbb''");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    try {
      tokenizer.next();
      fail();
    } catch (JdbcException expected) {
    }
  }

  @Test
  public void testUnion() {
    SqlTokenizer tokenizer = new SqlTokenizer("union");
    assertEquals(UNION_WORD, tokenizer.next());
    assertEquals("union", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testExcept() {
    SqlTokenizer tokenizer = new SqlTokenizer("except");
    assertEquals(EXCEPT_WORD, tokenizer.next());
    assertEquals("except", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testMinus() {
    SqlTokenizer tokenizer = new SqlTokenizer("minus");
    assertEquals(MINUS_WORD, tokenizer.next());
    assertEquals("minus", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIntersect() {
    SqlTokenizer tokenizer = new SqlTokenizer("intersect");
    assertEquals(INTERSECT_WORD, tokenizer.next());
    assertEquals("intersect", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testSelect() {
    SqlTokenizer tokenizer = new SqlTokenizer("select");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFrom() {
    SqlTokenizer tokenizer = new SqlTokenizer("from");
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testWhere() {
    SqlTokenizer tokenizer = new SqlTokenizer("where");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testGroupBy() {
    SqlTokenizer tokenizer = new SqlTokenizer("group by");
    assertEquals(GROUP_BY_WORD, tokenizer.next());
    assertEquals("group by", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testHaving() {
    SqlTokenizer tokenizer = new SqlTokenizer("having");
    assertEquals(HAVING_WORD, tokenizer.next());
    assertEquals("having", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOrderBy() {
    SqlTokenizer tokenizer = new SqlTokenizer("order by");
    assertEquals(ORDER_BY_WORD, tokenizer.next());
    assertEquals("order by", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testForUpdateBy() {
    SqlTokenizer tokenizer = new SqlTokenizer("for update");
    assertEquals(FOR_UPDATE_WORD, tokenizer.next());
    assertEquals("for update", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOption() {
    SqlTokenizer tokenizer = new SqlTokenizer("option (");
    assertEquals(OPTION_WORD, tokenizer.next());
    assertEquals("option", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testUpdate() {
    SqlTokenizer tokenizer = new SqlTokenizer("update");
    assertEquals(UPDATE_WORD, tokenizer.next());
    assertEquals("update", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testSet() {
    SqlTokenizer tokenizer = new SqlTokenizer("set");
    assertEquals(SET_WORD, tokenizer.next());
    assertEquals("set", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIn() {
    SqlTokenizer tokenizer = new SqlTokenizer("in");
    assertEquals(IN_WORD, tokenizer.next());
    assertEquals("in", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testAnd() {
    SqlTokenizer tokenizer = new SqlTokenizer("and");
    assertEquals(AND_WORD, tokenizer.next());
    assertEquals("and", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOr() {
    SqlTokenizer tokenizer = new SqlTokenizer("or");
    assertEquals(OR_WORD, tokenizer.next());
    assertEquals("or", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBindBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*aaa*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*aaa*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBindBlockComment_followingQuote() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*aaa*/'2001-01-01 12:34:56'");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*aaa*/", tokenizer.getToken());
    assertEquals(QUOTE, tokenizer.next());
    assertEquals("'2001-01-01 12:34:56'", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBindBlockComment_followingWordAndQuote() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*aaa*/timestamp'2001-01-01 12:34:56' and");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*aaa*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("timestamp'2001-01-01 12:34:56'", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(AND_WORD, tokenizer.next());
    assertEquals("and", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBindBlockComment_spaceIncluded() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /* aaa */bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/* aaa */", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBindBlockComment_startWithStringLiteral() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*\"aaa\"*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*\"aaa\"*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBindBlockComment_startWithCharLiteral() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*'a'*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*'a'*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLiteralBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*^aaa*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(LITERAL_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*^aaa*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLiteralBlockComment_followingQuote() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*^aaa*/'2001-01-01 12:34:56'");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(LITERAL_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*^aaa*/", tokenizer.getToken());
    assertEquals(QUOTE, tokenizer.next());
    assertEquals("'2001-01-01 12:34:56'", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLiteralBlockComment_spaceIncluded() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*^ aaa */bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(LITERAL_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*^ aaa */", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIfBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*%if true*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(IF_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%if true*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testForBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*%for element : list*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FOR_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%for element : list*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testEndBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("where bbb/*%end*/");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(END_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%end*/", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testExpandBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("select /*%expand*/* from");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(EXPAND_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%expand*/", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("*", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testExpandBlockComment_alias() {
    SqlTokenizer tokenizer = new SqlTokenizer("select /*%expand e*/* from");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(EXPAND_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%expand e*/", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("*", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testPopulateBlockComment() {
    SqlTokenizer tokenizer = new SqlTokenizer("set /*%populate*/ id = id");
    assertEquals(SET_WORD, tokenizer.next());
    assertEquals("set", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(POPULATE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%populate*/", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("id", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("id", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLineNumber() {
    SqlTokenizer tokenizer = new SqlTokenizer("aaa\nbbb\nccc\n/* \nddd\n */");
    assertEquals(1, tokenizer.getLineNumber());
    assertEquals(WORD, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(1, tokenizer.getLineNumber());
    assertEquals(EOL, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(2, tokenizer.getLineNumber());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(2, tokenizer.getLineNumber());
    assertEquals(EOL, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(3, tokenizer.getLineNumber());
    assertEquals(WORD, tokenizer.next());
    assertEquals("ccc", tokenizer.getToken());
    assertEquals(3, tokenizer.getLineNumber());
    assertEquals(EOL, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(4, tokenizer.getLineNumber());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/* \nddd\n */", tokenizer.getToken());
    assertEquals(6, tokenizer.getLineNumber());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testColumnNumber() {
    SqlTokenizer tokenizer = new SqlTokenizer("aaa bbb\nc\nd eee\n");
    assertEquals(0, tokenizer.getPosition());
    assertEquals(WORD, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(3, tokenizer.getPosition());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(4, tokenizer.getPosition());
    assertEquals(WORD, tokenizer.next());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(7, tokenizer.getPosition());
    assertEquals(EOL, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(0, tokenizer.getPosition());
    assertEquals(WORD, tokenizer.next());
    assertEquals("c", tokenizer.getToken());
    assertEquals(1, tokenizer.getPosition());
    assertEquals(EOL, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(0, tokenizer.getPosition());
    assertEquals(WORD, tokenizer.next());
    assertEquals("d", tokenizer.getToken());
    assertEquals(1, tokenizer.getPosition());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(2, tokenizer.getPosition());
    assertEquals(WORD, tokenizer.next());
    assertEquals("eee", tokenizer.getToken());
    assertEquals(5, tokenizer.getPosition());
    assertEquals(EOL, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(0, tokenizer.getPosition());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIllegalDirective() {
    SqlTokenizer tokenizer = new SqlTokenizer("where /*%*/bbb");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    try {
      tokenizer.next();
      fail();
    } catch (JdbcException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA2119, expected.getMessageResource());
    }
  }
}
