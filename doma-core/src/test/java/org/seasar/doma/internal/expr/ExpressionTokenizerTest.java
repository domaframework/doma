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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.seasar.doma.internal.expr.ExpressionTokenType.*;

import org.junit.jupiter.api.Disabled;
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

  @Test
  public void testCharLiteral() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("'a'");
    assertEquals(CHAR_LITERAL, tokenizer.next());
    assertEquals("'a'", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Disabled
  @Test
  // escape char is not supported
  public void testCharLiteral_escape() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("'\\n'");
    assertEquals(CHAR_LITERAL, tokenizer.next());
    assertEquals("'\\n'", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOperators() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("!x && y || z");
    assertEquals(NOT_OPERATOR, tokenizer.next());
    assertEquals("!", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("x", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(AND_OPERATOR, tokenizer.next());
    assertEquals("&&", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("y", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OR_OPERATOR, tokenizer.next());
    assertEquals("||", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("z", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testArithmeticOperators() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("a + b - c * d / e % f");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("a", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(ADD_OPERATOR, tokenizer.next());
    assertEquals("+", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("b", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(SUBTRACT_OPERATOR, tokenizer.next());
    assertEquals("-", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("c", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(MULTIPLY_OPERATOR, tokenizer.next());
    assertEquals("*", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("d", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(DIVIDE_OPERATOR, tokenizer.next());
    assertEquals("/", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("e", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(MOD_OPERATOR, tokenizer.next());
    assertEquals("%", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("f", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testComparisonOperators() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("a == b != c > d < e >= f <= g");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("a", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(EQ_OPERATOR, tokenizer.next());
    assertEquals("==", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("b", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(NE_OPERATOR, tokenizer.next());
    assertEquals("!=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("c", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(GT_OPERATOR, tokenizer.next());
    assertEquals(">", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("d", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(LT_OPERATOR, tokenizer.next());
    assertEquals("<", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("e", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(GE_OPERATOR, tokenizer.next());
    assertEquals(">=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("f", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(LE_OPERATOR, tokenizer.next());
    assertEquals("<=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("g", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNewOperator() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("new java.util.Date()");
    assertEquals(NEW_OPERATOR, tokenizer.next());
    assertEquals("new", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("java", tokenizer.getToken());
    assertEquals(FIELD_OPERATOR, tokenizer.next());
    assertEquals(".util", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".Date", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeNumbers() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("-123");
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("-123", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeFloat() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("-123.45F");
    assertEquals(FLOAT_LITERAL, tokenizer.next());
    assertEquals("-123.45F", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeDouble() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("-123.45D");
    assertEquals(DOUBLE_LITERAL, tokenizer.next());
    assertEquals("-123.45D", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeLong() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("-123L");
    assertEquals(LONG_LITERAL, tokenizer.next());
    assertEquals("-123L", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeBigDecimal() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("-123.45B");
    assertEquals(BIGDECIMAL_LITERAL, tokenizer.next());
    assertEquals("-123.45B", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testMultipleWhitespace() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("a    b\t\tc\n\nd");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("a", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("b", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\t", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\t", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("c", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("d", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testStringWithEscapes() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("\"Hello\\nWorld\\t\"");
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"Hello\\nWorld\\t\"", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testEmptyString() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("\"\"");
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"\"", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testEmptyExpression() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("");
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testWhitespaceOnly() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("   \t\n  ");
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\t", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals("\n", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNestedParentheses() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("((a))");
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("a", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testMethodChaining() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("a.b().c().d");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("a", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".b", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".c", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(FIELD_OPERATOR, tokenizer.next());
    assertEquals(".d", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testComplexNestedExpression() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("(a.eq(1) && b.ne(null)) || (c > 0)");
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("a", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".eq", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("1", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(AND_OPERATOR, tokenizer.next());
    assertEquals("&&", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("b", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".ne", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(NULL_LITERAL, tokenizer.next());
    assertEquals("null", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OR_OPERATOR, tokenizer.next());
    assertEquals("||", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("c", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(GT_OPERATOR, tokenizer.next());
    assertEquals(">", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("0", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testZeroValues() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("0 0L 0F 0D 0B");
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("0", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(LONG_LITERAL, tokenizer.next());
    assertEquals("0L", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(FLOAT_LITERAL, tokenizer.next());
    assertEquals("0F", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(DOUBLE_LITERAL, tokenizer.next());
    assertEquals("0D", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(BIGDECIMAL_LITERAL, tokenizer.next());
    assertEquals("0B", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testJavaIdentifierVariations() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("_var $var var123 _123 $123");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("_var", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("$var", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("var123", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("_123", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("$123", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFunctionWithComplexArguments() {
    ExpressionTokenizer tokenizer = new ExpressionTokenizer("@func(a.b, 123, \"test\", true)");
    assertEquals(FUNCTION_OPERATOR, tokenizer.next());
    assertEquals("@func", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("a", tokenizer.getToken());
    assertEquals(FIELD_OPERATOR, tokenizer.next());
    assertEquals(".b", tokenizer.getToken());
    assertEquals(COMMA_OPERATOR, tokenizer.next());
    assertEquals(",", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("123", tokenizer.getToken());
    assertEquals(COMMA_OPERATOR, tokenizer.next());
    assertEquals(",", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"test\"", tokenizer.getToken());
    assertEquals(COMMA_OPERATOR, tokenizer.next());
    assertEquals(",", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(TRUE_LITERAL, tokenizer.next());
    assertEquals("true", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }
}
