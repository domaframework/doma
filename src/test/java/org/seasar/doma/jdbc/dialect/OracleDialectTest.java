package org.seasar.doma.jdbc.dialect;

import junit.framework.TestCase;

public class OracleDialectTest extends TestCase {

  public void testExpressionFunctions_prefix() throws Exception {
    var dialect = new OracleDialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a％a＿%", functions.prefix("a$a%a_a％a＿"));
  }

  public void testExpressionFunctions_prefix_escape() throws Exception {
    var dialect = new OracleDialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a％a＿%", functions.prefix("a!a%a_a％a＿", '!'));
  }

  public void testExpressionFunctions_prefix_escapeWithBackslash() throws Exception {
    var dialect = new OracleDialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a\\\\a\\%a\\_a％a＿%", functions.prefix("a\\a%a_a％a＿", '\\'));
  }
}
