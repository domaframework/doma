package org.seasar.doma.jdbc.dialect;

import junit.framework.TestCase;
import org.seasar.doma.expr.ExpressionFunctions;

/** @author taedium */
public class OracleDialectTest extends TestCase {

  public void testExpressionFunctions_prefix() throws Exception {
    OracleDialect dialect = new OracleDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a％a＿%", functions.prefix("a$a%a_a％a＿"));
  }

  public void testExpressionFunctions_prefix_escape() throws Exception {
    OracleDialect dialect = new OracleDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a％a＿%", functions.prefix("a!a%a_a％a＿", '!'));
  }

  public void testExpressionFunctions_prefix_escapeWithBackslash() throws Exception {
    OracleDialect dialect = new OracleDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a\\\\a\\%a\\_a％a＿%", functions.prefix("a\\a%a_a％a＿", '\\'));
  }
}
