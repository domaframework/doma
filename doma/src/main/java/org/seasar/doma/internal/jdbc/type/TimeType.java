package org.seasar.doma.internal.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.JdbcType;


/**
 * @author taedium
 * 
 */
public class TimeType implements JdbcType<Time> {

    @Override
    public Time getValue(ResultSet resultSet, int index) throws SQLException {
        if (resultSet == null) {
            throw new DomaIllegalArgumentException("resultSet", resultSet);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return resultSet.getTime(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index,
            Time value) throws SQLException {
        if (preparedStatement == null) {
            throw new DomaIllegalArgumentException("preparedStatement",
                    preparedStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        if (value == null) {
            preparedStatement.setNull(index, Types.TIME);
        } else {
            preparedStatement.setTime(index, value);
        }
    }

    @Override
    public void registerOutParameter(CallableStatement callableStatement,
            int index) throws SQLException {
        if (callableStatement == null) {
            throw new DomaIllegalArgumentException("callableStatement",
                    callableStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        callableStatement.registerOutParameter(index, Types.TIME);
    }

    @Override
    public Time getValue(CallableStatement callableStatement, int index)
            throws SQLException {
        if (callableStatement == null) {
            throw new DomaIllegalArgumentException("callableStatement",
                    callableStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return callableStatement.getTime(index);
    }

}
