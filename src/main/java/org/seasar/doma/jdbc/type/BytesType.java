package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A JDBC type for {@link Types#BINARY} and {@code byte[]}.
 */
public class BytesType extends AbstractJdbcType<byte[]> {

    public BytesType() {
        super(Types.BINARY);
    }

    @Override
    protected byte[] doGetValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getBytes(index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, byte[] value)
            throws SQLException {
        preparedStatement.setBytes(index, value);
    }

    @Override
    protected byte[] doGetValue(CallableStatement callableStatement, int index)
            throws SQLException {
        return callableStatement.getBytes(index);
    }

    @Override
    protected String doConvertToLogFormat(byte[] value) {
        //noinspection ImplicitArrayToString
        return value.toString();
    }
}
