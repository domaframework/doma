package org.seasar.doma.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class SqliteDialectTest {

  @Test
  public void testIsUniqueConstraintViolated_true_primaryKey() {
    SqliteDialect dialect = new SqliteDialect();
    SQLException e = new SQLException("[SQLITE_CONSTRAINT_PRIMARYKEY] ...");
    assertTrue(dialect.isUniqueConstraintViolated(e));
  }

  @Test
  public void testIsUniqueConstraintViolated_true_uniqueKey() {
    SqliteDialect dialect = new SqliteDialect();
    SQLException e = new SQLException("[SQLITE_CONSTRAINT_UNIQUE] ...");
    assertTrue(dialect.isUniqueConstraintViolated(e));
  }

  @Test
  public void testIsUniqueConstraintViolated_false() {
    SqliteDialect dialect = new SqliteDialect();
    SQLException e = new SQLException("[SQLITE_CONSTRAINT_FOREIGNKEY] ...");
    assertFalse(dialect.isUniqueConstraintViolated(e));
  }
}
