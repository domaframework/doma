package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * A JDBC type for {@link Types#TIMESTAMP} and {@link Timestamp}.
 */
public class TimestampType extends AbstractJdbcType<Timestamp> {

    public TimestampType() {
        super(Types.TIMESTAMP);
    }

    @Override
    protected Timestamp doGetValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getTimestamp(index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, Timestamp value)
            throws SQLException {
        preparedStatement.setTimestamp(index, value);
    }

    @Override
    protected Timestamp doGetValue(CallableStatement callableStatement, int index)
            throws SQLException {
        return callableStatement.getTimestamp(index);
    }

    @Override
    protected String doConvertToLogFormat(Timestamp value) {
        return "'" + value + "'";
    }

}
