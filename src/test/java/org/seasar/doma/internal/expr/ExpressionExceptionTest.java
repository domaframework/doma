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
package org.seasar.doma.internal.expr;

import junit.framework.TestCase;

import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.message.Message;

/**
 * @author taedium
 * 
 */
public class ExpressionExceptionTest extends TestCase {

    public void testMethodInvocationFailed() throws Exception {
        ExpressionParser parser = new ExpressionParser(
                "new java.util.ArrayList().get(0)");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3001, e.getMessageResource());
        }
    }

    public void testMethodNotFound() throws Exception {
        ExpressionParser parser = new ExpressionParser("\"aaa\".bbb()");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3002, e.getMessageResource());
        }
    }

    public void testVariableUnresolvable() throws Exception {
        ExpressionParser parser = new ExpressionParser("aaa.eq(100)");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3003, e.getMessageResource());
        }
    }

    public void testDoubleQuotationNotClosed() throws Exception {
        ExpressionParser parser = new ExpressionParser("\"bbb\" == \"bbb");
        try {
            parser.parse();
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3004, e.getMessageResource());
        }
    }

    public void testClassNotFound() throws Exception {
        ExpressionParser parser = new ExpressionParser("new MyString()");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3005, e.getMessageResource());
        }
    }

    public void testConstructorNotFound() throws Exception {
        ExpressionParser parser = new ExpressionParser(
                "new java.lang.String(10B)");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3006, e.getMessageResource());
        }
    }

    public void testConstructorInvocationFailed() throws Exception {
        ExpressionParser parser = new ExpressionParser(
                "new java.util.ArrayList(-1)");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3007, e.getMessageResource());
        }
    }

    public void testComparisonFailed_incomparable() throws Exception {
        ExpressionParser parser = new ExpressionParser("1 > true");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3008, e.getMessageResource());
        }
    }

    public void testComparisonFailed_null() throws Exception {
        ExpressionParser parser = new ExpressionParser("1 > null");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3009, e.getMessageResource());
        }
    }

    public void testOperandNotFound() throws Exception {
        ExpressionParser parser = new ExpressionParser("true &&");
        try {
            parser.parse();
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3010, e.getMessageResource());
        }
    }

    public void testUnsupportedTokenFound() throws Exception {
        ExpressionParser parser = new ExpressionParser("5 & 5");
        try {
            parser.parse();
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3011, e.getMessageResource());
        }
    }

    public void testUnsupportedNumberLiteralFound() throws Exception {
        ExpressionParser parser = new ExpressionParser("5aaa");
        try {
            parser.parse();
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3012, e.getMessageResource());
        }
    }

    public void testOperandNotNumber() throws Exception {
        ExpressionParser parser = new ExpressionParser("5 + \"10\"");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3013, e.getMessageResource());
        }
    }

    public void testOperandNotText() throws Exception {
        ExpressionParser parser = new ExpressionParser("\"10\" + 5");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3020, e.getMessageResource());
        }
    }

    public void testArithmeticOperationFailed() throws Exception {
        ExpressionParser parser = new ExpressionParser("5 / 0");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3014, e.getMessageResource());
        }
    }

    public void testArithmeticOperationFailed_null() throws Exception {
        ExpressionParser parser = new ExpressionParser("5 / null");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3015, e.getMessageResource());
        }
    }

    public void testQuotationNotClosed() throws Exception {
        ExpressionParser parser = new ExpressionParser(" 'aaa");
        try {
            parser.parse();
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3016, e.getMessageResource());
        }
    }

    public void testFieldNotFound() throws Exception {
        ExpressionParser parser = new ExpressionParser("\"aaa\".bbb");
        ExpressionNode node = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        try {
            evaluator.evaluate(node);
            fail();
        } catch (ExpressionException e) {
            System.out.println(e.getMessage());
            assertEquals(Message.DOMA3018, e.getMessageResource());
        }
    }

}
