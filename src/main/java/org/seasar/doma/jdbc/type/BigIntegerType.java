package org.seasar.doma.jdbc.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A JDBC type for {@link Types#BIGINT} and {@link BigInteger}.
 */
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
