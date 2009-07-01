package org.seasar.doma.internal.jdbc.type;

import java.sql.CallableStatement;
import java.sql.Clob;
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
public class ClobType implements JdbcType<Clob> {

    @Override
    public Clob getValue(ResultSet resultSet, int index) throws SQLException {
        if (resultSet == null) {
            throw new DomaIllegalArgumentException("resultSet", resultSet);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return resultSet.getClob(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index,
            Clob value) throws SQLException {
        if (preparedStatement == null) {
            throw new DomaIllegalArgumentException("preparedStatement",
                    preparedStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        if (value == null) {
            preparedStatement.setNull(index, Types.CLOB);
        } else {
            preparedStatement.setClob(index, value);
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
        callableStatement.registerOutParameter(index, Types.CLOB);
    }

    @Override
    public Clob getValue(CallableStatement callableStatement, int index)
            throws SQLException {
        if (callableStatement == null) {
            throw new DomaIllegalArgumentException("callableStatement",
                    callableStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return callableStatement.getClob(index);
    }

}
