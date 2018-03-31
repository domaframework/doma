package org.seasar.doma.internal.expr;

import static org.seasar.doma.internal.expr.ExpressionTokenType.AND_OPERATOR;
import static org.seasar.doma.internal.expr.ExpressionTokenType.BIGDECIMAL_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.CLOSED_PARENS;
import static org.seasar.doma.internal.expr.ExpressionTokenType.COMMA_OPERATOR;
import static org.seasar.doma.internal.expr.ExpressionTokenType.DOUBLE_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.EOE;
import static org.seasar.doma.internal.expr.ExpressionTokenType.FALSE_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.FIELD_OPERATOR;
import static org.seasar.doma.internal.expr.ExpressionTokenType.FLOAT_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.FUNCTION_OPERATOR;
import static org.seasar.doma.internal.expr.ExpressionTokenType.INT_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.LONG_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.METHOD_OPERATOR;
import static org.seasar.doma.internal.expr.ExpressionTokenType.NULL_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.OPENED_PARENS;
import static org.seasar.doma.internal.expr.ExpressionTokenType.STATIC_FIELD_OPERATOR;
import static org.seasar.doma.internal.expr.ExpressionTokenType.STATIC_METHOD_OPERATOR;
import static org.seasar.doma.internal.expr.ExpressionTokenType.STRING_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.TRUE_LITERAL;
import static org.seasar.doma.internal.expr.ExpressionTokenType.VARIABLE;
import static org.seasar.doma.internal.expr.ExpressionTokenType.WHITESPACE;

import junit.framework.TestCase;
import org.seasar.doma.message.Message;

public class ExpressionTokenizerTest extends TestCase {

  public void testVariableOperand() throws Exception {
    var tokenizer = new ExpressionTokenizer("name");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("name", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testStringLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("\"aaa bbb\"");
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"aaa bbb\"", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testIntLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("+13");
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("+13", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testLongLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("+13L");
    assertEquals(LONG_LITERAL, tokenizer.next());
    assertEquals("+13L", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testFloatLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("+13F");
    assertEquals(FLOAT_LITERAL, tokenizer.next());
    assertEquals("+13F", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testDoubleLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("+13D");
    assertEquals(DOUBLE_LITERAL, tokenizer.next());
    assertEquals("+13D", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testBigDecimalLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("+13B");
    assertEquals(BIGDECIMAL_LITERAL, tokenizer.next());
    assertEquals("+13B", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testNullLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("null");
    assertEquals(NULL_LITERAL, tokenizer.next());
    assertEquals("null", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testTrueLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("true");
    assertEquals(TRUE_LITERAL, tokenizer.next());
    assertEquals("true", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testFalseLiteral() throws Exception {
    var tokenizer = new ExpressionTokenizer("false");
    assertEquals(FALSE_LITERAL, tokenizer.next());
    assertEquals("false", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testExpression() throws Exception {
    var tokenizer = new ExpressionTokenizer("manager.eq(true) && name.eq(\"aaa\")");
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
    var tokenizer = new ExpressionTokenizer("aaa bbb ccc");
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

  public void testFieldOperator() throws Exception {
    var tokenizer = new ExpressionTokenizer("aaa.bbb");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(FIELD_OPERATOR, tokenizer.next());
    assertEquals(".bbb", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  public void testFieldOperator_identifierNotFound() throws Exception {
    var tokenizer = new ExpressionTokenizer("aaa.");
    try {
      tokenizer.next();
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3021, expected.getMessageResource());
    }
  }

  public void testFieldOperator_illegalJavaIdentifierStart() throws Exception {
    var tokenizer = new ExpressionTokenizer("aaa.!");
    try {
      tokenizer.next();
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3022, expected.getMessageResource());
    }
  }

  public void testMethodOperator() throws Exception {
    var tokenizer = new ExpressionTokenizer("aaa.bbb(\"ccc\")");
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
    var tokenizer = new ExpressionTokenizer("aaa.bbb(2, 3)");
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

  public void testFunctionOperator() throws Exception {
    var tokenizer = new ExpressionTokenizer("@startWith(aaa)");
    assertEquals(FUNCTION_OPERATOR, tokenizer.next());
    assertEquals("@startWith", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
  }

  public void testBuiltinFunctionOperator_nameNotFound() throws Exception {
    try {
      new ExpressionTokenizer("@");
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3023, expected.getMessageResource());
    }
  }

  public void testBuiltinFunctionOperator_illegalJavaIdentifierStart() throws Exception {
    try {
      new ExpressionTokenizer("@!");
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3024, expected.getMessageResource());
    }
  }

  public void testStaticMethodOperator() throws Exception {
    var tokenizer = new ExpressionTokenizer("@java.lang.String@valueOf(aaa)");
    assertEquals(STATIC_METHOD_OPERATOR, tokenizer.next());
    assertEquals("@java.lang.String@valueOf", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
  }

  public void testStaticMethodOperator_simpleClassName() throws Exception {
    var tokenizer = new ExpressionTokenizer("@Aaa@valueOf(aaa)");
    assertEquals(STATIC_METHOD_OPERATOR, tokenizer.next());
    assertEquals("@Aaa@valueOf", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
  }

  public void testStaticFieldOperator() throws Exception {
    var tokenizer = new ExpressionTokenizer("@java.lang.String@CASE_INSENSITIVE_ORDER ");
    assertEquals(STATIC_FIELD_OPERATOR, tokenizer.next());
    assertEquals("@java.lang.String@CASE_INSENSITIVE_ORDER", tokenizer.getToken());
  }
}
