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
package org.seasar.doma.internal.expr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.message.Message;

public class ExpressionExceptionTest {

  @Test
  public void testMethodInvocationFailed() {
    ExpressionParser parser = new ExpressionParser("new java.util.ArrayList().get(0)");
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

  @Test
  public void testMethodNotFound() {
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

  @Test
  public void testVariableUnresolvable() {
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

  @Test
  public void testDoubleQuotationNotClosed() {
    ExpressionParser parser = new ExpressionParser("\"bbb\" == \"bbb");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3004, e.getMessageResource());
    }
  }

  @Test
  public void testClassNotFound() {
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

  @Test
  public void testConstructorNotFound() {
    ExpressionParser parser = new ExpressionParser("new java.lang.String(10B)");
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

  @Test
  public void testConstructorInvocationFailed() {
    ExpressionParser parser = new ExpressionParser("new java.util.ArrayList(-1)");
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

  @Test
  public void testComparisonFailed_incomparable() {
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

  @Test
  public void testComparisonFailed_null() {
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

  @Test
  public void testOperandNotFound() {
    ExpressionParser parser = new ExpressionParser("true &&");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3010, e.getMessageResource());
    }
  }

  @Test
  public void testUnsupportedTokenFound() {
    ExpressionParser parser = new ExpressionParser("5 & 5");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3011, e.getMessageResource());
    }
  }

  @Test
  public void testUnsupportedNumberLiteralFound() {
    ExpressionParser parser = new ExpressionParser("5aaa");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3012, e.getMessageResource());
    }
  }

  @Test
  public void testOperandNotNumber() {
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

  @Test
  public void testOperandNotText() {
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

  @Test
  public void testArithmeticOperationFailed() {
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

  @Test
  public void testArithmeticOperationFailed_null() {
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

  @Test
  public void testQuotationNotClosed() {
    ExpressionParser parser = new ExpressionParser(" 'aaa");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3016, e.getMessageResource());
    }
  }

  @Test
  public void testFieldNotFound() {
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
