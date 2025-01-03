/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
