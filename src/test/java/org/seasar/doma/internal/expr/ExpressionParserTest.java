package org.seasar.doma.internal.expr;

import java.math.BigDecimal;
import junit.framework.TestCase;
import org.seasar.doma.message.Message;

public class ExpressionParserTest extends TestCase {

  public void testTrue() throws Exception {
    var parser = new ExpressionParser("true");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testFalse() throws Exception {
    var parser = new ExpressionParser("false");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testNot() throws Exception {
    var parser = new ExpressionParser("!true");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testNot2() throws Exception {
    var parser = new ExpressionParser("!false");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testAnd() throws Exception {
    var parser = new ExpressionParser("!false && !false");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testAnd2() throws Exception {
    var parser = new ExpressionParser("(true || false) && (true || false)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testAnd3() throws Exception {
    var parser = new ExpressionParser("(true || false ) && !( true || false)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testAnd4() throws Exception {
    var parser = new ExpressionParser("(true || false ) && true");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testOr() throws Exception {
    var parser = new ExpressionParser("false || true");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testOr2() throws Exception {
    var parser = new ExpressionParser("false || false");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testOr44() throws Exception {
    var parser = new ExpressionParser("false || true && false");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testOr3() throws Exception {
    var parser = new ExpressionParser("true && true || true && true");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testOr4() throws Exception {
    var parser = new ExpressionParser("true && false || true && true");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testNoParamMethod() throws Exception {
    var parser = new ExpressionParser("hoge.length()");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "aaa"));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals(3, evaluationResult.getValue());
  }

  public void testMethod() throws Exception {
    var parser = new ExpressionParser("hoge.length()");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "aaa"));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals(3, evaluationResult.getValue());
  }

  public void testMethod2() throws Exception {
    var parser = new ExpressionParser("hoge.substring(2, 4)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(String.class, "abcdef"));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals("cd", evaluationResult.getValue());
  }

  public void testMethod3() throws Exception {
    var parser = new ExpressionParser("hoge.foo.substring(2, 4)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(Hoge.class, new Hoge()));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals("cd", evaluationResult.getValue());
  }

  public void testMethod4() throws Exception {
    var parser = new ExpressionParser("hoge.bar(2, 4).length()");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(Hoge.class, new Hoge()));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals(2, evaluationResult.getValue());
  }

  public void testMethod5() throws Exception {
    var parser = new ExpressionParser("hoge.bar(hoge.bar(2, 4).length(), 4).length()");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    evaluator.add("hoge", new Value(Hoge.class, new Hoge()));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals(2, evaluationResult.getValue());
  }

  public void testMethod6() throws Exception {
    var parser = new ExpressionParser("bbb.method(bbb.method(bbb.value))");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var bbb = new Bbb();
    bbb.value = "hoge";
    evaluator.add("bbb", new Value(Bbb.class, bbb));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals("hoge", evaluationResult.getValue());
    assertEquals(String.class, evaluationResult.getValueClass());
  }

  public void testMethod_targetObjectIsNull() throws Exception {
    var parser = new ExpressionParser("null.length()");
    try {
      var expression = parser.parse();
      var evaluator = new ExpressionEvaluator();
      evaluator.evaluate(expression);
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3027, expected.getMessageResource());
    }
  }

  public void testStatictMethod() throws Exception {
    var parser = new ExpressionParser("@java.lang.String@valueOf(1)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals("1", evaluationResult.getValue());
  }

  public void testStatictMethod_classNotFound() throws Exception {
    var parser = new ExpressionParser("@java.lang.Xxx@valueOf(1)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3005, expected.getMessageResource());
    }
  }

  public void testStatictMethod_methodNotFound() throws Exception {
    var parser = new ExpressionParser("@java.lang.String@xxx(1)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3002, expected.getMessageResource());
    }
  }

  public void testField() throws Exception {
    var parser = new ExpressionParser("bbb.value");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var bbb = new Bbb();
    bbb.value = "hoge";
    evaluator.add("bbb", new Value(Bbb.class, bbb));
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals("hoge", evaluationResult.getValue());
    assertEquals(String.class, evaluationResult.getValueClass());
  }

  public void testStatictField() throws Exception {
    var parser = new ExpressionParser("@java.lang.String@CASE_INSENSITIVE_ORDER");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals(String.CASE_INSENSITIVE_ORDER, evaluationResult.getValue());
  }

  public void testStatictField_classNotFound() throws Exception {
    var parser = new ExpressionParser("@java.lang.Xxx@CASE_INSENSITIVE_ORDER");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3005, expected.getMessageResource());
    }
  }

  public void testStatictField_fieldNotFound() throws Exception {
    var parser = new ExpressionParser("@java.lang.String@hoge");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3033, expected.getMessageResource());
    }
  }

  public void testFunction() throws Exception {
    var parser = new ExpressionParser("@prefix(\"aaa\")");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals("aaa", evaluationResult.getValue());
  }

  public void testFunction_notFound() throws Exception {
    var parser = new ExpressionParser("@hoge(\"aaa\")");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(expression);
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3028, expected.getMessageResource());
    }
  }

  public void testParens_notClosed() throws Exception {
    var parser = new ExpressionParser("hoge.bar(2, 4");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3026, expected.getMessageResource());
    }
  }

  public void testNew() throws Exception {
    var parser = new ExpressionParser("new java.lang.Integer(10)");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertEquals(10, evaluationResult.getValue());
  }

  public void testEq() throws Exception {
    var parser = new ExpressionParser("10 == 10");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testNotEq() throws Exception {
    var parser = new ExpressionParser("11 == 10");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testEq_null() throws Exception {
    var parser = new ExpressionParser("null == null");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
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

  public void testNe() throws Exception {
    var parser = new ExpressionParser("1 != 2");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testNotNe() throws Exception {
    var parser = new ExpressionParser("11 != 11");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testNe_null() throws Exception {
    var parser = new ExpressionParser("null != null");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
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

  public void testGe() throws Exception {
    var parser = new ExpressionParser("11 >= 10");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
    parser = new ExpressionParser("10 >= 10");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testNotGe() throws Exception {
    var parser = new ExpressionParser("9 >= 10");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testGe_null() throws Exception {
    var parser = new ExpressionParser("null >= null");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
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

  public void testLe() throws Exception {
    var parser = new ExpressionParser("10 <= 11");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
    parser = new ExpressionParser("10 <= 10");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testNotLe() throws Exception {
    var parser = new ExpressionParser("10 <= 9");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testLe_null() throws Exception {
    var parser = new ExpressionParser("null <= null");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
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

  public void testGt() throws Exception {
    var parser = new ExpressionParser("11 > 10");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testNotGt() throws Exception {
    var parser = new ExpressionParser("10 > 10");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("9 > 10");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testGt_null() throws Exception {
    var parser = new ExpressionParser("null > null");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
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

  public void testLt() throws Exception {
    var parser = new ExpressionParser("10 < 11");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertTrue(evaluationResult.getBooleanValue());
  }

  public void testNotLt() throws Exception {
    var parser = new ExpressionParser("10 < 10");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());

    parser = new ExpressionParser("10 < 9");
    expression = parser.parse();
    evaluator = new ExpressionEvaluator();
    evaluationResult = evaluator.evaluate(expression);
    assertFalse(evaluationResult.getBooleanValue());
  }

  public void testLt_null() throws Exception {
    var parser = new ExpressionParser("null < null");
    var expression = parser.parse();
    var evaluator = new ExpressionEvaluator();
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

  public void testUnsupportedToken() throws Exception {
    var parser = new ExpressionParser("5 ? 5");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3011, expected.getMessageResource());
    }
  }

  public void testIllegalNumberLiteral() throws Exception {
    var parser = new ExpressionParser("2.length");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException expected) {
      assertEquals(Message.DOMA3012, expected.getMessageResource());
    }
  }

  public void testInt() throws Exception {
    var parser = new ExpressionParser("2");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
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

  public void testLong() throws Exception {
    var parser = new ExpressionParser("2L");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
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

  public void testFloat() throws Exception {
    var parser = new ExpressionParser("2.5F");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
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
    assertEquals(-2.5f, result.getValue());
  }

  public void testDouble() throws Exception {
    var parser = new ExpressionParser("2.5D");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
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

  public void testBigDecimal() throws Exception {
    var parser = new ExpressionParser("2.5B");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(2.5), result.getValue());

    parser = new ExpressionParser("+2.5B");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(2.5), result.getValue());

    parser = new ExpressionParser("-2.5B");
    node = parser.parse();
    evaluator = new ExpressionEvaluator();
    result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(-2.5), result.getValue());
  }

  public void testChar() throws Exception {
    var parser = new ExpressionParser("'a'");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals('a', result.getValue());
  }

  public void testAdd() throws Exception {
    var parser = new ExpressionParser("1 + 1B");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(2), result.getValue());
  }

  public void testSubtract() throws Exception {
    var parser = new ExpressionParser("10 - 2B");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(8), result.getValue());
  }

  public void testMultiply() throws Exception {
    var parser = new ExpressionParser("10 * 2B");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(20), result.getValue());
  }

  public void testDivide() throws Exception {
    var parser = new ExpressionParser("10 / 2B");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(5), result.getValue());
  }

  public void testMod() throws Exception {
    var parser = new ExpressionParser("10 % 7B");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(new BigDecimal(3), result.getValue());
  }

  public void testArithmeticOperators() throws Exception {
    var parser = new ExpressionParser("5 + 3 * 4 - 9 / 3");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(14, result.getValue());
  }

  public void testArithmeticOperators2() throws Exception {
    var parser = new ExpressionParser("5+3*4-9/3");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(14, result.getValue());
  }

  public void testArithmeticOperators3() throws Exception {
    var parser = new ExpressionParser("1+-2*+2");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals(-3, result.getValue());
  }

  public void testConcat() throws Exception {
    var parser = new ExpressionParser("\"ab\" + \"cd\" + 'e'");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    var result = evaluator.evaluate(node);
    assertEquals("abcde", result.getValue());
  }

  public class Hoge {

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
