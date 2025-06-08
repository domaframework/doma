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
package org.seasar.doma.internal.jdbc.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.AND_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.BIND_VARIABLE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.CLOSED_PARENS;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.DELIMITER;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.DISTINCT_WORD;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.ELSEIF_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.ELSE_BLOCK_COMMENT;
import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.EMBEDDED_VARIABLE_BLOCK_COMMENT;
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

/**
 * Test class for ClassicSqlTokenizer to verify that the original implementation works correctly.
 * This preserves the behavior verification for the classic implementation before refactoring.
 */
@SuppressWarnings("deprecation")
public class ClassicSqlTokenizerTest {

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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testDelimiter() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where;");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(DELIMITER, tokenizer.next());
    assertEquals(";", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLineComment() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where--aaa\r\nbbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*+aaa*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /**/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*%!aaa*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where 'aaa'");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where 'aaa'''");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where 'aaa");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where 'aaa''bbb''");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("union");
    assertEquals(UNION_WORD, tokenizer.next());
    assertEquals("union", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testExcept() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("except");
    assertEquals(EXCEPT_WORD, tokenizer.next());
    assertEquals("except", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testMinus() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("minus");
    assertEquals(MINUS_WORD, tokenizer.next());
    assertEquals("minus", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIntersect() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("intersect");
    assertEquals(INTERSECT_WORD, tokenizer.next());
    assertEquals("intersect", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testSelect() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("select");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFrom() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("from");
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testWhere() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testGroupBy() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("group by");
    assertEquals(GROUP_BY_WORD, tokenizer.next());
    assertEquals("group by", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testHaving() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("having");
    assertEquals(HAVING_WORD, tokenizer.next());
    assertEquals("having", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOrderBy() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("order by");
    assertEquals(ORDER_BY_WORD, tokenizer.next());
    assertEquals("order by", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testForUpdateBy() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("for update");
    assertEquals(FOR_UPDATE_WORD, tokenizer.next());
    assertEquals("for update", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOption() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("option (");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("update");
    assertEquals(UPDATE_WORD, tokenizer.next());
    assertEquals("update", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testSet() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("set");
    assertEquals(SET_WORD, tokenizer.next());
    assertEquals("set", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIn() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("in");
    assertEquals(IN_WORD, tokenizer.next());
    assertEquals("in", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testAnd() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("and");
    assertEquals(AND_WORD, tokenizer.next());
    assertEquals("and", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOr() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("or");
    assertEquals(OR_WORD, tokenizer.next());
    assertEquals("or", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBindBlockComment() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*aaa*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*aaa*/'2001-01-01 12:34:56'");
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
    ClassicSqlTokenizer tokenizer =
        new ClassicSqlTokenizer("where /*aaa*/timestamp'2001-01-01 12:34:56' and");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /* aaa */bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*\"aaa\"*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*'a'*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*^aaa*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*^aaa*/'2001-01-01 12:34:56'");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*^ aaa */bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*%if true*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*%for element : list*/bbb");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where bbb/*%end*/");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("select /*%expand*/* from");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("select /*%expand e*/* from");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("set /*%populate*/ id = id");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("aaa\nbbb\nccc\n/* \nddd\n */");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("aaa bbb\nc\nd eee\n");
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
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*%*/bbb");
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

  @Test
  public void testDoubleQuoteAsWord() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where \"column\"");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("\"column\"", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNumbers() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where id = 123");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
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
    assertEquals("123", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testDecimalNumbers() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where price = 123.45");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("price", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("123.45", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeNumbers() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where amount = -100");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("amount", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("-100", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testPositiveNumbers() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where amount = +100");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("amount", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("+100", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testClosedParens() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("(select count(*) from table)");
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("count", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("*", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("table", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testDistinct() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("select distinct");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(DISTINCT_WORD, tokenizer.next());
    assertEquals("distinct", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBlockComment_multiline() {
    ClassicSqlTokenizer tokenizer =
        new ClassicSqlTokenizer("select /*+ this is\na multiline\ncomment */ from");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*+ this is\na multiline\ncomment */", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBlockComment_notClosed() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("select /* unclosed comment");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    try {
      tokenizer.next();
      fail();
    } catch (JdbcException expected) {
      assertEquals(Message.DOMA2102, expected.getMessageResource());
    }
  }

  @Test
  public void testEmbeddedBlockComment() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("where /*#orderBy*/order by id");
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(EMBEDDED_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*#orderBy*/", tokenizer.getToken());
    assertEquals(ORDER_BY_WORD, tokenizer.next());
    assertEquals("order by", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("id", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testElseBlockComment() {
    ClassicSqlTokenizer tokenizer =
        new ClassicSqlTokenizer("/*%if true*/where id = 1/*%else*/where id = 2/*%end*/");
    assertEquals(IF_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%if true*/", tokenizer.getToken());
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
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
    assertEquals("1", tokenizer.getToken());
    assertEquals(ELSE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%else*/", tokenizer.getToken());
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
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
    assertEquals("2", tokenizer.getToken());
    assertEquals(END_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%end*/", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testElseIfBlockComment() {
    ClassicSqlTokenizer tokenizer =
        new ClassicSqlTokenizer("/*%if true*/where id = 1/*%elseif false*/where id = 2/*%end*/");
    assertEquals(IF_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%if true*/", tokenizer.getToken());
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
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
    assertEquals("1", tokenizer.getToken());
    assertEquals(ELSEIF_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%elseif false*/", tokenizer.getToken());
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
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
    assertEquals("2", tokenizer.getToken());
    assertEquals(END_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*%end*/", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testComplexSQL() {
    ClassicSqlTokenizer tokenizer =
        new ClassicSqlTokenizer(
            "select e.id, e.name from employee e where e.dept_id = /*deptId*/1 and e.salary > /*minSalary*/50000");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("e.id", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals(",", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("e.name", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("employee", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("e", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("where", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("e.dept_id", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*deptId*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("1", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(AND_WORD, tokenizer.next());
    assertEquals("and", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("e.salary", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals(">", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIND_VARIABLE_BLOCK_COMMENT, tokenizer.next());
    assertEquals("/*minSalary*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("50000", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testTabWhitespace() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("select\tid\tfrom\ttable");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("select", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\t", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("id", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\t", tokenizer.getToken());
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("from", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\t", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("table", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testMixedCaseKeywords() {
    ClassicSqlTokenizer tokenizer = new ClassicSqlTokenizer("SeLeCt * FrOm TaBlE wHeRe Id = 1");
    assertEquals(SELECT_WORD, tokenizer.next());
    assertEquals("SeLeCt", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("*", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FROM_WORD, tokenizer.next());
    assertEquals("FrOm", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("TaBlE", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHERE_WORD, tokenizer.next());
    assertEquals("wHeRe", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("Id", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.next());
    assertEquals("=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.next());
    assertEquals("1", tokenizer.getToken());
    assertEquals(EOF, tokenizer.next());
    assertNull(tokenizer.getToken());
  }
}
