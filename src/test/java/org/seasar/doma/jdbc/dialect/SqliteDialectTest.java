package org.seasar.doma.jdbc.dialect;

import java.sql.SQLException;
import junit.framework.TestCase;

public class SqliteDialectTest extends TestCase {

  public void testIsUniqueConstraintViolated_true() {
    var dialect = new SqliteDialect();
    var e =
        new SQLException(
            "[SQLITE_CONSTRAINT]  Abort due to constraint violation (PRIMARY KEY must be unique)");
    assertTrue(dialect.isUniqueConstraintViolated(e));
  }

  public void testIsUniqueConstraintViolated_false() {
    var dialect = new SqliteDialect();
    var e =
        new SQLException(
            "[SQLITE_CONSTRAINT]  Abort due to constraint violation (hoge.foo may not be NULL)");
    assertFalse(dialect.isUniqueConstraintViolated(e));
  }
}
