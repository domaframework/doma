package org.seasar.doma.internal.jdbc.type;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.jdbc.JdbcType;


/**
 * @author taedium
 * 
 */
public class DateType implements JdbcType<Date> {

    @Override
    public Date getValue(ResultSet resultSet, int index) throws SQLException {
        if (resultSet == null) {
            throw new DomaIllegalArgumentException("resultSet", resultSet);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return resultSet.getDate(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index,
            Date value) throws SQLException {
        if (preparedStatement == null) {
            throw new DomaIllegalArgumentException("preparedStatement",
                    preparedStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        if (value == null) {
            preparedStatement.setNull(index, Types.DATE);
        } else {
            preparedStatement.setDate(index, value);
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
        callableStatement.registerOutParameter(index, Types.DATE);
    }

    @Override
    public Date getValue(CallableStatement callableStatement, int index)
            throws SQLException {
        if (callableStatement == null) {
            throw new DomaIllegalArgumentException("callableStatement",
                    callableStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return callableStatement.getDate(index);
    }

}
