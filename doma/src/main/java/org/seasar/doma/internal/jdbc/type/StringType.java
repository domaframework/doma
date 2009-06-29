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
public class StringType implements JdbcType<String> {

    @Override
    public String getValue(ResultSet resultSet, int index) throws SQLException {
        if (resultSet == null) {
            throw new DomaIllegalArgumentException("resultSet", resultSet);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return resultSet.getString(index);
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index,
            String value) throws SQLException {
        if (preparedStatement == null) {
            throw new DomaIllegalArgumentException("preparedStatement",
                    preparedStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        if (value == null) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            preparedStatement.setString(index, value);
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
        callableStatement.registerOutParameter(index, Types.VARCHAR);
    }

    @Override
    public String getValue(CallableStatement callableStatement, int index)
            throws SQLException {
        if (callableStatement == null) {
            throw new DomaIllegalArgumentException("callableStatement",
                    callableStatement);
        }
        if (index < 1) {
            throw new DomaIllegalArgumentException("index", index);
        }
        return callableStatement.getString(index);
    }

}
