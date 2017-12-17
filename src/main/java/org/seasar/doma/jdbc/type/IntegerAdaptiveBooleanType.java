package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A JDBC type for {@link Types#INTEGER} and {@link Boolean}.
 */
public class IntegerAdaptiveBooleanType extends AbstractJdbcType<Boolean> {

    public IntegerAdaptiveBooleanType() {
        super(Types.INTEGER);
    }

    @Override
    protected Boolean doGetValue(ResultSet resultSet, int index) throws SQLException {
        int value = resultSet.getInt(index);
        return fromIntToBoolean(value);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, Boolean value)
            throws SQLException {
        int i = fromBooleanToInt(value);
        preparedStatement.setInt(index, i);
    }

    @Override
    protected Boolean doGetValue(CallableStatement callableStatement, int index)
            throws SQLException {
        int value = callableStatement.getInt(index);
        return fromIntToBoolean(value);
    }

    @Override
    protected String doConvertToLogFormat(Boolean value) {
        int i = fromBooleanToInt(value);
        return String.valueOf(i);
    }

    protected int fromBooleanToInt(Boolean value) {
        return value ? 1 : 0;
    }

    protected Boolean fromIntToBoolean(int value) {
        return value == 1;
    }
}
