/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.jdbc.sql.SqlTokenType.*;
import junit.framework.TestCase;

import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class SqlTokenizerTest extends TestCase {

    private String lineSeparator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        lineSeparator = System.getProperty("line.separator");
        System.setProperty("line.separator", "\r\n");
    }

    @Override
    protected void tearDown() throws Exception {
        System.setProperty("line.separator", lineSeparator);
        super.tearDown();
    }

    public void testEof() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testDelimiter() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where;");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        assertEquals(DELIMITER, tokenizer.next());
        assertEquals(";", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testLineComment() throws Exception {
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

    public void testBlockComment() throws Exception {
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

    public void testBlockComment_empty() throws Exception {
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

    public void testQuote() throws Exception {
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

    public void testQuote_escaped() throws Exception {
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

    public void testQuote_notClosed() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where 'aaa");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        try {
            tokenizer.next();
            fail();
        } catch (JdbcException expected) {
        }
    }

    public void testQuote_escaped_notClosed() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where 'aaa''bbb''");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        try {
            tokenizer.next();
            fail();
        } catch (JdbcException expected) {
        }
    }

    public void testUnion() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("union");
        assertEquals(UNION_WORD, tokenizer.next());
        assertEquals("union", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testExcept() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("except");
        assertEquals(EXCEPT_WORD, tokenizer.next());
        assertEquals("except", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testMinus() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("minus");
        assertEquals(MINUS_WORD, tokenizer.next());
        assertEquals("minus", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testIntersect() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("intersect");
        assertEquals(INTERSECT_WORD, tokenizer.next());
        assertEquals("intersect", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testSelect() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("select");
        assertEquals(SELECT_WORD, tokenizer.next());
        assertEquals("select", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testFrom() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("from");
        assertEquals(FROM_WORD, tokenizer.next());
        assertEquals("from", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testWhere() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testGroupBy() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("group by");
        assertEquals(GROUP_BY_WORD, tokenizer.next());
        assertEquals("group by", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testHaving() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("having");
        assertEquals(HAVING_WORD, tokenizer.next());
        assertEquals("having", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testOrderBy() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("order by");
        assertEquals(ORDER_BY_WORD, tokenizer.next());
        assertEquals("order by", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testForUpdateBy() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("for update");
        assertEquals(FOR_UPDATE_WORD, tokenizer.next());
        assertEquals("for update", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testAnd() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("and");
        assertEquals(AND_WORD, tokenizer.next());
        assertEquals("and", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testOr() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("or");
        assertEquals(OR_WORD, tokenizer.next());
        assertEquals("or", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testBindBlockComment() throws Exception {
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

    public void testBindBlockComment_followingQuote() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer(
                "where /*aaa*/'2001-01-01 12:34:56'");
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

    public void testBindBlockComment_followingWordAndQuote() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer(
                "where /*aaa*/timestamp'2001-01-01 12:34:56' and");
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

    public void testBindBlockComment_spaceIncluded() throws Exception {
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

    public void testBindBlockComment_startWithStringLiteral() throws Exception {
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

    public void testBindBlockComment_startWithCharLiteral() throws Exception {
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

    public void testIfBlockComment() throws Exception {
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

    public void testElseifLineComment() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where --elseif true--bbb");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(ELSEIF_LINE_COMMENT, tokenizer.next());
        assertEquals("--elseif true--", tokenizer.getToken());
        assertEquals(WORD, tokenizer.next());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testElseLineComment() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where --else bbb");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(ELSE_LINE_COMMENT, tokenizer.next());
        assertEquals("--else", tokenizer.getToken());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(WORD, tokenizer.next());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(EOF, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testForBlockComment() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer(
                "where /*%for element : list*/bbb");
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

    public void testEndBlockComment() throws Exception {
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

    public void testLineNumber() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer(
                "aaa\nbbb\nccc\n/* \nddd\n */");
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

    public void testColumnNumber() throws Exception {
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

    public void testIllegalDirective() throws Exception {
        SqlTokenizer tokenizer = new SqlTokenizer("where /*%*/bbb");
        assertEquals(WHERE_WORD, tokenizer.next());
        assertEquals("where", tokenizer.getToken());
        try {
            tokenizer.next();
            fail();
        } catch (JdbcException expected) {
            System.out.println(expected);
            assertEquals(Message.DOMA2119, expected.getMessageResource());
        }
    }
}
