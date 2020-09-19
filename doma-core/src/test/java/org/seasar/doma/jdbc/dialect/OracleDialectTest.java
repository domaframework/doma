package org.seasar.doma.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.expr.ExpressionFunctions;

public class OracleDialectTest {

  @Test
  public void testExpressionFunctions_prefix() {
    OracleDialect dialect = new OracleDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a％a＿%", functions.prefix("a$a%a_a％a＿"));
  }

  @Test
  public void testExpressionFunctions_prefix_escape() {
    OracleDialect dialect = new OracleDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a％a＿%", functions.prefix("a!a%a_a％a＿", '!'));
  }

  @Test
  public void testExpressionFunctions_prefix_escapeWithBackslash() {
    OracleDialect dialect = new OracleDialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a\\\\a\\%a\\_a％a＿%", functions.prefix("a\\a%a_a％a＿", '\\'));
  }
}
