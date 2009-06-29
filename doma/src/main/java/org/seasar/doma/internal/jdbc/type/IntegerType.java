package org.seasar.doma.internal.jdbc.type;

import java.sql.CallableStatement;
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
public class IntegerType implements JdbcType<Integer> {

    @Override
    public Integer getValue(ResultSet resultSet, int index) throws SQLException {
        if (resultSet == null) {
            throw new DomaIllegalArgumentException("resultSet", resultSet);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return resultSet.getInt(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index,
            Integer value) throws SQLException {
        if (preparedStatement == null) {
            throw new DomaIllegalArgumentException("preparedStatement",
                    preparedStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        if (value == null) {
            preparedStatement.setNull(index, Types.INTEGER);
        } else {
            preparedStatement.setInt(index, value);
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
        callableStatement.registerOutParameter(index, Types.INTEGER);
    }

    @Override
    public Integer getValue(CallableStatement callableStatement, int index)
            throws SQLException {
        if (callableStatement == null) {
            throw new DomaIllegalArgumentException("callableStatement",
                    callableStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return callableStatement.getInt(index);
    }

}
