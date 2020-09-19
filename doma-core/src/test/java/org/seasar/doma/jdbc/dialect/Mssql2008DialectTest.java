package org.seasar.doma.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.expr.ExpressionFunctions;

public class Mssql2008DialectTest {

  @Test
  public void testExpressionFunctions_prefix() {
    Mssql2008Dialect dialect = new Mssql2008Dialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a$[%", functions.prefix("a$a%a_a["));
  }

  @Test
  public void testExpressionFunctions_prefix_escape() {
    Mssql2008Dialect dialect = new Mssql2008Dialect();
    ExpressionFunctions functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a![%", functions.prefix("a!a%a_a[", '!'));
  }
}
