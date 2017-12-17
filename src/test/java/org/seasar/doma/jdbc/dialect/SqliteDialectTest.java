package org.seasar.doma.jdbc.dialect;

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class SqliteDialectTest extends TestCase {

    public void testIsUniqueConstraintViolated_true() {
        SqliteDialect dialect = new SqliteDialect();
        SQLException e = new SQLException(
                "[SQLITE_CONSTRAINT]  Abort due to constraint violation (PRIMARY KEY must be unique)");
        assertTrue(dialect.isUniqueConstraintViolated(e));
    }

    public void testIsUniqueConstraintViolated_false() {
        SqliteDialect dialect = new SqliteDialect();
        SQLException e = new SQLException(
                "[SQLITE_CONSTRAINT]  Abort due to constraint violation (hoge.foo may not be NULL)");
        assertFalse(dialect.isUniqueConstraintViolated(e));
    }

}
