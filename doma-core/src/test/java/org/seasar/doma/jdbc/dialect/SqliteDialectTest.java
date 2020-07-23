package org.seasar.doma.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class SqliteDialectTest {

  @Test
  public void testIsUniqueConstraintViolated_true() {
    SqliteDialect dialect = new SqliteDialect();
    SQLException e =
        new SQLException(
            "[SQLITE_CONSTRAINT]  Abort due to constraint violation (PRIMARY KEY must be unique)");
    assertTrue(dialect.isUniqueConstraintViolated(e));
  }

  @Test
  public void testIsUniqueConstraintViolated_false() {
    SqliteDialect dialect = new SqliteDialect();
    SQLException e =
        new SQLException(
            "[SQLITE_CONSTRAINT]  Abort due to constraint violation (hoge.foo may not be NULL)");
    assertFalse(dialect.isUniqueConstraintViolated(e));
  }
}
