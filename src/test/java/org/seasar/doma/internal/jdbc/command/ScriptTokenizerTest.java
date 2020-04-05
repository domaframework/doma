package org.seasar.doma.internal.jdbc.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.seasar.doma.internal.jdbc.command.ScriptTokenType.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScriptTokenizerTest {

  private ScriptTokenizer tokenizer;

  @BeforeEach
  public void setUp() throws Exception {
    tokenizer = new ScriptTokenizer("/");
  }

  @Test
  public void testGetToken_endOfLine() {
    tokenizer.addLine("aaa");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
    assertEquals("", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
    assertEquals("", tokenizer.getToken());
  }

  @Test
  public void testGetToken_endOfFile() {
    tokenizer.addLine("aaa");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
    assertEquals("", tokenizer.getToken());

    tokenizer.addLine(null);
    assertEquals(END_OF_FILE, tokenizer.nextToken());
    assertNull(tokenizer.getToken());
    assertEquals(END_OF_FILE, tokenizer.nextToken());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testGetToken_lineComment() {
    tokenizer.addLine("aaa -- bbb /* ; ");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.nextToken());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(LINE_COMMENT, tokenizer.nextToken());
    assertEquals("-- bbb /* ; ", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }

  @Test
  public void testGetToken_blockCommentInTwoLines() {
    tokenizer.addLine("aaa/*b");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(START_OF_BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("/*", tokenizer.getToken());
    assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("b", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
    assertEquals("", tokenizer.getToken());

    tokenizer.addLine("bb*/ccc");
    assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("bb", tokenizer.getToken());
    assertEquals(END_OF_BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("ccc", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }

  @Test
  public void testGetToken_blockCommentsInOneLine() {
    tokenizer.addLine("aaa/*bbb*/ccc/*ddd*/");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(START_OF_BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("/*", tokenizer.getToken());
    assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(END_OF_BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("*/", tokenizer.getToken());
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("ccc", tokenizer.getToken());
    assertEquals(START_OF_BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("/*", tokenizer.getToken());
    assertEquals(BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("ddd", tokenizer.getToken());
    assertEquals(END_OF_BLOCK_COMMENT, tokenizer.nextToken());
    assertEquals("*/", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }

  @Test
  public void testGetToken_statementDelimiter() throws Exception {
    tokenizer.addLine("select * from aaa; ");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("select", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.nextToken());
    assertEquals(" * ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("from", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.nextToken());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(STATEMENT_DELIMITER, tokenizer.nextToken());
    assertEquals(";", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.nextToken());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }

  @Test
  public void testGetToken_blockDelimiter() throws Exception {
    tokenizer.addLine("aaa go");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.nextToken());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("go", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
    assertEquals("", tokenizer.getToken());

    tokenizer.addLine("/ ");
    assertEquals(BLOCK_DELIMITER, tokenizer.nextToken());
    assertEquals("/ ", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }

  @Test
  public void testGetToken_wordAndOther() throws Exception {
    tokenizer.addLine("select,");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("select", tokenizer.getToken());
    assertEquals(OTHER, tokenizer.nextToken());
    assertEquals(",", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
    assertEquals("", tokenizer.getToken());

    tokenizer.addLine("bbb");
    assertEquals(WORD, tokenizer.nextToken());
    assertEquals("bbb", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }

  @Test
  public void testGetToken_quote() throws Exception {
    tokenizer.addLine("'aaa'");
    assertEquals(QUOTE, tokenizer.nextToken());
    assertEquals("'aaa'", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }

  @Test
  public void testGetToken_quoteNotClosed() throws Exception {
    tokenizer.addLine("'aaa");
    assertEquals(QUOTE, tokenizer.nextToken());
    assertEquals("'aaa", tokenizer.getToken());
    assertEquals(END_OF_LINE, tokenizer.nextToken());
  }
}
