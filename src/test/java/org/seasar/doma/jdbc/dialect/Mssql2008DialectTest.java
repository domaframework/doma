package org.seasar.doma.jdbc.dialect;

import junit.framework.TestCase;

public class Mssql2008DialectTest extends TestCase {

  public void testExpressionFunctions_prefix() throws Exception {
    var dialect = new Mssql2008Dialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a$$a$%a$_a$[%", functions.prefix("a$a%a_a["));
  }

  public void testExpressionFunctions_prefix_escape() throws Exception {
    var dialect = new Mssql2008Dialect();
    var functions = dialect.getExpressionFunctions();
    assertEquals("a!!a!%a!_a![%", functions.prefix("a!a%a_a[", '!'));
  }
}
