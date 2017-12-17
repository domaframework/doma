package org.seasar.doma.jdbc.dialect;

import junit.framework.TestCase;

import org.seasar.doma.expr.ExpressionFunctions;

/**
 * @author taedium
 * 
 */
public class Db2DialectTest extends TestCase {

    public void testExpressionFunctions_prefix() throws Exception {
        Db2Dialect dialect = new Db2Dialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a$$a$%a$_a$％a$＿%", functions.prefix("a$a%a_a％a＿"));
    }

    public void testExpressionFunctions_prefix_escape() throws Exception {
        Db2Dialect dialect = new Db2Dialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a!!a!%a!_a!％a!＿%", functions.prefix("a!a%a_a％a＿", '!'));
    }

    public void testExpressionFunctions_prefix_escapeWithDefault() throws Exception {
        Db2Dialect dialect = new Db2Dialect();
        ExpressionFunctions functions = dialect.getExpressionFunctions();
        assertEquals("a\\\\a\\%a\\_a\\％a\\＿%", functions.prefix("a\\a%a_a％a＿", '\\'));
    }

}
