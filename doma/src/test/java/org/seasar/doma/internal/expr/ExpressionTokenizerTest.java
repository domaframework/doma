/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.expr.ExpressionTokenType.*;

import org.seasar.doma.internal.expr.ExpressionTokenizer;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class ExpressionTokenizerTest extends TestCase {

    public void testVariableOperand() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("name");
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("name", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testStringLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("\"aaa bbb\"");
        assertEquals(STRING_LITERAL, tokenizer.next());
        assertEquals("\"aaa bbb\"", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testIntLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13");
        assertEquals(INT_LITERAL, tokenizer.next());
        assertEquals("+13", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testLongLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13L");
        assertEquals(LONG_LITERAL, tokenizer.next());
        assertEquals("+13L", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testFloatLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13F");
        assertEquals(FLOAT_LITERAL, tokenizer.next());
        assertEquals("+13F", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testDoubleLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13D");
        assertEquals(DOUBLE_LITERAL, tokenizer.next());
        assertEquals("+13D", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testBigDecimalLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13B");
        assertEquals(BIGDECIMAL_LITERAL, tokenizer.next());
        assertEquals("+13B", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testNullLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("null");
        assertEquals(NULL_LITERAL, tokenizer.next());
        assertEquals("null", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testTrueLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("true");
        assertEquals(TRUE_LITERAL, tokenizer.next());
        assertEquals("true", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testFalseLiteral() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("false");
        assertEquals(FALSE_LITERAL, tokenizer.next());
        assertEquals("false", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testExpression() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer(
                "manager.eq(true) && name.eq(\"aaa\")");
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("manager", tokenizer.getToken());
        assertEquals(METHOD_OPERATOR, tokenizer.next());
        assertEquals(".eq", tokenizer.getToken());
        assertEquals(OPENED_PARENS, tokenizer.next());
        assertEquals("(", tokenizer.getToken());
        assertEquals(TRUE_LITERAL, tokenizer.next());
        assertEquals("true", tokenizer.getToken());
        assertEquals(CLOSED_PARENS, tokenizer.next());
        assertEquals(")", tokenizer.getToken());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(AND_OPERATOR, tokenizer.next());
        assertEquals("&&", tokenizer.getToken());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("name", tokenizer.getToken());
        assertEquals(METHOD_OPERATOR, tokenizer.next());
        assertEquals(".eq", tokenizer.getToken());
        assertEquals(OPENED_PARENS, tokenizer.next());
        assertEquals("(", tokenizer.getToken());
        assertEquals(STRING_LITERAL, tokenizer.next());
        assertEquals("\"aaa\"", tokenizer.getToken());
        assertEquals(CLOSED_PARENS, tokenizer.next());
        assertEquals(")", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testGetPosition() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("aaa bbb ccc");
        assertEquals(0, tokenizer.getPosition());
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(3, tokenizer.getPosition());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(4, tokenizer.getPosition());
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("bbb", tokenizer.getToken());
        assertEquals(7, tokenizer.getPosition());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(8, tokenizer.getPosition());
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("ccc", tokenizer.getToken());
        assertEquals(11, tokenizer.getPosition());
    }

    public void testNoParamMethodOperator() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("aaa.bbb");
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(NO_PARAM_METHOD_OPERATOR, tokenizer.next());
        assertEquals(".bbb", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testMethodOperator() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer(
                "aaa.bbb(\"ccc\")");
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(METHOD_OPERATOR, tokenizer.next());
        assertEquals(".bbb", tokenizer.getToken());
        assertEquals(OPENED_PARENS, tokenizer.next());
        assertEquals("(", tokenizer.getToken());
        assertEquals(STRING_LITERAL, tokenizer.next());
        assertEquals("\"ccc\"", tokenizer.getToken());
        assertEquals(CLOSED_PARENS, tokenizer.next());
        assertEquals(")", tokenizer.getToken());
        assertEquals(EOE, tokenizer.next());
        assertNull(tokenizer.getToken());
    }

    public void testParans() throws Exception {
        ExpressionTokenizer tokenizer = new ExpressionTokenizer("aaa.bbb(2, 3)");
        assertEquals(VARIABLE, tokenizer.next());
        assertEquals("aaa", tokenizer.getToken());
        assertEquals(METHOD_OPERATOR, tokenizer.next());
        assertEquals(".bbb", tokenizer.getToken());
        assertEquals(OPENED_PARENS, tokenizer.next());
        assertEquals("(", tokenizer.getToken());
        assertEquals(INT_LITERAL, tokenizer.next());
        assertEquals("2", tokenizer.getToken());
        assertEquals(COMMA_OPERATOR, tokenizer.next());
        assertEquals(",", tokenizer.getToken());
        assertEquals(WHITESPACE, tokenizer.next());
        assertEquals(" ", tokenizer.getToken());
        assertEquals(INT_LITERAL, tokenizer.next());
        assertEquals("3", tokenizer.getToken());
        assertEquals(CLOSED_PARENS, tokenizer.next());
        assertEquals(")", tokenizer.getToken());
    }
}
