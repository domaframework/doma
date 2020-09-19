package org.seasar.doma.internal.expr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.seasar.doma.internal.expr.ExpressionTokenType.*;

import org.junit.jupiter.api.Test;
import org.seasar.doma.message.Message;

public class ExpressionTokenizerTest {

  @Test
  public void testVariableOperand() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("name");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("name", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testStringLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("\"aaa bbb\"");
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"aaa bbb\"", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIntLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13");
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("+13", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLongLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13L");
    assertEquals(LONG_LITERAL, tokenizer.next());
    assertEquals("+13L", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFloatLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13F");
    assertEquals(FLOAT_LITERAL, tokenizer.next());
    assertEquals("+13F", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testDoubleLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13D");
    assertEquals(DOUBLE_LITERAL, tokenizer.next());
    assertEquals("+13D", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBigDecimalLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("+13B");
    assertEquals(BIGDECIMAL_LITERAL, tokenizer.next());
    assertEquals("+13B", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNullLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("null");
    assertEquals(NULL_LITERAL, tokenizer.next());
    assertEquals("null", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testTrueLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("true");
    assertEquals(TRUE_LITERAL, tokenizer.next());
    assertEquals("true", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFalseLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("false");
    assertEquals(FALSE_LITERAL, tokenizer.next());
    assertEquals("false", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testExpression() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("manager.eq(true) && name.eq(\"aaa\")");
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

  @Test
  public void testGetPosition() {
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

  @Test
  public void testFieldOperator() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("aaa.bbb");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(FIELD_OPERATOR, tokenizer.next());
    assertEquals(".bbb", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFieldOperator_identifierNotFound() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("aaa.");
    try {
      tokenizer.next();
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3021, expected.getMessageResource());
    }
  }

  @Test
  public void testFieldOperator_illegalJavaIdentifierStart() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("aaa.!");
    try {
      tokenizer.next();
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3022, expected.getMessageResource());
    }
  }

  @Test
  public void testMethodOperator() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("aaa.bbb(\"ccc\")");
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

  @Test
  public void testParens() {
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

  @Test
  public void testFunctionOperator() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("@startWith(aaa)");
    assertEquals(FUNCTION_OPERATOR, tokenizer.next());
    assertEquals("@startWith", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
  }

  @Test
  public void testBuiltinFunctionOperator_nameNotFound() {
    try {
      new ExpressionTokenizer("@");
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3023, expected.getMessageResource());
    }
  }

  @Test
  public void testBuiltinFunctionOperator_illegalJavaIdentifierStart() {
    try {
      new ExpressionTokenizer("@!");
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3024, expected.getMessageResource());
    }
  }

  @Test
  public void testStaticMethodOperator() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("@java.lang.String@valueOf(aaa)");
    assertEquals(STATIC_METHOD_OPERATOR, tokenizer.next());
    assertEquals("@java.lang.String@valueOf", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
  }

  @Test
  public void testStaticMethodOperator_simpleClassName() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("@Aaa@valueOf(aaa)");
    assertEquals(STATIC_METHOD_OPERATOR, tokenizer.next());
    assertEquals("@Aaa@valueOf", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
  }

  @Test
  public void testStaticFieldOperator() {
    ExpressionTokenizer tokenizer =
        new ExpressionTokenizer("@java.lang.String@CASE_INSENSITIVE_ORDER ");
    assertEquals(STATIC_FIELD_OPERATOR, tokenizer.next());
    assertEquals("@java.lang.String@CASE_INSENSITIVE_ORDER", tokenizer.getToken());
  }
}
