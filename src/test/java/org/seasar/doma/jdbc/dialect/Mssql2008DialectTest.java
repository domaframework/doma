package org.seasar.doma.jdbc.dialect;

import junit.framework.TestCase;
import org.seasar.doma.expr.ExpressionFunctions;

public class Mssql2008DialectTest extends TestCase {

  public void testExpressionFunctions_prefix() throws Exception {
    Mssql2008Dialect dialect = new Mssql2008Dialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a$[%", functions.prefix("a$a%a_a["));
  }

  public void testExpressionFunctions_prefix_escape() throws Exception {
    Mssql2008Dialect dialect = new Mssql2008Dialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a![%", functions.prefix("a!a%a_a[", '!'));
  }
}
