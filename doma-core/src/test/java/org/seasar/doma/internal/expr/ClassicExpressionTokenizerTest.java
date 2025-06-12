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

public class ClassicExpressionTokenizerTest {

  @Test
  public void testVariableOperand() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("name");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("name", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testStringLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("\"aaa bbb\"");
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"aaa bbb\"", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testIntLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("+13");
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("+13", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testLongLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("+13L");
    assertEquals(LONG_LITERAL, tokenizer.next());
    assertEquals("+13L", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFloatLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("+13F");
    assertEquals(FLOAT_LITERAL, tokenizer.next());
    assertEquals("+13F", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testDoubleLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("+13D");
    assertEquals(DOUBLE_LITERAL, tokenizer.next());
    assertEquals("+13D", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testBigDecimalLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("+13B");
    assertEquals(BIGDECIMAL_LITERAL, tokenizer.next());
    assertEquals("+13B", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNullLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("null");
    assertEquals(NULL_LITERAL, tokenizer.next());
    assertEquals("null", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testTrueLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("true");
    assertEquals(TRUE_LITERAL, tokenizer.next());
    assertEquals("true", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFalseLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("false");
    assertEquals(FALSE_LITERAL, tokenizer.next());
    assertEquals("false", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testExpression() {
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("manager.eq(true) && name.eq(\"aaa\")");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("aaa bbb ccc");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("aaa.bbb");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("aaa", tokenizer.getToken());
    assertEquals(FIELD_OPERATOR, tokenizer.next());
    assertEquals(".bbb", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testFieldOperator_identifierNotFound() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("aaa.");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("aaa.!");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("aaa.bbb(\"ccc\")");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("aaa.bbb(2, 3)");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("@startWith(aaa)");
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
      new ClassicExpressionTokenizer("@");
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3023, expected.getMessageResource());
    }
  }

  @Test
  public void testBuiltinFunctionOperator_illegalJavaIdentifierStart() {
    try {
      new ClassicExpressionTokenizer("@!");
      fail();
    } catch (ExpressionException expected) {
      System.out.println(expected.getMessage());
      assertEquals(Message.DOMA3024, expected.getMessageResource());
    }
  }

  @Test
  public void testStaticMethodOperator() {
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("@java.lang.String@valueOf(aaa)");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("@Aaa@valueOf(aaa)");
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
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("@java.lang.String@CASE_INSENSITIVE_ORDER ");
    assertEquals(STATIC_FIELD_OPERATOR, tokenizer.next());
    assertEquals("@java.lang.String@CASE_INSENSITIVE_ORDER", tokenizer.getToken());
  }

  @Test
  public void testCharLiteral() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("'a'");
    assertEquals(CHAR_LITERAL, tokenizer.next());
    assertEquals("'a'", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Disabled
  @Test
  // escape char is not supported
  public void testCharLiteral_escape() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("'\\n'");
    assertEquals(CHAR_LITERAL, tokenizer.next());
    assertEquals("'\\n'", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testOperators() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("!x && y || z");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("a + b - c * d / e % f");
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
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("a == b != c > d < e >= f <= g");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("new java.util.Date()");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("-123");
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("-123", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeFloat() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("-123.45F");
    assertEquals(FLOAT_LITERAL, tokenizer.next());
    assertEquals("-123.45F", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeDouble() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("-123.45D");
    assertEquals(DOUBLE_LITERAL, tokenizer.next());
    assertEquals("-123.45D", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeLong() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("-123L");
    assertEquals(LONG_LITERAL, tokenizer.next());
    assertEquals("-123L", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testNegativeBigDecimal() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("-123.45B");
    assertEquals(BIGDECIMAL_LITERAL, tokenizer.next());
    assertEquals("-123.45B", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testMultipleWhitespace() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("a    b\t\tc\n\nd");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("\"Hello\\nWorld\\t\"");
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"Hello\\nWorld\\t\"", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testEmptyString() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("\"\"");
    assertEquals(STRING_LITERAL, tokenizer.next());
    assertEquals("\"\"", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testEmptyExpression() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("");
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }

  @Test
  public void testWhitespaceOnly() {
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("   \t\n  ");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("((a))");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("a.b().c().d");
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
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("(a.eq(1) && b.ne(null)) || (c > 0)");
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
    ClassicExpressionTokenizer tokenizer = new ClassicExpressionTokenizer("0 0L 0F 0D 0B");
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
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("_var $var var123 _123 $123");
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
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("@func(a.b, 123, \"test\", true)");
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

  @Test
  public void testComplexMixedExpression() {
    ClassicExpressionTokenizer tokenizer =
        new ClassicExpressionTokenizer("user.getName().length() > 0 && user.getAge() >= 18");
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("user", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".getName", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".length", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(GT_OPERATOR, tokenizer.next());
    assertEquals(">", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("0", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(AND_OPERATOR, tokenizer.next());
    assertEquals("&&", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(VARIABLE, tokenizer.next());
    assertEquals("user", tokenizer.getToken());
    assertEquals(METHOD_OPERATOR, tokenizer.next());
    assertEquals(".getAge", tokenizer.getToken());
    assertEquals(OPENED_PARENS, tokenizer.next());
    assertEquals("(", tokenizer.getToken());
    assertEquals(CLOSED_PARENS, tokenizer.next());
    assertEquals(")", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(GE_OPERATOR, tokenizer.next());
    assertEquals(">=", tokenizer.getToken());
    assertEquals(WHITESPACE, tokenizer.next());
    assertEquals(" ", tokenizer.getToken());
    assertEquals(INT_LITERAL, tokenizer.next());
    assertEquals("18", tokenizer.getToken());
    assertEquals(EOE, tokenizer.next());
    assertNull(tokenizer.getToken());
  }
}
