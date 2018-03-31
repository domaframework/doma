package org.seasar.doma.internal.expr;

import junit.framework.TestCase;
import org.seasar.doma.message.Message;

public class ExpressionExceptionTest extends TestCase {

  public void testMethodInvocationFailed() throws Exception {
    var parser = new ExpressionParser("new java.util.ArrayList().get(0)");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3001, e.getMessageResource());
    }
  }

  public void testMethodNotFound() throws Exception {
    var parser = new ExpressionParser("\"aaa\".bbb()");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3002, e.getMessageResource());
    }
  }

  public void testVariableUnresolvable() throws Exception {
    var parser = new ExpressionParser("aaa.eq(100)");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3003, e.getMessageResource());
    }
  }

  public void testDoubleQuotationNotClosed() throws Exception {
    var parser = new ExpressionParser("\"bbb\" == \"bbb");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3004, e.getMessageResource());
    }
  }

  public void testClassNotFound() throws Exception {
    var parser = new ExpressionParser("new MyString()");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3005, e.getMessageResource());
    }
  }

  public void testConstructorNotFound() throws Exception {
    var parser = new ExpressionParser("new java.lang.String(10B)");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3006, e.getMessageResource());
    }
  }

  public void testConstructorInvocationFailed() throws Exception {
    var parser = new ExpressionParser("new java.util.ArrayList(-1)");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3007, e.getMessageResource());
    }
  }

  public void testComparisonFailed_incomparable() throws Exception {
    var parser = new ExpressionParser("1 > true");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3008, e.getMessageResource());
    }
  }

  public void testComparisonFailed_null() throws Exception {
    var parser = new ExpressionParser("1 > null");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3009, e.getMessageResource());
    }
  }

  public void testOperandNotFound() throws Exception {
    var parser = new ExpressionParser("true &&");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3010, e.getMessageResource());
    }
  }

  public void testUnsupportedTokenFound() throws Exception {
    var parser = new ExpressionParser("5 & 5");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3011, e.getMessageResource());
    }
  }

  public void testUnsupportedNumberLiteralFound() throws Exception {
    var parser = new ExpressionParser("5aaa");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3012, e.getMessageResource());
    }
  }

  public void testOperandNotNumber() throws Exception {
    var parser = new ExpressionParser("5 + \"10\"");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3013, e.getMessageResource());
    }
  }

  public void testOperandNotText() throws Exception {
    var parser = new ExpressionParser("\"10\" + 5");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3020, e.getMessageResource());
    }
  }

  public void testArithmeticOperationFailed() throws Exception {
    var parser = new ExpressionParser("5 / 0");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3014, e.getMessageResource());
    }
  }

  public void testArithmeticOperationFailed_null() throws Exception {
    var parser = new ExpressionParser("5 / null");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3015, e.getMessageResource());
    }
  }

  public void testQuotationNotClosed() throws Exception {
    var parser = new ExpressionParser(" 'aaa");
    try {
      parser.parse();
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3016, e.getMessageResource());
    }
  }

  public void testFieldNotFound() throws Exception {
    var parser = new ExpressionParser("\"aaa\".bbb");
    var node = parser.parse();
    var evaluator = new ExpressionEvaluator();
    try {
      evaluator.evaluate(node);
      fail();
    } catch (ExpressionException e) {
      System.out.println(e.getMessage());
      assertEquals(Message.DOMA3018, e.getMessageResource());
    }
  }
}
