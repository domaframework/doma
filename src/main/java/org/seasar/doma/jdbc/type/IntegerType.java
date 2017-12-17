package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A JDBC type for {@link Types#INTEGER} and {@link Integer}.
 */
public class IntegerType extends AbstractJdbcType<Integer> {

    public IntegerType() {
        super(Types.INTEGER);
    }

    @Override
    protected Integer doGetValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getInt(index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, Integer value)
            throws SQLException {
        preparedStatement.setInt(index, value);
    }

    @Override
    protected Integer doGetValue(CallableStatement callableStatement, int index)
            throws SQLException {
        return callableStatement.getInt(index);
    }

    @Override
    protected String doConvertToLogFormat(Integer value) {
        return String.valueOf(value);
    }

}
