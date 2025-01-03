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
package org.seasar.doma.jdbc.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;

/** A JDBC type for {@link Types#BIGINT} and {@link BigInteger}. */
public class BigIntegerType extends AbstractJdbcType<BigInteger> {

  public BigIntegerType() {
    super(Types.BIGINT);
  }

  @Override
  protected BigInteger doGetValue(ResultSet resultSet, int index) throws SQLException {
    BigDecimal decimal = resultSet.getBigDecimal(index);
    return decimal != null ? decimal.toBigInteger() : null;
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, BigInteger value)
      throws SQLException {
    preparedStatement.setBigDecimal(index, new BigDecimal(value));
  }

  @Override
  protected BigInteger doGetValue(CallableStatement callableStatement, int index)
      throws SQLException {
    BigDecimal decimal = callableStatement.getBigDecimal(index);
    return decimal != null ? decimal.toBigInteger() : null;
  }

  @Override
  protected String doConvertToLogFormat(BigInteger value) {
    return value.toString();
  }
}
