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
package org.seasar.doma.it;

import java.math.BigDecimal;
import java.sql.SQLException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.jdbc.dialect.SqliteDialect.SqliteJdbcMappingVisitor;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;

public class CustomSqliteJdbcMappingVisitor extends SqliteJdbcMappingVisitor {

  @Override
  public Void visitBigDecimalWrapper(
      BigDecimalWrapper wrapper, JdbcMappingFunction p, JdbcMappingHint q) throws SQLException {
    BigDecimal decimal = wrapper.get();
    IntegerWrapper integerWrapper;
    if (decimal == null) {
      integerWrapper = new IntegerWrapper(null);
    } else {
      integerWrapper = new IntegerWrapper(decimal.intValue());
    }
    super.visitIntegerWrapper(integerWrapper, p, q);
    Integer integer = integerWrapper.get();
    if (integer != null) {
      wrapper.set(integer);
    }
    return null;
  }
}
