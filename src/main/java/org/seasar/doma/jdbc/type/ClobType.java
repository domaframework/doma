package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A JDBC type for {@link Types#CLOB} and {@link Clob}.
 */
public class ClobType extends AbstractJdbcType<Clob> {

    public ClobType() {
        super(Types.CLOB);
    }

    @Override
    protected Clob doGetValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getClob(index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, Clob value)
            throws SQLException {
        preparedStatement.setClob(index, value);
    }

    @Override
    protected Clob doGetValue(CallableStatement callableStatement, int index) throws SQLException {
        return callableStatement.getClob(index);
    }

    @Override
    protected String doConvertToLogFormat(Clob value) {
        return value.toString();
    }

}
