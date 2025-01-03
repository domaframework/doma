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
package org.seasar.doma.jdbc;

import java.util.Collections;
import org.junit.jupiter.api.Test;

public class UtilLoggingJdbcLoggerTest {

  @Test
  public void testLogSql() {
    PreparedSql sql =
        new PreparedSql(
            SqlKind.SELECT, "aaa", "bbb", "ccc", Collections.emptyList(), SqlLogType.FORMATTED);
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logSql("ddd", "eee", sql);
  }

  @Test
  public void testLogLocalTransactionBegun() {
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logTransactionBegun("ddd", "eee", "fff");
  }

  @Test
  public void testLogLocalTransactionCommitted() {
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logTransactionCommitted("ddd", "eee", "fff");
  }

  @Test
  public void testLogLocalTransactionRolledback() {
    UtilLoggingJdbcLogger logger = new UtilLoggingJdbcLogger();
    logger.logTransactionRolledback("ddd", "eee", "fff");
  }
}
