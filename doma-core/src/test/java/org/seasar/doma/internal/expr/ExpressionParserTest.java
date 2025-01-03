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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.message.Message;

public class ExpressionParserTest {

  @Test
  public void testTrue() {
    ExpressionParser parser = new ExpressionParser("true");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testFalse() {
    ExpressionParser parser = new ExpressionParser("false");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNot() {
    ExpressionParser parser = new ExpressionParser("!true");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNot2() {
    ExpressionParser parser = new ExpressionParser("!false");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testAnd() {
    ExpressionParser parser = new ExpressionParser("!false && !false");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testAnd2() {
    ExpressionParser parser = new ExpressionParser("(true || false) && (true || false)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testAnd3() {
    ExpressionParser parser = new ExpressionParser("(true || false ) && !( true || false)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testAnd4() {
    ExpressionParser parser = new ExpressionParser("(true || false ) && true");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testOr() {
    ExpressionParser parser = new ExpressionParser("false || true");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testOr2() {
    ExpressionParser parser = new ExpressionParser("false || false");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testOr44() {
    ExpressionParser parser = new ExpressionParser("false || true && false");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testOr3() {
    ExpressionParser parser = new ExpressionParser("true && true || true && true");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testOr4() {
    ExpressionParser parser = new ExpressionParser("true && false || true && true");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNoParamMethod() {
    ExpressionParser parser = new ExpressionParser("hoge.length()");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "aaa"));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals(3, evaluationResult.getValue());
  }

  @Test
  public void testMethod() {
    ExpressionParser parser = new ExpressionParser("hoge.length()");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "aaa"));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals(3, evaluationResult.getValue());
  }

  @Test
  public void testMethod2() {
    ExpressionParser parser = new ExpressionParser("hoge.substring(2, 4)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "abcdef"));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals("cd", evaluationResult.getValue());
  }

  @Test
  public void testMethod3() {
    ExpressionParser parser = new ExpressionParser("hoge.foo.substring(2, 4)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(Hoge.class, new Hoge()));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals("cd", evaluationResult.getValue());
  }

  @Test
  public void testMethod4() {
    ExpressionParser parser = new ExpressionParser("hoge.bar(2, 4).length()");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(Hoge.class, new Hoge()));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals(2, evaluationResult.getValue());
  }

  @Test
  public void testMethod5() {
    ExpressionParser parser = new ExpressionParser("hoge.bar(hoge.bar(2, 4).length(), 4).length()");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(Hoge.class, new Hoge()));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals(2, evaluationResult.getValue());
  }

  @Test
  public void testMethod6() {
    ExpressionParser parser = new ExpressionParser("bbb.method(bbb.method(bbb.value))");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Bbb bbb = new Bbb();
    bbb.value = "hoge";
    evaluator.add("bbb", new Value(Bbb.class, bbb));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals("hoge", evaluationResult.getValue());
    assertEquals(String.class, evaluationResult.getValueClass());
  }

  @Test
  public void testMethod_targetObjectIsNull() {
    ExpressionParser parser = new ExpressionParser("null.length()");
    try {
      ExpressionNode expression = parser.parse();
      ExpressionEvaluator evaluator = new ExpressionEvaluator();
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3027, expected.getMessageResource());
    }
  }

  @Test
  public void testStaticMethod() {
    ExpressionParser parser = new ExpressionParser("@java.lang.String@valueOf(1)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals("1", evaluationResult.getValue());
  }

  @Test
  public void testStaticMethod_classNotFound() {
    ExpressionParser parser = new ExpressionParser("@java.lang.Xxx@valueOf(1)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3005, expected.getMessageResource());
    }
  }

  @Test
  public void testStaticMethod_methodNotFound() {
    ExpressionParser parser = new ExpressionParser("@java.lang.String@xxx(1)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3002, expected.getMessageResource());
    }
  }

  @Test
  public void testField() {
    ExpressionParser parser = new ExpressionParser("bbb.value");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    Bbb bbb = new Bbb();
    bbb.value = "hoge";
    evaluator.add("bbb", new Value(Bbb.class, bbb));
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals("hoge", evaluationResult.getValue());
    assertEquals(String.class, evaluationResult.getValueClass());
  }

  @Test
  public void testStaticField() {
    ExpressionParser parser = new ExpressionParser("@java.lang.String@CASE_INSENSITIVE_ORDER");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals(String.CASE_INSENSITIVE_ORDER, evaluationResult.getValue());
  }

  @Test
  public void testStaticField_classNotFound() {
    ExpressionParser parser = new ExpressionParser("@java.lang.Xxx@CASE_INSENSITIVE_ORDER");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3005, expected.getMessageResource());
    }
  }

  @Test
  public void testStaticField_fieldNotFound() {
    ExpressionParser parser = new ExpressionParser("@java.lang.String@hoge");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3033, expected.getMessageResource());
    }
  }

  @Test
  public void testFunction() {
    ExpressionParser parser = new ExpressionParser("@prefix(\"aaa\")");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals("aaa", evaluationResult.getValue());
  }

  @Test
  public void testFunction_notFound() {
    ExpressionParser parser = new ExpressionParser("@hoge(\"aaa\")");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3028, expected.getMessageResource());
    }
  }

  @Test
  public void testParens_notClosed() {
    ExpressionParser parser = new ExpressionParser("hoge.bar(2, 4");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3026, expected.getMessageResource());
    }
  }

  @Test
  public void testNew() {
    ExpressionParser parser = new ExpressionParser("new java.lang.Integer(10)");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertEquals(10, evaluationResult.getValue());
  }

  @Test
  public void testEq() {
    ExpressionParser parser = new ExpressionParser("10 == 10");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNotEq() {
    ExpressionParser parser = new ExpressionParser("11 == 10");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testEq_null() {
    ExpressionParser parser = new ExpressionParser("null == null");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("null == 1");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("1 == null");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNe() {
    ExpressionParser parser = new ExpressionParser("1 != 2");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNotNe() {
    ExpressionParser parser = new ExpressionParser("11 != 11");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNe_null() {
    ExpressionParser parser = new ExpressionParser("null != null");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("null != 1");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("1 != null");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testGe() {
    ExpressionParser parser = new ExpressionParser("11 >= 10");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
    parser = new ExpressionParser("10 >= 10");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNotGe() {
    ExpressionParser parser = new ExpressionParser("9 >= 10");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testGe_null() {
    ExpressionParser parser = new ExpressionParser("null >= null");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("null >= 1");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("1 >= null");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }
  }

  @Test
  public void testLe() {
    ExpressionParser parser = new ExpressionParser("10 <= 11");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
    parser = new ExpressionParser("10 <= 10");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNotLe() {
    ExpressionParser parser = new ExpressionParser("10 <= 9");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testLe_null() {
    ExpressionParser parser = new ExpressionParser("null <= null");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("null <= 1");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("1 <= null");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }
  }

  @Test
  public void testGt() {
    ExpressionParser parser = new ExpressionParser("11 > 10");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNotGt() {
    ExpressionParser parser = new ExpressionParser("10 > 10");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("9 > 10");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testGt_null() {
    ExpressionParser parser = new ExpressionParser("null > null");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("null > 1");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("1 > null");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }
  }

  @Test
  public void testLt() {
    ExpressionParser parser = new ExpressionParser("10 < 11");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  @Test
  public void testNotLt() {
    ExpressionParser parser = new ExpressionParser("10 < 10");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("10 < 9");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  @Test
  public void testLt_null() {
    ExpressionParser parser = new ExpressionParser("null < null");
    ExpressionNode expression = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("null < 1");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }

    parser = new ExpressionParser("1 < null");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3009, expected.getMessageResource());
    }
  }

  @Test
  public void testUnsupportedToken() {
    ExpressionParser parser = new ExpressionParser("5 ? 5");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3011, expected.getMessageResource());
    }
  }

  @Test
  public void testIllegalNumberLiteral() {
    ExpressionParser parser = new ExpressionParser("2.length");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3012, expected.getMessageResource());
    }
  }

  @Test
  public void testInt() {
    ExpressionParser parser = new ExpressionParser("2");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(2, result.getValue());

    parser = new ExpressionParser("+2");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(2, result.getValue());

    parser = new ExpressionParser("-2");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(-2, result.getValue());
  }

  @Test
  public void testLong() {
    ExpressionParser parser = new ExpressionParser("2L");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(2L, result.getValue());

    parser = new ExpressionParser("+2L");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(2L, result.getValue());

    parser = new ExpressionParser("-2L");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals((long) -2, result.getValue());
  }

  @Test
  public void testFloat() {
    ExpressionParser parser = new ExpressionParser("2.5F");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(2.5f, result.getValue());

    parser = new ExpressionParser("+2.5F");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(2.5f, result.getValue());

    parser = new ExpressionParser("-2.5F");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals((float) -2.5, result.getValue());
  }

  @Test
  public void testDouble() {
    ExpressionParser parser = new ExpressionParser("2.5D");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(2.5, result.getValue());

    parser = new ExpressionParser("+2.5D");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(2.5, result.getValue());

    parser = new ExpressionParser("-2.5D");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(-2.5, result.getValue());
  }

  @Test
  public void testBigDecimal() {
    ExpressionParser parser = new ExpressionParser("2.5B");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(new BigDecimal("2.5"), result.getValue());

    parser = new ExpressionParser("+2.5B");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(new BigDecimal("2.5"), result.getValue());

    parser = new ExpressionParser("-2.5B");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(BigDecimal.valueOf(-2.5), result.getValue());
  }

  @Test
  public void testChar() {
    ExpressionParser parser = new ExpressionParser("'a'");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals('a', result.getValue());
  }

  @Test
  public void testAdd() {
    ExpressionParser parser = new ExpressionParser("1 + 1B");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(2), result.getValue());
  }

  @Test
  public void testSubtract() {
    ExpressionParser parser = new ExpressionParser("10 - 2B");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(8), result.getValue());
  }

  @Test
  public void testMultiply() {
    ExpressionParser parser = new ExpressionParser("10 * 2B");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(20), result.getValue());
  }

  @Test
  public void testDivide() {
    ExpressionParser parser = new ExpressionParser("10 / 2B");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(5), result.getValue());
  }

  @Test
  public void testMod() {
    ExpressionParser parser = new ExpressionParser("10 % 7B");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(3), result.getValue());
  }

  @Test
  public void testArithmeticOperators() {
    ExpressionParser parser = new ExpressionParser("5 + 3 * 4 - 9 / 3");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(14, result.getValue());
  }

  @Test
  public void testArithmeticOperators2() {
    ExpressionParser parser = new ExpressionParser("5+3*4-9/3");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(14, result.getValue());
  }

  @Test
  public void testArithmeticOperators3() {
    ExpressionParser parser = new ExpressionParser("1+-2*+2");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals(-3, result.getValue());
  }

  @Test
  public void testConcat() {
    ExpressionParser parser = new ExpressionParser("\"ab\" + \"cd\" + 'e'");
    ExpressionNode node = parser.parse();
    ExpressionEvaluator evaluator = new ExpressionEvaluator();
    EvaluationResult result = evaluator.evaluate(node);
    assertEquals("abcde", result.getValue());
  }

  public static class Hoge {

    private final String foo = "abcdef";

    public String foo() {
      return foo;
    }

    public String bar(int start, int end) {
      return foo.substring(start, end);
    }
  }

  public static class Aaa<T> {
    public T value;

    public T method(T value) {
      return value;
    }
  }

  public static class Bbb extends Aaa<String> {}
}
